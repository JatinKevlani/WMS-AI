package com.wmsai.oopj;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Reads words from a file and outputs non-duplicate words sorted in descending order.
 * Covers OOPJ Practical #29 — non-duplicate words descending from file.
 * Uses BufferedReader [OOPJ-27].
 */
public class TagDeduplicator {

    /**
     * Reads all words from a file, removes duplicates, and returns them sorted descending.
     *
     * @param filePath path to the text file
     * @return list of unique words sorted in descending order
     * @throws IOException if file reading fails
     */
    public static List<String> readAndDeduplicate(String filePath) throws IOException {
        Set<String> uniqueWords = new LinkedHashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        uniqueWords.add(word.toLowerCase());
                    }
                }
            }
        }

        List<String> sorted = new ArrayList<>(uniqueWords);
        sorted.sort(Collections.reverseOrder());
        return sorted;
    }

    /**
     * Processes an in-memory string array (for API use without requiring a file).
     *
     * @param words array of words to deduplicate
     * @return list of unique words sorted descending
     */
    public static List<String> deduplicateAndSort(String[] words) {
        Set<String> unique = new LinkedHashSet<>();
        for (String word : words) {
            if (word != null && !word.trim().isEmpty()) {
                unique.add(word.trim().toLowerCase());
            }
        }
        List<String> sorted = new ArrayList<>(unique);
        sorted.sort(Collections.reverseOrder());
        return sorted;
    }
}
