/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.constraints;

import java.util.Objects;

import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;

/**
 * A constraint requiring a paremeter to be castable as a long.
 *
 * @author Christophe Lauret
 */
public final class LongParameterConstraint implements Constraint {

  /**
   * Name of the parameter.
   */
  private final String _name;

  /**
   * Whether the parameter is required
   */
  private final boolean _required;

  private final long _min;

  private final long _max;

  public LongParameterConstraint(String name, boolean required) {
    this(name, required, Long.MIN_VALUE, Long.MAX_VALUE);
  }

  public LongParameterConstraint(String name, boolean required, long min, long max) {
    this._name = Objects.requireNonNull(name);
    if (name.length() == 0) throw new IllegalArgumentException();
    if (min >= max) throw new IllegalArgumentException();
    this._required = required;
    this._min = min;
    this._max = max;
  }

  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    String value = req.getParameter(this._name);
    ContentStatus status = ContentStatus.OK;
    if (value == null) {
      if (this._required) return RequiredParameterConstraint.failedRequired(this._name, xml);
      return ContentStatus.OK;
    } else {
      try {
        long v = Long.parseLong(value);
        if (v < this._min || v > this._max) {
          xml.openElement("error");
          xml.attribute("type", "out-of-range");
          xml.attribute("parameter", this._name);
          xml.attribute("min", Long.toString(this._min));
          xml.attribute("max", Long.toString(this._max));
          xml.closeElement();
          status = ContentStatus.BAD_REQUEST;
        }
      } catch (NumberFormatException ex) {
        xml.openElement("error");
        xml.attribute("type", "invalid-long-parameter");
        xml.attribute("parameter", this._name);
        xml.closeElement();
        status = ContentStatus.BAD_REQUEST;
      }
    }
    return status;
  }

}
