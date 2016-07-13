/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a parameter that must be castable as a long.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(LongParameters.class)
public @interface LongParameter {

  /**
   * @return the name of the parameter
   */
  String value();

  /**
   * @return true if the parameter is required (default is <code>true</code>).
   */
  boolean required() default true;

  /**
   * @return the inclusive minimum value for this parameter (default is Long.MIN_VALUE)
   */
  long min() default Long.MIN_VALUE;

  /**
   * @return the inclusive maximum value for this parameter (default is Long.MAX_VALUE)
   */
  long max() default Long.MAX_VALUE;

}
