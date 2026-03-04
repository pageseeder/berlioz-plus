/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.plus.constraints.Constraint;
import org.pageseeder.berlioz.plus.constraints.EmailParameterConstraint;
import org.pageseeder.berlioz.plus.constraints.ParameterConstraint;
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
 *
 * @since 0.5.0
 * @version 0.6.0
 */
public final class RequestValidator {

  /** Logger for the request validator */
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestValidator.class);

  /** List of constraints to obey */
  private final List<Constraint> constraints = new ArrayList<>(4);

  /**
   * Creates a new validator.
   */
  public RequestValidator() {
  }

  /**
   * Creates a new validator from a single constraint.
   *
   * @param constraint a constraint to obey for a request to be valid.
   */
  public RequestValidator(Constraint constraint) {
    this.constraints.add(constraint);
  }

  /**
   * Creates a new validator from a list of constraints.
   *
   * @param constraints a list of constraints to obey for a request to be valid.
   */
  public RequestValidator(List<Constraint> constraints) {
    this.constraints.addAll(constraints);
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
   * Creates a new {@code RequestValidator} instance using the annotations present on the specified class.
   *
   * <p>This method dynamically processes all annotations of the provided class to generate optional constraints
   * that are added to the returned {@code RequestValidator}.
   *
   * @param clazz The class from which annotations should be processed to configure the validator.
   *              Must extend {@code Generator}.
   * @return A new {@code RequestValidator} instance populated with constraints derived from the class annotations.
   */
  public static RequestValidator create(Class<? extends Generator> clazz) {
    LOGGER.debug("Building validator for {}", clazz.getName());
    RequestValidator validator = new RequestValidator();
    Annotation[] annotations = clazz.getAnnotations();
    for (Annotation annotation : annotations) {
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
   */
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    for (Constraint c : this.constraints) {
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
      this.constraints.add(new RequiredParameterConstraint(name));
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
      this.constraints.add(new RequiredParameterConstraint(name.getName()));
    }
    return this;
  }

  /**
   * Method to create a new validator that requires authentication.
   *
   * @param name The name of the parameter to check.
   * @param regex The regular expression to match.
   *
   * @return This validator instance.
   */
  public RequestValidator matches(String name, String regex) {
    this.constraints.add(new ParameterConstraint(name, false, regex));
    return this;
  }

  /**
   * Method to create a new validator that checks that a specified parameter is a valid email address.
   *
   * @param name The name of the parameter to check.
   *
   * @return This validator instance.
   */
  public RequestValidator isEmail(String name) {
    this.constraints.add(new EmailParameterConstraint(name, false));
    return this;
  }

  /**
   * Method to create a new validator that requires authentication.
   *
   * @param kindOfConstraint The type of constraint to add.
   *
   * @return A new request validator instance.
   */
  public RequestValidator with(Class<? extends Constraint> kindOfConstraint) {
    List<Constraint> constraintList = new ArrayList<>(this.constraints);
    Constraint constraint;
    try {
      constraint = kindOfConstraint.getDeclaredConstructor().newInstance();
      constraintList.add(constraint);
    } catch (ReflectiveOperationException ex) {
      throw new IllegalArgumentException("Unable to instantiate constraint: " + kindOfConstraint.getName(), ex);
    }
    return new RequestValidator(constraintList);
  }

  /**
   * Updates the validator with an annotation, adding an optional constraint if applicable.
   *
   * @param annotation The annotation to be processed and used for generating a constraint.
   * @return The updated request validator instance.
   */
  public RequestValidator update(Annotation annotation) {
    addOptionalConstraint(annotation);
    return this;
  }

  /**
   * Adds an optional constraint to the validator based on the provided annotation.
   *
   * <p>This method uses all available {@code AnnotationProcessor} implementations
   * to process the annotation. If a processor accepts the annotation, it generates
   * a corresponding constraint and adds it to the validator's internal list of constraints.
   *
   * @param annotation The annotation to be processed and used for generating a constraint.
   */
  private void addOptionalConstraint(Annotation annotation) {
    ServiceLoader<AnnotationProcessor> loader = ServiceLoader.load(AnnotationProcessor.class);
    for (AnnotationProcessor processor : loader) {
      LOGGER.debug("{}/{}->{}", processor.getClass(), annotation, processor.accepts(annotation));
      if (processor.accepts(annotation)) {
        Constraint constraint = processor.getConstraint(annotation);
        this.constraints.add(constraint);
      }
    }
  }

}