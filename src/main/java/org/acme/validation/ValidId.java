package org.acme.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IdValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD}) // Può essere utilizzata su parametri e campi
@Retention(RetentionPolicy.RUNTIME) // Ritenzione a runtime
public @interface ValidId {
    String message() default "ID must be positive and a number!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
