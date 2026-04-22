package com.wmsai.oopj;

import java.util.HashMap;
import java.util.Map;

/**
 * Text analysis utility.
 * Covers T067, OOPJ-13 (vowel/consonant counter), OOPJ-14 (count capital-starting words).
 */
public class TextAnalyzer {

    /**
     * Counts vowels and consonants in a string [OOPJ-13].
     * @return Map with keys "vowels" and "consonants"
     */
    public static Map<String, Integer> countVowelsAndConsonants(String line) {
        int vowels = 0, consonants = 0;
        String lower = line.toLowerCase();
        for (char c : lower.toCharArray()) {
            if (Character.isLetter(c)) {
                if ("aeiou".indexOf(c) >= 0) {
                    vowels++;
                } else {
                    consonants++;
                }
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("vowels", vowels);
        result.put("consonants", consonants);
        return result;
    }

    /**
     * Counts words starting with a capital letter [OOPJ-14].
     */
    public static int countCapitalStartWords(String line) {
        if (line == null || line.isBlank()) return 0;
        int count = 0;
        for (String word : line.split("\\s+")) {
            if (!word.isEmpty() && Character.isUpperCase(word.charAt(0))) {
                count++;
            }
        }
        return count;
    }
}
