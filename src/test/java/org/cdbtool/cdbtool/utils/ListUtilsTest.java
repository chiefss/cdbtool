package org.cdbtool.cdbtool.utils;

import org.cdbtool.cdbtool.exceptions.UtilsException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ListUtilsTest {

    @Test
    void testFindOrThrow_Found() throws UtilsException {
        List<String> items = Arrays.asList("first", "second", "third");

        String result = ListUtils.findOrThrow(items, "second", item -> item);

        assertEquals("second", result);
    }

    @Test
    void testFindOrThrow_NotFound() {
        List<String> items = Arrays.asList("first", "second", "third");

        Exception exception = assertThrows(UtilsException.class, () -> {
            ListUtils.findOrThrow(items, "hello", item -> item);
        });

        assertEquals("Cannot find item", exception.getMessage());
    }

    @Test
    void testFind_Found() {
        List<String> items = Arrays.asList("first", "second", "third");

        Optional<String> result = ListUtils.find(items, "third", item -> item);

        assertTrue(result.isPresent());
        assertEquals("third", result.get());
    }

    @Test
    void testFind_NotFound() {
        List<String> items = Arrays.asList("first", "second", "third");

        Optional<String> result = ListUtils.find(items, "hello", item -> item);

        assertFalse(result.isPresent());
    }
}