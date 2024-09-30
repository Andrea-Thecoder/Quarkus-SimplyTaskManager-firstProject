package org.acme.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.acme.utils.HtmlSanitizerUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = HtmlSanitizerUtil.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SanitizeHtmlAnnotation {
    String message() default "Invalid HTML content"; // Aggiungi un messaggio di default
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
