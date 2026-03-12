package com.lulow.justtype.model;

public class LevelConfig {

    public static final int INITIAL_TIME         = 20;
    public static final int MIN_TIME             = 2;
    private static final int LEVELS_PER_REDUCTION = 5;
    private static final int TIME_REDUCTION       = 2;

    private LevelConfig() {}

    public enum Tier { T1, T2, T3, T4, T5 }

    public static Tier getTierForLevel(int level) {
        if (level <= 5)  return Tier.T1;
        if (level <= 10) return Tier.T2;
        if (level <= 20) return Tier.T3;
        if (level <= 35) return Tier.T4;
        return Tier.T5;
    }

    public static int getMaxTimeForLevel(int level) {
        int levelsPassed = (level - 1) / LEVELS_PER_REDUCTION;
        int time = INITIAL_TIME - (levelsPassed * TIME_REDUCTION);
        return Math.max(time, MIN_TIME);
    }
}