package com.lulow.justtype.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordBank {

    private static final String[] BEGINNER_WORDS = {
            "class", "git", "run", "bool", "IDEA",
            "star", "java", "fast", "game", "cast",
            "Type", "tree", "zip", "model", "word",
            "bug", "View", "main", "virus", "root"
    };

    private static final String[] EASY_WORDS = {
            "integer", "commit", "Controller", "buffer", "Syntax",
            "module", "compile", "pointer", "interface", "console",
            "binary", "directory", "Source", "PACKAGE", "virtual",
            "Lambda", "String", "TERMINAL", "deploy", "Library"
    };

    private static final String[] MEDIUM_WORDS = {
            "framework", "Algorithm", "database", "endpoint", "CallBack",
            "recursion", "INSTANCE", "MetaData", "argument", "protocol",
            "middleware", "BreakPoint", "container", "gradient", "Heritage",
            "UNIVERSE", "velocity", "frontend", "Backend", "operator"
    };

    private static final String[] HARD_WORDS = {
            "asynchronous", "polymorphism", "abstraction", "inheritance", "repository",
            "MicroServices", "concurrency", "deployment", "Dependency", "Environment",
            "performance", "transaction", "Encryption", "throughput", "redundancy",
            "JavaScript", "reflection", "Serialized", "IMMUTABLE", "validation"
    };

    private static final String[] EXPERT_WORDS = {
            "authentication", "infrastructure", "multithreading", "implementation", "uninterrupted",
            "synchronization", "decentralized", "encapsulation", "orchestration", "compatibility",
            "cryptography", "standardization", "maintainability", "vulnerability", "instantaneous",
            "metaprogramming", "parallelization", "functionality", "troubleshooting", "superstructure"
    };

    private List<String>     deck        = new ArrayList<>();
    private LevelConfig.Tier currentTier = null;
    private int              deckIndex   = 0;

    public String getRandomWord(int level) {
        LevelConfig.Tier tier = LevelConfig.getTierForLevel(level);

        if (tier != currentTier || deckIndex >= deck.size()) {
            currentTier = tier;
            deck        = shuffled(wordsForTier(tier));
            deckIndex   = 0;
        }

        return deck.get(deckIndex++);
    }

    private List<String> shuffled(String[] words) {
        List<String> list = new ArrayList<>(List.of(words));
        Collections.shuffle(list);
        return list;
    }

    private String[] wordsForTier(LevelConfig.Tier tier) {
        return switch (tier) {
            case BEGINNER -> BEGINNER_WORDS;
            case EASY     -> EASY_WORDS;
            case MEDIUM   -> MEDIUM_WORDS;
            case HARD     -> HARD_WORDS;
            default       -> EXPERT_WORDS;
        };
    }
}
