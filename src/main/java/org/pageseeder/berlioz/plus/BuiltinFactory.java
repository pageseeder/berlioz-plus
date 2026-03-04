package org.pageseeder.berlioz.plus;

import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.plus.spi.RequestFactory;

/**
 * BerliozPlus built-in annotation processor
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public final class BuiltinFactory implements RequestFactory<ValidatedRequest> {

  @Override
  public ValidatedRequest getRequest(ContentRequest request) {
    return new ValidatedRequest(request);
  }

}
