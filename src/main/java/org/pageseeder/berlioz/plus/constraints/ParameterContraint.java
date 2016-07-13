/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.constraints;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jdt.annotation.Nullable;
import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;

/**
 * A constraint requiring a user to be authenticated.
 *
 * Note: this constraint only check against the pattern if the parameter
 * is specified.
 *
 * @author Christophe Lauret
 */
public final class ParameterContraint implements Constraint {

  /**
   * Name of the parameter.
   */
  private final String _name;

  /**
   * Whether the parameter is required
   */
  private final boolean _required;

  /**
   * Regular expression pattern.
   */
  @Nullable
  private final Pattern _pattern;

  public ParameterContraint(String name, boolean required, String regex) {
    this._name = Objects.requireNonNull(name);
    if (name.length() == 0) throw new IllegalArgumentException();
    this._required = required;
    if (regex != null && regex.length() > 0) {
      try {
        this._pattern = Pattern.compile(regex);
      } catch (PatternSyntaxException ex) {
        throw new IllegalArgumentException("Invalid pattern", ex);
      }
    } else {
      this._pattern = null;
    }
  }

  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    String value = req.getParameter(this._name);
    if (value == null) {
      if (this._required) return RequiredParameterConstraint.failedRequired(this._name, xml);
      return ContentStatus.OK;
    } else {
      Pattern p = this._pattern;
      if (p != null) {
        if (p.matcher(value).matches()) return ContentStatus.OK;
        else {
          xml.openElement("error");
          xml.attribute("type", "invalid-parameter");
          xml.attribute("parameter", this._name);
          xml.attribute("pattern", p.toString());
          xml.closeElement();
          return ContentStatus.BAD_REQUEST;
        }
      } else return ContentStatus.OK;
    }
  }

}
