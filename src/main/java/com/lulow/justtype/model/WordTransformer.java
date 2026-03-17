package com.lulow.justtype.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Applies casing transformations to words for the EXPERT tier.
 * Randomly uppercases a subset of characters to increase difficulty.
 */
public class WordTransformer {

    /** Minimum number of characters to uppercase. */
    private static final int MIN_UPPERCASE_COUNT = 1;

    /** Maximum number of characters to uppercase. */
    private static final int MAX_UPPERCASE_COUNT = 3;

    /**
     * Lowercases the word, then randomly uppercases between
     * {@value #MIN_UPPERCASE_COUNT} and {@value #MAX_UPPERCASE_COUNT} characters.
     *
     * @param word the input word
     * @return the transformed word with random uppercase characters
     */
    public String applyTier5Casing(String word) {
        char[] chars = word.toLowerCase().toCharArray();

        int uppercaseCount = MIN_UPPERCASE_COUNT
                + (int)(Math.random() * (MAX_UPPERCASE_COUNT - MIN_UPPERCASE_COUNT + 1));
        uppercaseCount = Math.min(uppercaseCount, chars.length);

        List<Integer> indices = new ArrayList<>();
        for (int index = 0; index < chars.length; index++) indices.add(index);
        Collections.shuffle(indices);

        for (int pos = 0; pos < uppercaseCount; pos++) {
            int targetIndex = indices.get(pos);
            chars[targetIndex] = Character.toUpperCase(chars[targetIndex]);
        }

        return new String(chars);
    }
}