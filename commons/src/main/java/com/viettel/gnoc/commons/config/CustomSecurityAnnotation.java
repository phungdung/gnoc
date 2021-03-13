package com.viettel.gnoc.commons.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomSecurityAnnotation {

  String target() default "";

  String[] permission() default {};

  String[] modulePermission() default {};
}
