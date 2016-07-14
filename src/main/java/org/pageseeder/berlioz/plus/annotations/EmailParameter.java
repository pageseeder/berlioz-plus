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
 * Indicates a parameter that must be a valid email
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(EmailParameters.class)
@Documented
public @interface EmailParameter {

  /**
   * @return the name of the parameter
   */
  String value();

  /**
   * @return true if the parameter is required (default is <code>true</code>).
   */
  boolean required() default true;

}
