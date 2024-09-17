package org.cdbtool.cdbtool.enums;

import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShortcutFKeyEnumTest {

    @Test
    void testGetIndexByKey_Valid() {
        Optional<Integer> index = ShortcutFKeyEnum.getIndexByKey(KeyType.F1);
        assertTrue(index.isPresent());
        assertEquals(1, index.get());
    }

    @Test
    void testGetIndexByKey_Invalid() {
        Optional<Integer> index = ShortcutFKeyEnum.getIndexByKey(KeyType.F10);
        assertTrue(index.isPresent());
        assertEquals(10, index.get());
    }

    @Test
    void testGetIndexByKey_NotFound() {
        Optional<Integer> index = ShortcutFKeyEnum.getIndexByKey(KeyType.Enter);
        assertFalse(index.isPresent());
    }
}