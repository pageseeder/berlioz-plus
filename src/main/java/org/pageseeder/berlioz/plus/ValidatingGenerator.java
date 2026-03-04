package org.pageseeder.berlioz.plus;

import org.pageseeder.berlioz.plus.spi.RequestFactory;

/**
 * Base class for all generators in this application.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public abstract class ValidatingGenerator extends Generator<ValidatedRequest> {

  private static final RequestFactory<ValidatedRequest> REQUEST_FACTORY = new BuiltinFactory();

  /**
   * Constructs a new instance of the {@code ValidatingGenerator}.
   *
   * <p>This constructor initializes the generator with a predefined request factory
   * specifically designed to handle {@link ValidatedRequest} instances. The associated
   * factory ensures proper creation and handling of validated requests during the
   * content generation process.
   */
  protected ValidatingGenerator() {
    super(REQUEST_FACTORY);
  }

}
