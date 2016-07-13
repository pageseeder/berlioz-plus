/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.constraints;

import java.io.IOException;

import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.XMLPrinter;

/**
 * Defines a constraint on the request.
 *
 * @author Christophe Lauret
 */
public interface Constraint {

  /**
   * Validates the content request.
   *
   * @param req The content request to validate.
   * @param xml The XML to write the content to
   *
   * @return <code>true</code> if the request was considered valid;
   *         <code>false</code> otherwise in that case the generator should end.
   *
   * @throws IOException If an error occurs while writing the XML output.
   */
  ContentStatus validate(ContentRequest req, XMLPrinter xml);

}
