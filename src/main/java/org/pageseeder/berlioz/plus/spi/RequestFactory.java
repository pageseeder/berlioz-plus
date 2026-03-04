package org.pageseeder.berlioz.plus.spi;

import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.plus.ValidatedRequest;

/**
 * Factory for creating validated requests based on content requests.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public interface RequestFactory<R extends ValidatedRequest> {

  /**
   * Generates a validated request from the provided content request.
   *
   * @param request The content request to be transformed into a validated request.
   * @return A validated request derived from the given content request.
   */
  R getRequest(ContentRequest request);

}
