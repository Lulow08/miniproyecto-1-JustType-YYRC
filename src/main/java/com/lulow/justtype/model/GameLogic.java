package com.lulow.justtype.model;

import com.lulow.justtype.model.validator.AnswerValidator;

public class GameLogic {

    private final WordBank        wordBank        = new WordBank();
    private final WordSplitter    wordSplitter    = new WordSplitter();
    private final AnswerValidator answerValidator = new AnswerValidator();

    private int    currentLevel = 1;
    private String currentWord  = "";
    private char[] currentChars;

    public char[] getCurrentChars() { return currentChars; }
    public String getCurrentWord()  { return currentWord; }
    public int    getCurrentLevel() { return currentLevel; }

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

    public int getMaxTimeForCurrentLevel() {
        return LevelConfig.getMaxTimeForLevel(currentLevel);
    }
}
