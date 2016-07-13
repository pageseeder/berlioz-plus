/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.constraints;

import java.util.Objects;

import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;

/**
 * A constraint requiring a parameter to be specified and not empty.
 *
 * @author Christophe Lauret
 */
public final class RequiredParameterConstraint implements Constraint {

  /** Name of the parameter */
  private final String _name;

  public RequiredParameterConstraint(String name) {
    this._name = Objects.requireNonNull(name);
    if (name.length() == 0) throw new IllegalArgumentException();
  }

  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    String value = req.getParameter(this._name);
    if (value != null) return ContentStatus.OK;
    return failedRequired(this._name, xml);
  }

  public static ContentStatus failedRequired(String name, XMLPrinter xml) {
    xml.openElement("error");
    xml.attribute("type", "missing-parameter");
    xml.attribute("parameter", name);
    xml.closeElement();
    return ContentStatus.BAD_REQUEST;
  }

}