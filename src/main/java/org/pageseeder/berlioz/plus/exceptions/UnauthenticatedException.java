/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.exceptions;

import org.pageseeder.berlioz.content.ContentStatus;

/**
 * Exception thrown when a parameter is either missing or invalid.
 */
public final class UnauthenticatedException extends RequestException {

  /** As per requirement for Serializable. */
  private static final long serialVersionUID = 20160710L;

  public UnauthenticatedException() {
    super(ContentStatus.FORBIDDEN);
  }

  public UnauthenticatedException(String message) {
    super(ContentStatus.FORBIDDEN, message);
  }

}
