package org.pageseeder.berlioz.plus;

import org.pageseeder.berlioz.plus.spi.RequestFactory;

/**
 * Base class for all generators in this application.
 */
public abstract class ValidatingGenerator extends Generator<ValidatedRequest> {

  private static final RequestFactory<ValidatedRequest> REQUEST_FACTORY = new BuiltinFactory();

  public ValidatingGenerator() {
    super(REQUEST_FACTORY);
  }

}
