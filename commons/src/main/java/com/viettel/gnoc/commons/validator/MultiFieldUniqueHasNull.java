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
@Constraint(validatedBy = MultiFieldUniqueHasNullValidator.class)
public @interface MultiFieldUniqueHasNull {

  String message() default "{common.unique}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class clazz();

  String uniqueFields() default "";

  String idField() default "";

}
