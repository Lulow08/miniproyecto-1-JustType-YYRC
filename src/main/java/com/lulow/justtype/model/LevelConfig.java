package com.lulow.justtype.model;

public final class LevelConfig {

    public static final int INITIAL_TIME          = 20;
    public static final int MIN_TIME              = 2;
    public static final int WIN_LEVEL             = 45;

    private static final int LEVELS_PER_REDUCTION = 5;
    private static final int TIME_REDUCTION       = 2;

    private LevelConfig() {}

    public enum Tier { BEGINNER, EASY, MEDIUM, HARD, EXPERT }

    public static Tier getTierForLevel(int level) {
        if (level <= 5)  return Tier.BEGINNER;
        if (level <= 10) return Tier.EASY;
        if (level <= 20) return Tier.MEDIUM;
        if (level <= 35) return Tier.HARD;
        return Tier.EXPERT;
    }

    public static int getMaxTimeForLevel(int level) {
        int reductions = (level - 1) / LEVELS_PER_REDUCTION;
        return Math.max(INITIAL_TIME - reductions * TIME_REDUCTION, MIN_TIME);
    }
}
