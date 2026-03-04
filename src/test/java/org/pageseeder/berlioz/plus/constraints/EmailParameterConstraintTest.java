package org.pageseeder.berlioz.plus.constraints;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;
import org.pageseeder.berlioz.plus.test.MapBackedContentRequest;
import org.pageseeder.xmlwriter.XML;
import org.pageseeder.xmlwriter.XMLStringWriter;

class EmailParameterConstraintTest {

  @Test
  void constructor_nullName_throwsNpe() {
    assertThrows(NullPointerException.class, () -> new EmailParameterConstraint(null, false));
  }

  @Test
  void constructor_emptyName_throwsIae() {
    assertThrows(IllegalArgumentException.class, () -> new EmailParameterConstraint("", false));
  }

  @Test
  void validate_parameterMissing_notRequired_returnsOk_andDoesNotWriteXml() {
    EmailParameterConstraint c = new EmailParameterConstraint("email", false);
    ContentRequest req = new MapBackedContentRequest(Map.of()); // no "email"
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);
    ContentStatus status = c.validate(req, new XMLPrinter(xml));
    assertEquals(ContentStatus.OK, status);
    assertTrue(xml.toString().isEmpty(), "No XML should be written when the optional parameter is missing");
  }

  @Test
  void validate_parameterMissing_required_returnsBadRequest() {
    EmailParameterConstraint c = new EmailParameterConstraint("email", true);
    ContentRequest req = new MapBackedContentRequest(Map.of()); // no "email"
    XMLPrinter xml = new XMLPrinter(new XMLStringWriter(XML.NamespaceAware.No));

    ContentStatus status = c.validate(req, xml);

    // The exact XML is produced by RequiredParameterConstraint; here we just assert the outcome.
    assertEquals(ContentStatus.BAD_REQUEST, status);
  }

  @Test
  void validate_validEmail_returnsOk_andDoesNotWriteXml() {
    EmailParameterConstraint c = new EmailParameterConstraint("email", true);
    List<String> emails = List.of("john.doe@example.com", "jane.doe@example.com", "user+tag@example.com");
    for (String email : emails) {
      ContentRequest req = new MapBackedContentRequest(Map.of("email", email));
      XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);
      ContentStatus status = c.validate(req, new XMLPrinter(xml));
      assertEquals(ContentStatus.OK, status);
      assertTrue(xml.toString().isEmpty(), "No XML should be written for a valid email");
    }
  }

  @Test
  void validate_invalidEmail_returnsBadRequest_andWritesInvalidEmailErrorXml() {
    EmailParameterConstraint c = new EmailParameterConstraint("email", true);
    ContentRequest req = new MapBackedContentRequest(Map.of("email", "not-an-email"));
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);
    ContentStatus status = c.validate(req, new XMLPrinter(xml));
    assertEquals(ContentStatus.BAD_REQUEST, status);
    assertTrue(xml.toString().contains("invalid-email"), "XML should contain 'invalid-email' error type");
  }

}