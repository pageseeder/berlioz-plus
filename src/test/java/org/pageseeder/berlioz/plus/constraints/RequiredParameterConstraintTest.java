package org.pageseeder.berlioz.plus.constraints;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;
import org.pageseeder.berlioz.plus.test.MapBackedContentRequest;
import org.pageseeder.xmlwriter.XML;
import org.pageseeder.xmlwriter.XMLStringWriter;

class RequiredParameterConstraintTest {

  @Test
  void constructor_nullName_throwsNpe() {
    assertThrows(NullPointerException.class, () -> new RequiredParameterConstraint(null));
  }

  @Test
  void constructor_emptyName_throwsIae() {
    assertThrows(IllegalArgumentException.class, () -> new RequiredParameterConstraint(""));
  }

  @Test
  void validate_parameterPresent_returnsOk_andDoesNotWriteXml() {
    RequiredParameterConstraint c = new RequiredParameterConstraint("id");
    ContentRequest req = new MapBackedContentRequest(Map.of("id", "123"));
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.OK, status);
    assertTrue(xml.toString().isEmpty(), "No XML should be written when the parameter is present");
  }

  @Test
  void validate_parameterMissing_returnsBadRequest_andWritesMissingParameterErrorXml() {
    RequiredParameterConstraint c = new RequiredParameterConstraint("id");
    ContentRequest req = new MapBackedContentRequest(Map.of()); // no "id"
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.BAD_REQUEST, status);

    String out = xml.toString();
    assertTrue(out.contains("missing-parameter"), "XML should contain 'missing-parameter' error type");
    assertTrue(out.contains("parameter=\"id\"") || out.contains("parameter='id'"),
        "XML should include the missing parameter name");
  }

  @Test
  void failedRequired_writesErrorXml_andReturnsBadRequest() {
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = RequiredParameterConstraint.failedRequired("id", new XMLPrinter(xml));

    assertEquals(ContentStatus.BAD_REQUEST, status);

    String out = xml.toString();
    assertTrue(out.contains("<error"), "XML should contain an <error> element");
    assertTrue(out.contains("missing-parameter"), "XML should contain 'missing-parameter' error type");
    assertTrue(out.contains("parameter=\"id\"") || out.contains("parameter='id'"),
        "XML should include the missing parameter name");
  }
}