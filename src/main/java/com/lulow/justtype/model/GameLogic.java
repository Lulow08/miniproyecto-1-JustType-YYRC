package com.lulow.justtype.model;

import com.lulow.justtype.model.validator.AnswerValidator;

public class GameLogic {

    private AnswerValidator answerValidator = new AnswerValidator();

    public void processAnswer(String input) {
        answerValidator.validateAnswer(input); // TODO: turn into boolean type

        // Handle logic depending on validation
    }
}
