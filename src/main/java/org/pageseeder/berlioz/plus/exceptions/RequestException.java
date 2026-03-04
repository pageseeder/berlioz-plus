/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.exceptions;

import java.util.Objects;

import org.pageseeder.berlioz.content.ContentStatus;

/**
 * Class of exception caused by bad requests such as missing or illegal
 * parameters.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public class RequestException extends RuntimeException {

  /** As per requirement for Serializable. */
  private static final long serialVersionUID = 2026_03_04L;

  /**
   * The content status to return if this exception is thrown.
   */
  private final ContentStatus status;

  /**
   * Create a new request exception with the specified content status.
   *
   * @param status the content to return if this exception is thrown
   */
  public RequestException(ContentStatus status) {
    this.status = Objects.requireNonNull(status);
  }

  /**
   * Create a new request exception with the specified content status.
   *
   * @param status the content to return if this exception is thrown
   * @param message explanation for this exception
   */
  public RequestException(ContentStatus status, String message) {
    super(message);
    this.status = Objects.requireNonNull(status);
  }

  /**
   * Create a new request exception with the specified content status.
   *
   * @param status the content to return if this exception is thrown
   * @param cause throwable that caused this exception
   */
  public RequestException(ContentStatus status, Throwable cause) {
    super(cause);
    this.status = Objects.requireNonNull(status);
  }

  /**
   * Create a new request exception with the specified content status.
   *
   * @param status the content to return if this exception is thrown
   * @param message explanation for this exception
   * @param cause throwable that caused this exception
   */
  public RequestException(ContentStatus status, String message, Throwable cause) {
    super(message, cause);
    this.status = Objects.requireNonNull(status);
  }

  /**
   * @return the content to return if this exception is thrown
   */
  public ContentStatus status() {
    return this.status;
  }
}
