/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.exceptions;

import org.pageseeder.berlioz.content.ContentStatus;

/**
 * This exception signifies a "Bad Gateway" HTTP status in the context of request handling.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public class GatewayException extends RequestException {

  /** As per requirement for Serializable */
  private static final long serialVersionUID = 2026_03_04L;

  /**
   * Constructs a new {@code GatewayException} with the specified detail message.
   *
   * @param message the detail message explaining the reason for the exception
   */
  public GatewayException(String message) {
    super(ContentStatus.BAD_GATEWAY, message);
  }

  /**
   * Constructs a new {@code GatewayException} with the specified content status and cause.
   *
   * @param status the content status representing the HTTP response code for this exception
   * @param cause the underlying cause of this exception
   */
  public GatewayException(ContentStatus status, Throwable cause) {
    super(ContentStatus.BAD_GATEWAY, cause);
  }

  /**
   * Constructs a new {@code GatewayException} with the specified detail message and cause.
   *
   * @param message the detail message explaining the reason for this exception
   * @param cause the underlying cause of this exception
   */
  public GatewayException(String message, Throwable cause) {
    super(ContentStatus.BAD_GATEWAY, message, cause);
  }

}
