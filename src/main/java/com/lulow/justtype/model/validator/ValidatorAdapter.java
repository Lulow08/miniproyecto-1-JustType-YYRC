package com.lulow.justtype.model.validator;

public abstract class ValidatorAdapter implements IValidator {
    @Override
    public boolean validateAnswer(String answer, String expected) {
        return false;
    }
}