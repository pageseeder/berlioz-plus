package org.pageseeder.berlioz.plus;

import java.lang.annotation.Annotation;
import java.time.temporal.Temporal;

import org.pageseeder.berlioz.plus.annotations.EmailParameter;
import org.pageseeder.berlioz.plus.annotations.LongParameter;
import org.pageseeder.berlioz.plus.annotations.Parameter;
import org.pageseeder.berlioz.plus.annotations.TemporalParameter;
import org.pageseeder.berlioz.plus.constraints.Constraint;
import org.pageseeder.berlioz.plus.constraints.EmailParameterConstraint;
import org.pageseeder.berlioz.plus.constraints.LongParameterConstraint;
import org.pageseeder.berlioz.plus.constraints.ParameterContraint;
import org.pageseeder.berlioz.plus.constraints.TemporalParameterConstraint;
import org.pageseeder.berlioz.plus.spi.AnnotationProcessor;

/**
 * BerliozPlus built-in annotation processor
 *
 * @author Christophe Lauret
 */
public final class BuiltinProcessor implements AnnotationProcessor {

  @Override
  public boolean accepts(Annotation annotation) {
    return (annotation instanceof Parameter
         || annotation instanceof LongParameter
         || annotation instanceof EmailParameter
         || annotation instanceof TemporalParameter);
  }

  @Override
  public Constraint getConstraint(Annotation annotation) {
    if (annotation instanceof Parameter) {
      Parameter parameter = (Parameter)annotation;
      String name = parameter.value();
      boolean required = parameter.required();
      String regex = parameter.matches();
      return new ParameterContraint(name, required, regex);
    } else if (annotation instanceof LongParameter) {
      LongParameter parameter = (LongParameter)annotation;
      String name = parameter.value();
      boolean required = parameter.required();
      long min = parameter.min();
      long max = parameter.max();
      return new LongParameterConstraint(name, required, min, max);
    } else if (annotation instanceof EmailParameter) {
      EmailParameter parameter = (EmailParameter)annotation;
      String name = parameter.value();
      boolean required = parameter.required();
      return new EmailParameterConstraint(name, required);
    } else if (annotation instanceof TemporalParameter) {
      TemporalParameter parameter = (TemporalParameter)annotation;
      String name = parameter.value();
      boolean required = parameter.required();
      Class<? extends Temporal> type = parameter.type();
      return new TemporalParameterConstraint(name, required, type);
    } else throw new IllegalArgumentException();
  }

}
