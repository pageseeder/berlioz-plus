/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a required parameter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Parameters.class)
@Documented
public @interface Parameter {

  /**
   * @return the name of the parameter
   */
  String value();

  /**
   * @return <code>true</code> if the parameter is required (default is <code>true</code>).
   */
  boolean required() default true;

  /**
   * @return a regular expression pattern the parameter must match
   */
  String matches() default "";

}
