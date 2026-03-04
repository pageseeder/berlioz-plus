/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.constraints;

import java.util.Objects;

import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;

/**
 * A constraint requiring a parameter to be castable as a long value.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public final class LongParameterConstraint implements Constraint {

  /**
   * Name of the parameter.
   */
  private final String name;

  /**
   * Whether the parameter is required
   */
  private final boolean required;

  private final long min;

  private final long max;

  /**
   * Constructs a {@code LongParameterConstraint} with the specified parameter name and
   * whether it is required. The constraint will allow any value within the full range
   * of {@code Long} values.
   *
   * @param name     The name of the parameter to be constrained.
   * @param required Whether the parameter is mandatory.
   *
   * @throws NullPointerException     If the {@code name} is {@code null}.
   * @throws IllegalArgumentException If the {@code name} is empty.
   */
  public LongParameterConstraint(String name, boolean required) {
    this(name, required, Long.MIN_VALUE, Long.MAX_VALUE);
  }

  /**
   * Constructs a {@code LongParameterConstraint} with the specified parameter name,
   * requirement status, and range constraints.
   *
   * @param name     The name of the parameter to be constrained. Must not be null or empty.
   * @param required Indicates whether the parameter is mandatory.
   * @param min      The minimum allowable value for the parameter (inclusive).
   * @param max      The maximum allowable value for the parameter (inclusive).
   *
   * @throws NullPointerException     If the {@code name} is {@code null}.
   * @throws IllegalArgumentException If the {@code name} is empty or if {@code min} is greater than or equal to {@code max}.
   */
  public LongParameterConstraint(String name, boolean required, long min, long max) {
    this.name = Objects.requireNonNull(name);
    if (name.isEmpty()) throw new IllegalArgumentException("Parameter name cannot be empty");
    if (min >= max) throw new IllegalArgumentException("Minimum must be less than maximum");
    this.required = required;
    this.min = min;
    this.max = max;
  }

  /**
   * Validates the specified {@code ContentRequest} based on the constraints defined for this parameter.
   * If the parameter value is missing, invalid, or out of the permissible range, appropriate error
   * information is appended to the provided {@code XMLPrinter}.
   *
   * @param req The {@code ContentRequest} containing the parameter to validate.
   * @param xml The {@code XMLPrinter} used to append any error information during validation.
   * @return A {@code ContentStatus} indicating the result of the validation. Returns {@code ContentStatus.OK}
   *         if the parameter is valid or not required, and {@code ContentStatus.BAD_REQUEST} for invalid parameters.
   */
  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    String value = req.getParameter(this.name);
    ContentStatus status = ContentStatus.OK;
    if (value == null) {
      if (this.required) return RequiredParameterConstraint.failedRequired(this.name, xml);
      return ContentStatus.OK;
    } else {
      try {
        long v = Long.parseLong(value);
        if (v < this.min || v > this.max) {
          xml.openElement("error");
          xml.attribute("type", "out-of-range");
          xml.attribute("parameter", this.name);
          xml.attribute("min", Long.toString(this.min));
          xml.attribute("max", Long.toString(this.max));
          xml.closeElement();
          status = ContentStatus.BAD_REQUEST;
        }
      } catch (NumberFormatException ex) {
        xml.openElement("error");
        xml.attribute("type", "invalid-long-parameter");
        xml.attribute("parameter", this.name);
        xml.closeElement();
        status = ContentStatus.BAD_REQUEST;
      }
    }
    return status;
  }

}
