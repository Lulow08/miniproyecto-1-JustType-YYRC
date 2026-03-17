package com.lulow.justtype.model.validator;

/**
 * Validates the player's answer using an exact string comparison.
 * Case, spaces, and punctuation must all match.
 */
public class AnswerValidator extends ValidatorAdapter {

    /**
     * Returns {@code true} only if the answer exactly equals the expected word.
     *
     * @param answer   the text the player typed
     * @param expected the correct word
     * @return {@code true} if they are identical
     */
    @Override
    public boolean validateAnswer(String answer, String expected) {
        return answer != null && answer.equals(expected);
    }
}