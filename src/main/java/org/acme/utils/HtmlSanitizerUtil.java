package org.acme.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.acme.annotation.SanitizeHtmlAnnotation;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlSanitizerUtil implements ConstraintValidator<SanitizeHtmlAnnotation, String> {

    // Definisci la politica di sanitizzazione
    private static final PolicyFactory policy = new HtmlPolicyBuilder()
            .allowElements("b", "i") // Tag consentiti
            .allowUrlProtocols("http", "https") // Consenti protocolli URL
            .toFactory();

    @Override
    public void initialize(SanitizeHtmlAnnotation constraintAnnotation) {
        // Inizializzazione se necessaria
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Valori nulli sono considerati validi
        }

        String sanitized = policy.sanitize(value);
        System.out.println(sanitized);

        // Controlla se il contenuto sanitizzato è diverso dall'originale
        if (!sanitized.equals(value)) {
            return true; // Il contenuto è stato sanitizzato correttamente
        } else {
            // Se non ci sono stati cambiamenti, significa che non è valido
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("HTML non valido").addConstraintViolation();
            return false; // Il contenuto originale era già valido
        }
    }
}