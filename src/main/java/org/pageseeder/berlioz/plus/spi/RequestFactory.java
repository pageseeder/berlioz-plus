package org.pageseeder.berlioz.plus.spi;

import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.plus.ValidatedRequest;

/**
 * An annotation processor must provide a constraint implementation
 * for a generator based on an annotation.
 *
 * @author Christophe Lauret
 */
public interface RequestFactory<R extends ValidatedRequest> {

  /**
   * Returns the request constraint corresponding to the specified annotation.
   *
   * @param annotation to process
   *
   * @return a constraint of <code>null</code>.
   */
  R getRequest(ContentRequest request);

}
