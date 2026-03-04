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
 * <p>Note: this constraint only checks against the pattern if the parameter
 * is specified.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public final class TemporalParameterConstraint implements Constraint {

  /**
   * Name of the parameter.
   */
  private final String name;

  /**
   * Whether the parameter is required
   */
  private final boolean required;

  /**
   * Represents the type of temporal value expected for the constraint.
   *
   * <p>This variable holds a {@link Class} object representing a subclass of
   * {@link Temporal} to indicate the specific temporal type
   * (e.g., {@link java.time.LocalDate}, {@link java.time.LocalDateTime}, etc.)
   * expected by the implementation of the constraint.
   *
   * <p>This is used to determine if a given parameter can be successfully parsed
   * as the specified temporal type.
   */
  private final Class<? extends Temporal> type;

  /**
   * Constructs a {@code TemporalParameterConstraint} to validate a temporal parameter.
   *
   * @param name     The name of the parameter; must not be null or empty.
   * @param required Whether the parameter is required.
   * @param type     The {@link Temporal} subclass that the parameter value is expected to parse as.
   *
   * @throws NullPointerException     If {@code name} is null.
   * @throws IllegalArgumentException If {@code name} is empty.
   */
  public TemporalParameterConstraint(String name, boolean required, Class<? extends Temporal> type) {
    this.name = Objects.requireNonNull(name);
    this.required = required;
    if (name.isEmpty()) throw new IllegalArgumentException("Parameter name cannot be empty");
    this.type = type;
  }

  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    String value = req.getParameter(this.name);
    if (value == null) {
      if (this.required) return RequiredParameterConstraint.failedRequired(this.name, xml);
      return ContentStatus.OK;
    } else {
      if (isParsableAs(value, this.type)) return ContentStatus.OK;
      xml.openElement("error");
      xml.attribute("type", "invalid-email");
      xml.attribute("parameter", this.name);
      xml.closeElement();
      return ContentStatus.BAD_REQUEST;
    }
  }

  private static boolean isParsableAs(String value, Class<? extends Temporal> type) {
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
