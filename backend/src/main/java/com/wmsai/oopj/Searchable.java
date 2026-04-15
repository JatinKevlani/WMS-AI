package com.wmsai.oopj;

/**
 * Searchable interface with search methods.
 * Covers T034, OOPJ-19 — interface with searchById and searchByName.
 */
public interface Searchable {
    /** Search entity by its unique identifier. */
    Object searchById(int id);

    /** Search entity by name (partial match). */
    Object searchByName(String name);
}
