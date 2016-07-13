/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.constraints;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.temporal.Temporal;
import java.util.Objects;

import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;

/**
 * A constraint requiring a parameter to be a valid email address.
 *
 * Note: this constraint only check against the pattern if the parameter
 * is specified.
 *
 * @author Christophe Lauret
 */
public final class TemporalParameterConstraint implements Constraint {

  /**
   * Name of the parameter.
   */
  private final String _name;

  /**
   * Whether the parameter is required
   */
  private final boolean _required;

  private final Class<? extends Temporal> _type;


  public TemporalParameterConstraint(String name, boolean required, Class<? extends Temporal> type) {
    this._name = Objects.requireNonNull(name);
    this._required = required;
    if (name.length() == 0) throw new IllegalArgumentException();
    this._type = type;
  }

  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    String value = req.getParameter(this._name);
    if (value == null) {
      if (this._required) return RequiredParameterConstraint.failedRequired(this._name, xml);
      return ContentStatus.OK;
    } else {
      if (isParsableAs(value, this._type)) return ContentStatus.OK;
      xml.openElement("error");
      xml.attribute("type", "invalid-email");
      xml.attribute("parameter", this._name);
      xml.closeElement();
      return ContentStatus.BAD_REQUEST;
    }
  }


  protected static boolean isParsableAs(String value, Class<? extends Temporal> type) {
    boolean parsable = false;
    // TODO We could improve this for common types (LocalDate, etc...)
    try {
      Method m = type.getMethod("parse", CharSequence.class);
      try {
        m.invoke(new Object(), value);
        parsable = true;
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        // not parsable
      }
    } catch (NoSuchMethodException | SecurityException ex) {
      throw new IllegalStateException();
    }
    return parsable;
  }

}
