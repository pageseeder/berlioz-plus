/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus;

import java.io.IOException;

import org.pageseeder.berlioz.BerliozException;
import org.pageseeder.berlioz.content.ContentGenerator;
import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.exceptions.RequestException;
import org.pageseeder.berlioz.plus.spi.RequestFactory;
import org.pageseeder.xmlwriter.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the generator from the content generator
 *
 * @param <R> A validated request
 */
public abstract class Generator<R extends ValidatedRequest> implements ContentGenerator {

  /**
   * Validator for this generator (instantiated from annotations)
   */
  private final RequestValidator _validator;

  private final RequestFactory<R> _factory;

  /**
   * Construct a new instance.
   */
  public Generator(RequestFactory<R> factory) {
    this._validator = RequestValidator.create(this.getClass());
    this._factory = factory;
  }

  @Override
  public final void process(ContentRequest req, XMLWriter xml) throws BerliozException, IOException {

    ContentStatus status = ContentStatus.OK;
    XMLPrinter appender = new XMLPrinter(xml);

    // Validate first
    status = this._validator.validate(req, appender);

    // Continue if request is valid
    if (status == ContentStatus.OK) {
      try {
        R r = this._factory.getRequest(req);
        status = generate(r, new XMLPrinter(xml));
      } catch (RequestException ex) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.error("Caught request exception: {}", ex.getMessage(), ex);
        req.setStatus(ex.status());
        return;
      }
    }

    // Ensure the XML is flushed out
    xml.flush();

    // Do not try to set the status when redirects
    if (status != ContentStatus.TEMPORARY_REDIRECT && status != ContentStatus.SEE_OTHER) {
      req.setStatus(status);
    }
  }

  /**
   * Generate the content
   *
   * @param req The request to process.
   * @param xml The XML output.
   *
   * @return the status of the content.
   *
   * @throws RequestException If an error occurred processing the request.
   */
  public abstract ContentStatus generate(R req, XMLPrinter xml);

}
