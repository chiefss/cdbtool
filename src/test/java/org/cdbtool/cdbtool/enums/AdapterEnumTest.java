package org.cdbtool.cdbtool.enums;

import org.cdbtool.cdbtool.exceptions.AdapterEnumException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdapterEnumTest {

    @Test
    void testFindById_Valid() throws AdapterEnumException {
        AdapterEnum adapter = AdapterEnum.findById(1);
        assertEquals(AdapterEnum.PG, adapter);
    }

    @Test
    void testFindById_Invalid() {
        AdapterEnumException exception = assertThrows(AdapterEnumException.class, () -> {
            AdapterEnum.findById(9999);
        });
        assertEquals("Cannot find adapter by id \"9999\"", exception.getMessage());
    }

    @Test
    void testFindByName_Valid() throws AdapterEnumException {
        AdapterEnum adapter = AdapterEnum.findByName("Postgres");
        assertEquals(AdapterEnum.PG, adapter);
    }

    @Test
    void testFindByName_Invalid() {
        AdapterEnumException exception = assertThrows(AdapterEnumException.class, () -> {
            AdapterEnum.findByName("other-database");
        });
        assertEquals("Cannot find adapter by name \"other-database\"", exception.getMessage());
    }
}