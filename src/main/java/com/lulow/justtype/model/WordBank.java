package com.lulow.justtype.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

    private final Random random = new Random();
    private final Set<String> used = new HashSet<>();

    public String getRandomWord(int level) {
        String[] tier = tierForLevel(level);
        String word;
        int attempts = 0;

        do {
            word = tier[random.nextInt(tier.length)];
            attempts++;
        } while (used.contains(word) && attempts < tier.length);

        if (attempts >= tier.length) {
            clearTierUsed(tier);
        }

        used.add(word);
        return word;
    }

    private void clearTierUsed(String[] tier) {
        for (String word : tier) {
            used.remove(word);
        }
    }

    private String[] tierForLevel(int level) {
        return switch (LevelConfig.getTierForLevel(level)) {
            case T1 -> TIER_1;
            case T2 -> TIER_2;
            case T3 -> TIER_3;
            case T4 -> TIER_4;
            default -> TIER_5;
        };
    }
}
