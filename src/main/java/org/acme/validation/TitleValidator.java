package org.acme.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<ValidTitle,String> {

    private int maxLength ;
    @Override
    public void initialize(ValidTitle constraintAnnotation) {
        this.maxLength  = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {

        boolean isValid = title != null && !title.trim().isEmpty() && title.length() <= this.maxLength;

        if(!isValid){
            context.disableDefaultConstraintViolation();//disabilita il default message e la stringa sotto ne costruisce uno personalizzato.
            context.buildConstraintViolationWithTemplate(getErrorMessage(title))
                    .addConstraintViolation();
        }
        return isValid;
    }

    private String getErrorMessage(String title){
        if(title == null )
                return "Title cannot be null";
        if(title.trim().isEmpty())
                return "Title cannot be empty";
        if(title.length() > this.maxLength)
                return "Title is too long, must be between 1 and 40 characters";

        return "Title is not valid";
    }
}