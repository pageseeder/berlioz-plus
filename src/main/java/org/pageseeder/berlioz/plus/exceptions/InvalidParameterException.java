/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.exceptions;

import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.RequestParameter;

/**
 * Exception thrown when a parameter is either missing or invalid.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public final class InvalidParameterException extends RequestException {

  /** As per requirement for Serializable. */
  private static final long serialVersionUID = 2026_03_04L;

  /**
   * The name of the invalid parameter.
   */
  private final String parameter;

  public InvalidParameterException(String parameter) {
    super(ContentStatus.BAD_REQUEST, "Invalid parameter '"+parameter+"'");
    this.parameter = parameter;
  }

  public InvalidParameterException(RequestParameter parameter) {
    this(parameter.getName());
  }

  public InvalidParameterException(String parameter, Throwable cause) {
    super(ContentStatus.BAD_REQUEST, "Invalid parameter '"+parameter+"'", cause);
    this.parameter = parameter;
  }

  public InvalidParameterException(RequestParameter parameter, Throwable cause) {
    this(parameter.getName(), cause);
  }

  public String parameter() {
    return this.parameter;
  }
}
