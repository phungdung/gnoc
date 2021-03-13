package com.viettel.gnoc.commons.validator;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SizeByteValidator.class)
public @interface SizeByte {

  String message() default "{javax.validation.constraints.Size.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int max() default 2147483647;

  int min() default 0;
}
