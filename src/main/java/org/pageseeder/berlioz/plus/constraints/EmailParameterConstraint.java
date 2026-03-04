/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.constraints;

import java.util.Objects;
import java.util.regex.Pattern;

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
public final class EmailParameterConstraint implements Constraint {

  /**
   * Regular expression pattern matching an email address.
   */
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[\\w\\-]([\\.\\w\\-\\`\\'])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

  /**
   * Name of the parameter.
   */
  private final String name;

  /**
   * Whether the parameter is required
   */
  private final boolean required;

  public EmailParameterConstraint(String name, boolean required) {
    this.name = Objects.requireNonNull(name);
    this.required = required;
    if (name.isEmpty()) throw new IllegalArgumentException("Parameter name cannot be empty");
  }

  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    String value = req.getParameter(this.name);
    if (value == null) {
      if (this.required) return RequiredParameterConstraint.failedRequired(this.name, xml);
      return ContentStatus.OK;
    } else {
      if (EMAIL_PATTERN.matcher(value).matches()) return ContentStatus.OK;
      xml.openElement("error");
      xml.attribute("type", "invalid-email");
      xml.attribute("parameter", this.name);
      xml.closeElement();
      return ContentStatus.BAD_REQUEST;
    }
  }

}
