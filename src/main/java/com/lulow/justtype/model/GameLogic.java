package com.lulow.justtype.model;

import com.lulow.justtype.model.validator.AnswerValidator;

public class GameLogic {

    public static final int INITIAL_TIME = 20;
    public static final int MIN_TIME = 2;
    private static final int LEVELS_PER_REDUCTION = 5;
    private static final int TIME_REDUCTION = 2;

    private final WordBank wordBank = new WordBank();
    private final WordSplitter wordSplitter = new WordSplitter();
    private final AnswerValidator answerValidator = new AnswerValidator();

    private int currentLevel = 1;
    private String currentWord = "";
    private char[] currentChars;

    public char[] getCurrentChars() { return currentChars; }
    public String getCurrentWord() { return currentWord; }
    public int getCurrentLevel() { return currentLevel; }

    public void nextWord() {
        currentWord = wordBank.getRandomWord(currentLevel);
        currentChars = wordSplitter.split(currentWord);
    }

    public boolean processAnswer(String input) {
        return answerValidator.validateAnswer(input, currentWord);
    }

    public void levelUp() {
        currentLevel++;
    }

    public void reset() {
        currentLevel = 1;
    }

    public int getMaxTimeForCurrentLevel() {
        int reductions = (currentLevel - 1) / LEVELS_PER_REDUCTION;
        int time = INITIAL_TIME - (reductions * TIME_REDUCTION);
        return Math.max(time, MIN_TIME);
    }
}
