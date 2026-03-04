package org.pageseeder.berlioz.plus.spi;

import java.lang.annotation.Annotation;

import org.pageseeder.berlioz.plus.constraints.Constraint;

/**
 * An annotation processor must provide a constraint implementation
 * for a generator based on an annotation.
 *
 * @author Christophe Lauret
 *
 * @since 0.5.0
 * @version 0.6.0
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
