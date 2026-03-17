package com.lulow.justtype.model.validator;

/**
 * Default no-op adapter for {@link IValidator}.
 * Always returns {@code false}; subclasses override as needed.
 */
public abstract class ValidatorAdapter implements IValidator {
    @Override
    public boolean validateAnswer(String answer, String expected) {
        return false;
    }
}