package org.pageseeder.berlioz.plus.constraints;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.Temporal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;
import org.pageseeder.berlioz.plus.test.MapBackedContentRequest;
import org.pageseeder.xmlwriter.XML;
import org.pageseeder.xmlwriter.XMLStringWriter;

class TemporalParameterConstraintTest {

  @Test
  void constructor_nullName_throwsNpe() {
    assertThrows(NullPointerException.class,
        () -> new TemporalParameterConstraint(null, false, LocalDate.class));
  }

  @Test
  void constructor_emptyName_throwsIae() {
    assertThrows(IllegalArgumentException.class,
        () -> new TemporalParameterConstraint("", false, LocalDate.class));
  }

  @Test
  void validate_parameterMissing_notRequired_returnsOk_andDoesNotWriteXml() {
    TemporalParameterConstraint c = new TemporalParameterConstraint("date", false, LocalDate.class);
    ContentRequest req = new MapBackedContentRequest(Map.of()); // no "date"
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.OK, status);
    assertTrue(xml.toString().isEmpty(), "No XML should be written when the optional parameter is missing");
  }

  @Test
  void validate_parameterMissing_required_returnsBadRequest() {
    TemporalParameterConstraint c = new TemporalParameterConstraint("date", true, LocalDate.class);
    ContentRequest req = new MapBackedContentRequest(Map.of()); // no "date"
    XMLPrinter xml = new XMLPrinter(new XMLStringWriter(XML.NamespaceAware.No));

    ContentStatus status = c.validate(req, xml);

    // The exact XML is produced by RequiredParameterConstraint; here we just assert the outcome.
    assertEquals(ContentStatus.BAD_REQUEST, status);
  }

  @Test
  void validate_validLocalDate_returnsOk_andDoesNotWriteXml() {
    TemporalParameterConstraint c = new TemporalParameterConstraint("date", true, LocalDate.class);
    ContentRequest req = new MapBackedContentRequest(Map.of("date", "2025-03-04"));
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.OK, status);
    assertTrue(xml.toString().isEmpty(), "No XML should be written for a valid temporal value");
  }

  @Test
  void validate_invalidLocalDate_returnsBadRequest_andWritesErrorXml() {
    TemporalParameterConstraint c = new TemporalParameterConstraint("date", true, LocalDate.class);
    ContentRequest req = new MapBackedContentRequest(Map.of("date", "not-a-date"));
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.BAD_REQUEST, status);
    assertTrue(xml.toString().contains("invalid-localdate"),
        "XML should contain the error type written by the constraint");
  }

  @Test
  void validate_validYear_usesReflectionPath_returnsOk_andDoesNotWriteXml() {
    TemporalParameterConstraint c = new TemporalParameterConstraint("year", true, Year.class);
    ContentRequest req = new MapBackedContentRequest(Map.of("year", "2026"));
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.OK, status);
    assertTrue(xml.toString().isEmpty(), "No XML should be written for a valid temporal value");
  }

  @Test
  void validate_invalidYear_usesReflectionPath_returnsBadRequest_andWritesErrorXml() {
    TemporalParameterConstraint c = new TemporalParameterConstraint("year", true, Year.class);
    ContentRequest req = new MapBackedContentRequest(Map.of("year", "not-a-year"));
    XMLStringWriter xml = new XMLStringWriter(XML.NamespaceAware.No);

    ContentStatus status = c.validate(req, new XMLPrinter(xml));

    assertEquals(ContentStatus.BAD_REQUEST, status);
    assertTrue(xml.toString().contains("invalid-year"),
        "XML should contain the error type written by the constraint");
  }

  @Test
  void validate_typeWithoutParseMethod_throwsIllegalStateException() {
    TemporalParameterConstraint c = new TemporalParameterConstraint("t", true, Temporal.class);
    ContentRequest req = new MapBackedContentRequest(Map.of("t", "anything"));
    XMLPrinter xml = new XMLPrinter(new XMLStringWriter(XML.NamespaceAware.No));

    assertThrows(IllegalStateException.class, () -> c.validate(req, xml));
  }

  @Test
  void validate_nullType_withValuePresent_throwsNpe() {
    TemporalParameterConstraint c = new TemporalParameterConstraint("t", true, null);
    ContentRequest req = new MapBackedContentRequest(Map.of("t", "2025-03-04"));
    XMLPrinter xml = new XMLPrinter(new XMLStringWriter(XML.NamespaceAware.No));

    assertThrows(NullPointerException.class, () -> c.validate(req, xml));
  }
}