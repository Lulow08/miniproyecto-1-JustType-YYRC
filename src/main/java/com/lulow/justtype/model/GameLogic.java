package com.lulow.justtype.model;

import com.lulow.justtype.model.validator.AnswerValidator;

public class GameLogic {

    private final WordBank        wordBank        = new WordBank();
    private final WordSplitter    wordSplitter    = new WordSplitter();
    private final AnswerValidator answerValidator = new AnswerValidator();
    private final WordTransformer wordTransformer = new WordTransformer();

    private int    currentLevel      = 1;
    private int    completedLevels   = 0;
    private int    lastTimeRemaining = 0;
    private String currentWord       = "";
    private char[] currentChars;

    public char[]  getCurrentChars()      { return currentChars; }
    public String  getCurrentWord()       { return currentWord; }
    public int     getCurrentLevel()      { return currentLevel; }
    public int     getCompletedLevels()   { return completedLevels; }
    public int     getLastTimeRemaining() { return lastTimeRemaining; }
    public boolean hasWon()               { return currentLevel > LevelConfig.WIN_LEVEL; }

    public boolean submitAnswer(String input) {
        if (answerValidator.validateAnswer(input, currentWord)) {
            currentLevel++;
            completedLevels++;
            return true;
        }
        return false;
    }

    public boolean timeUpAnswer(String input) {
        if (answerValidator.validateAnswer(input, currentWord)) {
            currentLevel++;
            completedLevels++;
            return true;
        }
        return false;
    }

    public void recordTimeRemaining(int seconds) {
        lastTimeRemaining = seconds;
    }

    public void reset() {
        currentLevel      = 1;
        completedLevels   = 0;
        lastTimeRemaining = 0;
    }

    public void nextWord() {
        String raw  = wordBank.getRandomWord(currentLevel);
        currentWord = applyTransform(raw);
        currentChars = wordSplitter.split(currentWord);
    }

    public int getMaxTimeForCurrentLevel() {
        return LevelConfig.getMaxTimeForLevel(currentLevel);
    }

    private String applyTransform(String word) {
        if (LevelConfig.getTierForLevel(currentLevel) == LevelConfig.Tier.EXPERT) {
            return wordTransformer.applyTier5Casing(word);
        }
        return word;
    }
}