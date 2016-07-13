/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.exceptions;

import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.RequestParameter;

/**
 * Exception thrown when a parameter is either missing or invalid.
 */
public final class MissingParameterException extends RequestException {

  /** As per requirement for Serializable. */
  private static final long serialVersionUID = 20160710L;

  /**
   * The name of the missing parameter.
   */
  private final String _parameter;

  public MissingParameterException(String parameter) {
    super(ContentStatus.BAD_REQUEST, "Missing parameter '"+parameter+"'");
    this._parameter = parameter;
  }

  public MissingParameterException(RequestParameter parameter) {
    this(parameter.getName());
  }

  public String parameter() {
    return this._parameter;
  }
}
