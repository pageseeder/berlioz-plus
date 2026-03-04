/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.exceptions;

import org.pageseeder.berlioz.content.ContentStatus;

/**
 * Exception thrown authentication or when access is forbidden is required.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public final class UnauthenticatedException extends RequestException {

  /** As per requirement for Serializable. */
  private static final long serialVersionUID = 2026_03_04L;

  /**
   * Creates a new exception.
   */
  public UnauthenticatedException() {
    super(ContentStatus.FORBIDDEN);
  }

  /**
   * Creates a new exception with the specified message.
   *
   * @param message The message to display.
   */
  public UnauthenticatedException(String message) {
    super(ContentStatus.FORBIDDEN, message);
  }

}
