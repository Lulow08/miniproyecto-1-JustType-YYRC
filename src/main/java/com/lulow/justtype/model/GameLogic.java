package com.lulow.justtype.model;

import com.lulow.justtype.model.validator.AnswerValidator;

/**
 * Core game logic. Tracks the current level, word, and score,
 * and delegates validation, word selection, and transformation.
 */
public class GameLogic {

    private final WordBank        wordBank        = new WordBank();
    private final WordSplitter    wordSplitter    = new WordSplitter();
    private final AnswerValidator answerValidator = new AnswerValidator();
    private final WordTransformer wordTransformer = new WordTransformer();

    /** Current level number (1-based). */
    private int    currentLevel      = 1;

    /** Total number of correctly answered levels. */
    private int    completedLevels   = 0;

    /** Seconds remaining when the last level ended. */
    private int    lastTimeRemaining = 0;

    /** The current word the player must type. */
    private String currentWord       = "";

    /** Character array of the current word. */
    private char[] currentChars;

    /**
     * Returns the characters of the current word.
     *
     * @return char array of the current word
     */
    public char[] getCurrentChars() { return currentChars; }

    /**
     * Returns the current word as a string.
     *
     * @return the current word
     */
    public String getCurrentWord() { return currentWord; }

    /**
     * Returns the current level number.
     *
     * @return current level
     */
    public int getCurrentLevel() { return currentLevel; }

    /**
     * Returns the number of levels completed so far.
     *
     * @return completed levels count
     */
    public int getCompletedLevels() { return completedLevels; }

    /**
     * Returns the time remaining recorded at the end of the last level.
     *
     * @return last recorded time remaining in seconds
     */
    public int getLastTimeRemaining() { return lastTimeRemaining; }

    /**
     * Returns whether the player has reached the win condition.
     *
     * @return {@code true} if the player has passed the final level
     */
    public boolean hasWon() { return currentLevel > LevelConfig.WIN_LEVEL; }

    /**
     * Validates the player's answer submitted via button or Enter.
     * Increments level and completed count on success.
     *
     * @param input the text typed by the player
     * @return {@code true} if the answer is correct
     */
    public boolean submitAnswer(String input) {
        if (answerValidator.validateAnswer(input, currentWord)) {
            currentLevel++;
            completedLevels++;
            return true;
        }
        return false;
    }

    /**
     * Validates the player's answer when the timer runs out.
     *
     * @param input the text typed by the player when time expired
     * @return {@code true} if the answer is correct
     */
    public boolean timeUpAnswer(String input) {
        if (answerValidator.validateAnswer(input, currentWord)) {
            currentLevel++;
            completedLevels++;
            return true;
        }
        return false;
    }

    /**
     * Records how many seconds were left when a level ended.
     *
     * @param seconds seconds remaining
     */
    public void recordTimeRemaining(int seconds) {
        lastTimeRemaining = seconds;
    }

    /**
     * Resets the game state to the initial values.
     */
    public void reset() {
        currentLevel      = 1;
        completedLevels   = 0;
        lastTimeRemaining = 0;
    }

    /**
     * Picks the next word from the word bank and applies any
     * tier-based transformations.
     */
    public void nextWord() {
        String raw   = wordBank.getRandomWord(currentLevel);
        currentWord  = applyTransform(raw);
        currentChars = wordSplitter.split(currentWord);
    }

    /**
     * Returns the time limit for the current level.
     *
     * @return max time in seconds
     */
    public int getMaxTimeForCurrentLevel() {
        return LevelConfig.getMaxTimeForLevel(currentLevel);
    }

    /**
     * Applies a casing transformation to the word if the current tier is EXPERT.
     *
     * @param word the raw word from the word bank
     * @return the transformed word, or the original if no transformation applies
     */
    private String applyTransform(String word) {
        if (LevelConfig.getTierForLevel(currentLevel) == LevelConfig.Tier.EXPERT) {
            return wordTransformer.applyTier5Casing(word);
        }
        return word;
    }
}