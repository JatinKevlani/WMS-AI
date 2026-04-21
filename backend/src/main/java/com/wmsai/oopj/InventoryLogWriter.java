package com.wmsai.oopj;

import java.io.*;
import java.nio.file.*;
import java.util.Random;

/**
 * Writes random stock values to a file and uses RecursiveUtils to find the smallest.
 * Covers OOPJ Practical #27 — create/append 150 random integers, recursive smallest.
 * Uses FileWriter [OOPJ-26].
 */
public class InventoryLogWriter {

    private static final int COUNT = 150;

    /**
     * Writes 150 random integers (1–1000) to a file, space-separated.
     *
     * @param filePath path to the output file
     * @return the array of random integers written
     * @throws IOException if file writing fails
     */
    public static int[] writeRandomStockLog(String filePath) throws IOException {
        Files.createDirectories(Path.of(filePath).getParent());

        Random random = new Random();
        int[] values = new int[COUNT];

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (int i = 0; i < COUNT; i++) {
                values[i] = random.nextInt(1000) + 1;
                writer.print(values[i]);
                if (i < COUNT - 1) writer.print(" ");
            }
        }

        return values;
    }

    /**
     * Reads integers from a file and finds the smallest using RecursiveUtils.
     *
     * @param filePath path to the file containing space-separated integers
     * @return the smallest integer found
     * @throws IOException if file reading fails
     */
    public static int readAndFindSmallest(String filePath) throws IOException {
        String content = Files.readString(Path.of(filePath)).trim();
        String[] parts = content.split("\\s+");
        int[] values = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            values[i] = Integer.parseInt(parts[i]);
        }
        return RecursiveUtils.findSmallest(values);
    }

    /**
     * Combined operation: write random stock log and find smallest.
     *
     * @param filePath path to output file
     * @return the smallest value written
     * @throws IOException if I/O fails
     */
    public static int writeAndFindSmallest(String filePath) throws IOException {
        int[] values = writeRandomStockLog(filePath);
        return RecursiveUtils.findSmallest(values);
    }
}
