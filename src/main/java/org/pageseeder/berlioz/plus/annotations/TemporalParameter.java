/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.temporal.Temporal;

/**
 *  Indicates a parameter that must be castable as a temporal class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(TemporalParameters.class)
public @interface TemporalParameter {

  /**
   * @return the name of the parameter
   */
  String value();

  /**
   * @return <code>true</code> if the parameter is required (default is <code>true</code>).
   */
  boolean required() default true;

  /**
   * @return The temporal class the parameter must be
   */
  Class<? extends Temporal> type() default LocalDate.class;

}
