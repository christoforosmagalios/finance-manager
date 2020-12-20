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
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueUsernameValidator.class})
public @interface UniqueUsername {

  String message() default AppConstants.USERNAME_EXISTS;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
