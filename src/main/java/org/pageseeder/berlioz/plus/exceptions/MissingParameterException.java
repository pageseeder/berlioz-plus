/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.exceptions;

import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.RequestParameter;

/**
 * Exception thrown when a parameter is either missing.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public final class MissingParameterException extends RequestException {

  /** As per requirement for Serializable. */
  private static final long serialVersionUID = 2026_03_04L;

  /**
   * The name of the missing parameter.
   */
  private final String parameter;

  public MissingParameterException(String parameter) {
    super(ContentStatus.BAD_REQUEST, "Missing parameter '"+parameter+"'");
    this.parameter = parameter;
  }

  public MissingParameterException(RequestParameter parameter) {
    this(parameter.getName());
  }

  public String parameter() {
    return this.parameter;
  }
}
