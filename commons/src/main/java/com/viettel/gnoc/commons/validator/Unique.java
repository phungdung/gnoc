package com.viettel.gnoc.commons.validator;


import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target({TYPE})
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

  String message() default "{odtype.code.unique}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class clazz();

  String uniqueField() default "";

  String idField() default "";
}
