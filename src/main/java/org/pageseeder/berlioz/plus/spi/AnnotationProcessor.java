package org.pageseeder.berlioz.plus.spi;

import java.lang.annotation.Annotation;

import org.pageseeder.berlioz.plus.constraints.Constraint;

/**
 * An annotation processor must provide a constraint implementation
 * for a generator based on an annotation.
 *
 * @author Christophe Lauret
 */
public interface AnnotationProcessor {

  /**
   * Returns the request constraint corresponding to the specified annotation.
   *
   * @param annotation to process
   *
   * @return a constraint of <code>null</code>.
   */
  boolean accepts(Annotation annotation);

  /**
   * Returns the request constraint corresponding to the specified annotation.
   *
   * @param annotation to process
   *
   * @return a constraint of <code>null</code>.
   */
  Constraint getConstraint(Annotation annotation);

}
