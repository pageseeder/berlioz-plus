/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.constraints;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.jspecify.annotations.Nullable;
import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;

/**
 * A constraint requiring a user to be authenticated.
 *
 * <p>Note: this constraint only checks against the pattern if the parameter
 * is specified.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public final class ParameterContraint implements Constraint {

  /**
   * Name of the parameter.
   */
  private final String name;

  /**
   * Whether the parameter is required
   */
  private final boolean required;

  /**
   * Regular expression pattern.
   */
  @Nullable
  private final Pattern pattern;

  public ParameterContraint(String name, boolean required, String regex) {
    this.name = Objects.requireNonNull(name);
    if (name.isEmpty()) throw new IllegalArgumentException("Parameter name cannot be empty");
    this.required = required;
    if (!regex.isEmpty()) {
      try {
        this.pattern = Pattern.compile(regex);
      } catch (PatternSyntaxException ex) {
        throw new IllegalArgumentException("Invalid pattern", ex);
      }
    } else {
      this.pattern = null;
    }
  }

  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    String value = req.getParameter(this.name);
    if (value == null) {
      if (this.required) return RequiredParameterConstraint.failedRequired(this.name, xml);
      return ContentStatus.OK;
    } else {
      Pattern p = this.pattern;
      if (p != null) {
        if (p.matcher(value).matches()) return ContentStatus.OK;
        else {
          xml.openElement("error");
          xml.attribute("type", "invalid-parameter");
          xml.attribute("parameter", this.name);
          xml.attribute("pattern", p.toString());
          xml.closeElement();
          return ContentStatus.BAD_REQUEST;
        }
      } else return ContentStatus.OK;
    }
  }

}
