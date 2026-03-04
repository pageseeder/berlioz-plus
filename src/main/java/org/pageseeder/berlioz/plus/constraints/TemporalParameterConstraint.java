/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.constraints;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jspecify.annotations.Nullable;
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
   * Cache reflective parse methods to avoid repeated lookups.
   * Value is {@code null} when the type has no suitable parse(CharSequence) method.
   */
  private static final ConcurrentMap<Class<? extends Temporal>, Method> PARSE_METHOD_CACHE = new ConcurrentHashMap<>();

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

  /**
   * Determines if a given string value can be parsed into a specified type that implements
   * the {@code Temporal} interface.
   *
   * @param value The string value to be checked for parsability; can be {@code null}.
   * @param type  The {@code Class} object representing the type extending {@code Temporal}
   *              to which the value is expected to be parsed; must not be {@code null}.
   *
   * @return {@code true} if the value can be successfully parsed as the specified temporal type;
   *         {@code false} otherwise.
   *
   * @throws NullPointerException If the {@code type} parameter is {@code null}.
   */
  private static boolean isParsableAs(@Nullable String value, Class<? extends Temporal> type) {
    if (value == null) return false;
    Objects.requireNonNull(type, "Temporal type must not be null");

    // Fast-path for common Java time types
    // These avoid reflection and are generally the hottest cases.
    try {
      if (type == LocalDate.class) {
        LocalDate.parse(value);
        return true;
      }
      if (type == LocalDateTime.class) {
        LocalDateTime.parse(value);
        return true;
      }
      if (type == LocalTime.class) {
        LocalTime.parse(value);
        return true;
      }
      if (type == OffsetDateTime.class) {
        OffsetDateTime.parse(value);
        return true;
      }
      if (type == OffsetTime.class) {
        OffsetTime.parse(value);
        return true;
      }
      if (type == ZonedDateTime.class) {
        ZonedDateTime.parse(value);
        return true;
      }
      if (type == Instant.class) {
        Instant.parse(value);
        return true;
      }
    } catch (RuntimeException ex) {
      // Not parsable for this common type (e.g., DateTimeParseException)
      return false;
    }

    return isParsableAsOtherTemporal(value, type);
  }

  /**
   * Checks if a given string value can be parsed into a specified type that implements the {@code Temporal} interface.
   *
   * @param value The string value to be checked for parsability; must not be null.
   * @param type  The class type extending {@code Temporal} to which the value is expected to be parsed; must not be null.
   *
   * @return {@code true} if the value can be successfully parsed as the specified temporal type; {@code false} otherwise.
   *
   * @throws IllegalStateException If the specified type does not have a {@code parse(CharSequence)} method or if access
   *                               to the method is not permitted.
   */
  private static boolean isParsableAsOtherTemporal(String value, Class<? extends Temporal> type) {
    // Generic path: cached reflective lookup of static parse(CharSequence)
    Method parse = PARSE_METHOD_CACHE.computeIfAbsent(type, t -> {
      try {
        // Must be static for java.time types; the invocation target will be null.
        return t.getMethod("parse", CharSequence.class);
      } catch (NoSuchMethodException ex) {
        return null; // cached "no parser" for this type
      } catch (SecurityException ex) {
        throw new IllegalStateException("Cannot access parse(CharSequence) on " + t.getName(), ex);
      }
    });

    if (parse == null) {
      throw new IllegalStateException("No parse(CharSequence) method found on " + type.getName());
    }

    try {
      parse.invoke(null, value); // static method => null target
      return true;
    } catch (IllegalAccessException ex) {
      throw new IllegalStateException("Cannot invoke parse(CharSequence) on " + type.getName(), ex);
    } catch (InvocationTargetException | IllegalArgumentException ex) {
      // InvocationTargetException: Parsing failed; most commonly wraps DateTimeParseException
      // IllegalArgumentException: Should not happen with the correct signature, but treat as not parsable
      return false;
    }
  }

}
