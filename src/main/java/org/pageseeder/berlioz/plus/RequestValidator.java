/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.eclipse.jdt.annotation.NonNull;
import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.constraints.Constraint;
import org.pageseeder.berlioz.plus.constraints.EmailParameterConstraint;
import org.pageseeder.berlioz.plus.constraints.ParameterContraint;
import org.pageseeder.berlioz.plus.constraints.RequiredParameterConstraint;
import org.pageseeder.berlioz.plus.spi.AnnotationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simplifies the checking of parameters for content generators.
 *
 * <p>It can:
 * <ul>
 *   <li>check that all required parameters are available on the request
 *   <li>validate that parameters match a specific rule
 * </ul>
 *
 * @author Christophe Lauret
 */
public final class RequestValidator {

  /** Logger for the request validator */
  private final static Logger LOGGER = LoggerFactory.getLogger(RequestValidator.class);

  /** List of constraints to obey */
  private final List<Constraint> _constraints = new ArrayList<>(4);

  /**
   * Creates a new validator.
   */
  public RequestValidator() {
  }

  /**
   * Creates a new validator from single constraint.
   *
   * @param constraint a constraint to obey for a request to be valid.
   */
  public RequestValidator(Constraint constraint) {
    this._constraints.add(constraint);
  }

  /**
   * Creates a new validator from a list of constraints.
   *
   * @param constraints a list of constraints to obey for a request to be valid.
   */
  public RequestValidator(List<Constraint> constraints) {
    this._constraints.addAll(constraints);
  }

  /**
   * Factory method for easy chaining.
   *
   * @return a new request validator with no constraint.
   */
  public static RequestValidator none() {
    return new RequestValidator();
  }

  /**
   * Factory method for easy chaining.
   *
   * @return a new request validator with no constraint.
   */
  public static RequestValidator create() {
    return new RequestValidator();
  }

  /**
   *
   */
  public static RequestValidator create(Class<? extends Generator> clazz) {
    LOGGER.debug("Building validator for {}", clazz.getName());
    RequestValidator validator = new RequestValidator();
    @NonNull Annotation[] annotations = clazz.getAnnotations();
    for(Annotation annotation : annotations) {
      validator.addOptionalConstraint(annotation);
    }
    return validator;
  }

  /**
   * Validate the content request against the constraints defined in this class.
   *
   * <p>If this method returns a <code>ContentStatus</code> different from <code>OK</code>,
   * the generator should return as this method would have set the response content and headers.
   *
   * @param req The content request to validate
   * @param xml The XML writer to use should an error be reported.
   *
   * @return the content status of the request.
   *
   * @throws IOException If an error occurred while writing the XML.
   */
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    for (Constraint c : this._constraints) {
      ContentStatus status = c.validate(req, xml);
      if (status != ContentStatus.OK) return status;
    }
    return ContentStatus.OK;
  }

  // Built-in builders
  // ---------------------------------------------------------------------------------------

  /**
   * Method to create a new validator from an array of required parameters names.
   *
   * @param required the names of the required parameters.
   *
   * @return This validator instance.
   */
  public RequestValidator requires(String... required) {
    for (String name : required) {
      if (name != null) {
        this._constraints.add(new RequiredParameterConstraint(name));
      }
    }
    return this;
  }

  /**
   * Method to create a new validator from an array of required parameters names.
   *
   * @param required the names of the required parameters.
   *
   * @return This validator instance.
   */
  public RequestValidator requires(RequestParameter... required) {
    for (RequestParameter name : required) {
      this._constraints.add(new RequiredParameterConstraint(name.getName()));
    }
    return this;
  }

  /**
   * Method to create a new validator that requires authentication.
   *
   * @return This validator instance.
   */
  public RequestValidator matches(String name, String regex) {
    this._constraints.add(new ParameterContraint(name, false, regex));
    return this;
  }

  /**
   * Method to create a new validator that checks that a specified parameter is a valid email address.
   *
   * @return This validator instance.
   */
  public RequestValidator isEmail(String name) {
    this._constraints.add(new EmailParameterConstraint(name, false));
    return this;
  }

  /**
   * Method to create a new validator that requires authentication.
   *
   * @return A new request validator instance.
   */
  public RequestValidator with(Class<? extends Constraint> kindOfConstraint) {
    List<Constraint> constraints = new ArrayList<>(this._constraints);
    Constraint constraint;
    try {
      constraint = kindOfConstraint.newInstance();
      constraints.add(constraint);
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new IllegalArgumentException(ex);
    }
    return new RequestValidator(constraints);
  }

  public RequestValidator update(Annotation annotation) {
    addOptionalConstraint(annotation);
    return this;
  }

  private void addOptionalConstraint(Annotation annotation) {
    ServiceLoader<AnnotationProcessor> loader = ServiceLoader.load(AnnotationProcessor.class);
    Iterator<AnnotationProcessor> it = loader.iterator();
    while (it.hasNext()) {
      AnnotationProcessor processor = it.next();
      LOGGER.debug(processor.getClass()+"/"+annotation.toString()+"->"+processor.accepts(annotation));
      if (processor.accepts(annotation)) {
        Constraint constraint = processor.getConstraint(annotation);
        this._constraints.add(constraint);
      }
    }
  }

}