package org.acme.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TitleValidator.class) // Specifica il validatore
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER}) // Dove può essere usata
@Retention(RetentionPolicy.RUNTIME) // Durata dell'annotazione
public @interface ValidTitle {
    String message() default "Il titolo non è valido."; // Messaggio di errore predefinito
    Class<?>[] groups() default {}; // Gruppi di validazione
    Class<? extends Payload>[] payload() default {}; // Carico utile
    int maxLength() default 255; // Lunghezza massima predefinita

}