/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.exceptions;

import org.pageseeder.berlioz.content.ContentStatus;

/**
 * Request exceptions caused while writing the output.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public class OutputException extends RequestException {

  /** As per requirement for Serializable */
  private static final long serialVersionUID = 2026_03_04L;

  /**
   * Constructs a new {@code OutputException} with the specified detail message.
   *
   * @param message the detail message explaining the reason for the exception
   */
  public OutputException(String message) {
    super(ContentStatus.INTERNAL_SERVER_ERROR, message);
  }

  /**
   * Constructs a new {@code OutputException} with the specified underlying cause.
   *
   * @param cause the underlying cause of this exception
   */
  public OutputException(Throwable cause) {
    super(ContentStatus.INTERNAL_SERVER_ERROR, cause);
  }

  /**
   * Constructs a new {@code OutputException} with the specified detail message and underlying cause.
   *
   * @param message the detail message explaining the reason for the exception
   * @param cause the underlying cause of this exception
   */
  public OutputException(String message, Throwable cause) {
    super(ContentStatus.INTERNAL_SERVER_ERROR, message, cause);
  }

}
