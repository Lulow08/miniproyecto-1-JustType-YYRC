package com.lulow.justtype.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordBank {

    private static final String[] TIER_1 = {
            "class", "git", "run", "bool", "IDEA",
            "star", "java", "fast", "game", "cast",
            "Type", "tree", "zip", "model", "word",
            "bug", "View", "main", "virus", "root"
    };

    private static final String[] TIER_2 = {
            "integer", "commit", "Controller", "buffer", "Syntax",
            "module", "compile", "pointer", "interface", "console",
            "binary", "directory", "Source", "PACKAGE", "virtual",
            "Lambda", "String", "TERMINAL", "deploy", "Library"
    };

    private static final String[] TIER_3 = {
            "framework", "Algorithm", "database", "endpoint", "CallBack",
            "recursion", "INSTANCE", "MetaData", "argument", "protocol",
            "middleware", "BreakPoint", "container", "gradient", "Heritage",
            "UNIVERSE", "velocity", "frontend", "Backend", "operator"
    };

    private static final String[] TIER_4 = {
            "asynchronous", "polymorphism", "abstraction", "inheritance", "repository",
            "microservices", "concurrency", "deployment", "dependency", "environment",
            "performance", "transaction", "encryption", "throughput", "redundancy",
            "javascript", "reflection", "serialized", "immutable", "validation"
    };

    private static final String[] TIER_5 = {
            "authentication", "infrastructure", "multithreading", "implementation", "uninterrupted",
            "synchronization", "decentralized", "encapsulation", "orchestration", "compatibility",
            "cryptography", "standardization", "maintainability", "vulnerability", "instantaneous",
            "metaprogramming", "parallelization", "functionality", "troubleshooting", "superstructure"
    };

    private List<String> deck = new ArrayList<>();
    private LevelConfig.Tier currentTier = null;
    private int index = 0;

    public String getRandomWord(int level) {
        LevelConfig.Tier tier = LevelConfig.getTierForLevel(level);

        if (tier != currentTier || index >= deck.size()) {
            currentTier = tier;
            deck = shuffled(tierForLevel(tier));
            index = 0;
        }

        return deck.get(index++);
    }

    private List<String> shuffled(String[] words) {
        List<String> list = new ArrayList<>(List.of(words));
        Collections.shuffle(list);
        return list;
    }

    private String[] tierForLevel(LevelConfig.Tier tier) {
        return switch (tier) {
            case T1 -> TIER_1;
            case T2 -> TIER_2;
            case T3 -> TIER_3;
            case T4 -> TIER_4;
            default -> TIER_5;
        };
    }
}
