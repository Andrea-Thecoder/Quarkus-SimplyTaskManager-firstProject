package org.acme.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class IdValidator  implements  ConstraintValidator<ValidId, Long>{
    @Override
    public void initialize(ValidId constraintAnnotation) {
        // Inizializzazione se necessaria
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null || id <= 0){
          context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("ID must be positive and a number!").addConstraintViolation();
            return false;
        }

        return true;
    }
}
