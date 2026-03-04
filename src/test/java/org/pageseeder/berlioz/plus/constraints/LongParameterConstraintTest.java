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

class LongParameterConstraintTest {

  @Test
  void constructor_nullName_throwsNpe() {
    assertThrows(NullPointerException.class, () -> new LongParameterConstraint(null, false));
  }

  @Test
  void constructor_emptyName_throwsIae() {
    assertThrows(IllegalArgumentException.class, () -> new LongParameterConstraint("", false));
  }

  @Test
  void constructor_minGreaterThanOrEqualMax_throwsIae() {
    assertThrows(IllegalArgumentException.class, () -> new LongParameterConstraint("id", false, 10L, 10L));
    assertThrows(IllegalArgumentException.class, () -> new LongParameterConstraint("id", false, 11L, 10L));
  }

  @Test
  void validate_parameterMissing_notRequired_returnsOk_andDoesNotWriteXml() {
    LongParameterConstraint c = new LongParameterConstraint("id", false);
    ContentRequest req = new MapBackedContentRequest(Map.of()); // no "id"
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.OK, status);
    assertTrue(xml.toString().isEmpty(), "No XML should be written when the optional parameter is missing");
  }

  @Test
  void validate_parameterMissing_required_returnsBadRequest() {
    LongParameterConstraint c = new LongParameterConstraint("id", true);
    ContentRequest req = new MapBackedContentRequest(Map.of()); // no "id"
    XMLPrinter xml = new XMLPrinter(new XMLStringWriter(XML.NamespaceAware.No));

    ContentStatus status = c.validate(req, xml);

    // The exact XML is produced by RequiredParameterConstraint; here we just assert the outcome.
    assertEquals(ContentStatus.BAD_REQUEST, status);
  }

  @Test
  void validate_validLong_returnsOk_andDoesNotWriteXml() {
    LongParameterConstraint c = new LongParameterConstraint("id", true);
    ContentRequest req = new MapBackedContentRequest(Map.of("id", "123"));
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.OK, status);
    assertTrue(xml.toString().isEmpty(), "No XML should be written for a valid long");
  }

  @Test
  void validate_invalidLong_returnsBadRequest_andWritesInvalidLongErrorXml() {
    LongParameterConstraint c = new LongParameterConstraint("id", true);
    ContentRequest req = new MapBackedContentRequest(Map.of("id", "not-a-long"));
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.BAD_REQUEST, status);
    assertTrue(xml.toString().contains("invalid-long-parameter"),
        "XML should contain 'invalid-long-parameter' error type");
  }

  @Test
  void validate_outOfRange_returnsBadRequest_andWritesOutOfRangeErrorXml() {
    LongParameterConstraint c = new LongParameterConstraint("id", true, 10L, 20L);
    ContentRequest req = new MapBackedContentRequest(Map.of("id", "21"));
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.BAD_REQUEST, status);
    String out = xml.toString();
    assertTrue(out.contains("out-of-range"), "XML should contain 'out-of-range' error type");
    assertTrue(out.contains("min=\"10\"") || out.contains("min='10'"), "XML should include the min attribute");
    assertTrue(out.contains("max=\"20\"") || out.contains("max='20'"), "XML should include the max attribute");
  }
}
