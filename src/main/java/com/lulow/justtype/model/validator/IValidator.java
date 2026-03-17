package com.lulow.justtype.model.validator;

/**
 * Contract for answer validation.
 */
public interface IValidator {

    /**
     * Validates the player's answer against the expected word.
     *
     * @param answer   the player's input
     * @param expected the correct word
     * @return {@code true} if they match
     */
    boolean validateAnswer(String answer, String expected);
}