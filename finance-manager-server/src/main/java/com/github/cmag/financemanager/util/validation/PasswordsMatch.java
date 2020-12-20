package com.github.cmag.financemanager.util.validation;

import com.github.cmag.financemanager.config.AppConstants;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PasswordsMatchValidator.class})
public @interface PasswordsMatch {

  String message() default AppConstants.PASSWORDS_DON_NOT_MATCH;
  String field() default "password";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
