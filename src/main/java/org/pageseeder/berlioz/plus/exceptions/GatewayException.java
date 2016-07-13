/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */

/**
 *
 */
package org.pageseeder.berlioz.plus.exceptions;

import org.pageseeder.berlioz.content.ContentStatus;

/**
 */
public class GatewayException extends RequestException {

  /** As per requirement for Serializable */
  private static final long serialVersionUID = 20160713L;

  /**
   * @param message
   */
  public GatewayException(String message) {
    super(ContentStatus.BAD_GATEWAY, message);
  }

  /**
   * @param cause
   */
  public GatewayException(ContentStatus status, Throwable cause) {
    super(ContentStatus.BAD_GATEWAY, cause);
  }

  /**
   * @param message
   * @param cause
   */
  public GatewayException(String message, Throwable cause) {
    super(ContentStatus.BAD_GATEWAY, message, cause);
  }

}
