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
 * Note: this constraint only check against the pattern if the parameter
 * is specified.
 *
 * @author Christophe Lauret
 */
public final class EmailParameterConstraint implements Constraint {

  /**
   * Regular expression pattern matching an email address.
   */
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w\\-]([\\.\\w\\-\\`\\'])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

  /**
   * Name of the parameter.
   */
  private final String _name;

  /**
   * Whether the parameter is required
   */
  private final boolean _required;

  public EmailParameterConstraint(String name, boolean required) {
    this._name = Objects.requireNonNull(name);
    this._required = required;
    if (name.length() == 0) throw new IllegalArgumentException();
  }

  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    String value = req.getParameter(this._name);
    if (value == null) {
      if (this._required) return RequiredParameterConstraint.failedRequired(this._name, xml);
      return ContentStatus.OK;
    } else {
      if (EMAIL_PATTERN.matcher(value).matches()) return ContentStatus.OK;
      xml.openElement("error");
      xml.attribute("type", "invalid-email");
      xml.attribute("parameter", this._name);
      xml.closeElement();
      return ContentStatus.BAD_REQUEST;
    }
  }

}
