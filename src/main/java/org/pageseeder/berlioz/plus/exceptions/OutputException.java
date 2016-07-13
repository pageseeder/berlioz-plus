/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */

/**
 *
 */
package org.pageseeder.berlioz.plus.exceptions;

import org.pageseeder.berlioz.content.ContentStatus;

/**
 * Request exceptions caused while writing the output.
 */
public class OutputException extends RequestException {

  /** As per requirement for Serializable */
  private static final long serialVersionUID = 20160713L;

  /**
   * @param message
   */
  public OutputException(String message) {
    super(ContentStatus.INTERNAL_SERVER_ERROR, message);
  }

  /**
   * @param cause
   */
  public OutputException(Throwable cause) {
    super(ContentStatus.INTERNAL_SERVER_ERROR, cause);
  }

  /**
   * @param message
   * @param cause
   */
  public OutputException(String message, Throwable cause) {
    super(ContentStatus.INTERNAL_SERVER_ERROR, message, cause);
  }

}
