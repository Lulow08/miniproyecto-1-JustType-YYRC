package com.lulow.justtype.model;

/**
 * Configuration constants and utility methods for game levels.
 * Defines timing rules, difficulty tiers, and win condition.
 */
public final class LevelConfig {

    /** Starting time in seconds for the first set of levels. */
    public static final int INITIAL_TIME           = 20;

    /** Minimum time in seconds a level can have. */
    public static final int MIN_TIME               = 2;

    /** The level number that triggers the win screen when surpassed. */
    public static final int WIN_LEVEL              = 45;

    /** How many levels must be completed before the timer decreases. */
    private static final int LEVELS_PER_REDUCTION  = 5;

    /** Seconds removed from the timer every {@code LEVELS_PER_REDUCTION} levels. */
    private static final int TIME_REDUCTION        = 2;

    /** Prevents instantiation. */
    private LevelConfig() {}

    /**
     * Difficulty tiers used to select word sets and apply visual effects.
     */
    public enum Tier { BEGINNER, EASY, MEDIUM, HARD, EXPERT }

    /**
     * Returns the difficulty tier for a given level.
     *
     * @param level the current level number
     * @return the corresponding {@link Tier}
     */
    public static Tier getTierForLevel(int level) {
        if (level <= 5)  return Tier.BEGINNER;
        if (level <= 10) return Tier.EASY;
        if (level <= 20) return Tier.MEDIUM;
        if (level <= 35) return Tier.HARD;
        return Tier.EXPERT;
    }

    /**
     * Calculates the time limit for a given level.
     * The time decreases by {@value #TIME_REDUCTION}s every {@value #LEVELS_PER_REDUCTION} levels,
     * down to a minimum of {@value #MIN_TIME}s.
     *
     * @param level the current level number
     * @return time limit in seconds
     */
    public static int getMaxTimeForLevel(int level) {
        int reductions = (level - 1) / LEVELS_PER_REDUCTION;
        return Math.max(INITIAL_TIME - reductions * TIME_REDUCTION, MIN_TIME);
    }
}