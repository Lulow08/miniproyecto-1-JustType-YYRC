package com.lulow.justtype.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordTransformer {

    private static final int MIN_UPPERCASE_COUNT = 1;
    private static final int MAX_UPPERCASE_COUNT = 3;

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