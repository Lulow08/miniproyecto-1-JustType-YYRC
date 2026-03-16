package com.lulow.justtype.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordTransformer {

    private static final int MIN_UPPERCASE = 1;
    private static final int MAX_UPPERCASE = 3;

    public String applyTier5Casing(String word) {
        char[] chars = word.toLowerCase().toCharArray();

        int count = MIN_UPPERCASE + (int)(Math.random() * (MAX_UPPERCASE - MIN_UPPERCASE + 1));
        count = Math.min(count, chars.length);

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) indices.add(i);
        Collections.shuffle(indices);

        for (int i = 0; i < count; i++) {
            int idx = indices.get(i);
            chars[idx] = Character.toUpperCase(chars[idx]);
        }

        return new String(chars);
    }
}