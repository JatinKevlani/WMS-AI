package com.wmsai.oopj;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Vowel Tracker — analyses text, tracks running count per vowel type.
 * Covers OOPJ Practical #16 — loop processing sentences, running vowel totals, exits on "quit".
 */
public class VowelTracker {

    private int countA = 0;
    private int countE = 0;
    private int countI = 0;
    private int countO = 0;
    private int countU = 0;
    private int totalProcessed = 0;

    /**
     * Processes a single sentence and updates running vowel totals.
     *
     * @param sentence the text to analyze
     * @return map of vowel counts for this sentence
     */
    public Map<String, Integer> processSentence(String sentence) {
        if (sentence == null) return Map.of();

        Map<String, Integer> sentenceCounts = new LinkedHashMap<>();
        int a = 0, e = 0, i = 0, o = 0, u = 0;

        for (char c : sentence.toLowerCase().toCharArray()) {
            switch (c) {
                case 'a' -> { a++; countA++; }
                case 'e' -> { e++; countE++; }
                case 'i' -> { i++; countI++; }
                case 'o' -> { o++; countO++; }
                case 'u' -> { u++; countU++; }
            }
        }

        totalProcessed++;
        sentenceCounts.put("a", a);
        sentenceCounts.put("e", e);
        sentenceCounts.put("i", i);
        sentenceCounts.put("o", o);
        sentenceCounts.put("u", u);
        sentenceCounts.put("totalVowels", a + e + i + o + u);

        return sentenceCounts;
    }

    /**
     * Processes multiple sentences, stopping when "quit" is encountered.
     *
     * @param sentences array of sentences to process
     * @return running totals after all processing
     */
    public Map<String, Object> processUntilQuit(String[] sentences) {
        for (String s : sentences) {
            if ("quit".equalsIgnoreCase(s.trim())) break;
            processSentence(s);
        }
        return getRunningTotals();
    }

    /** Returns the running vowel totals across all processed sentences. */
    public Map<String, Object> getRunningTotals() {
        Map<String, Object> totals = new LinkedHashMap<>();
        totals.put("a", countA);
        totals.put("e", countE);
        totals.put("i", countI);
        totals.put("o", countO);
        totals.put("u", countU);
        totals.put("grandTotal", countA + countE + countI + countO + countU);
        totals.put("sentencesProcessed", totalProcessed);
        return totals;
    }

    /** Resets all counters. */
    public void reset() {
        countA = countE = countI = countO = countU = totalProcessed = 0;
    }
}
