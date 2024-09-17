package org.cdbtool.cdbtool.utils;

import org.cdbtool.cdbtool.ui.views.ViewComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class HistoryListTest {

    public static final int HISTORY_SIZE = 10;

    HistoryList<ViewComponent> historyList;

    @BeforeEach
    void setUp() {
        historyList = new HistoryList<>(HISTORY_SIZE);
    }

    @Test
    void testAdd() throws NoSuchFieldException, IllegalAccessException {
        Class<?> myClass = historyList.getClass();
        Field field = myClass.getDeclaredField("viewComponents");
        field.setAccessible(true);
        List<ViewComponent> value = (List<ViewComponent>) field.get(historyList);
        
        historyList.add(mock(ViewComponent.class));

        assertEquals(1, value.size());
    }

    @Test
    void testAdd_Max() throws NoSuchFieldException, IllegalAccessException {
        Class<?> myClass = historyList.getClass();
        Field field = myClass.getDeclaredField("viewComponents");
        field.setAccessible(true);
        List<ViewComponent> value = (List<ViewComponent>) field.get(historyList);
        ViewComponent viewComponentFirst = mock(ViewComponent.class);
        value.add(viewComponentFirst);
        for (int i = 0; i < HISTORY_SIZE - 1; i++) {
            ViewComponent viewComponent = mock(ViewComponent.class);
            value.add(viewComponent);
        }
        ViewComponent viewComponentLast = mock(ViewComponent.class);

        historyList.add(viewComponentLast);

        assertNotEquals(viewComponentFirst, value.get(0));
        assertEquals(viewComponentLast, value.get(HISTORY_SIZE - 1));
    }

    @Test
    void testTake() throws NoSuchFieldException, IllegalAccessException {
        Class<?> myClass = historyList.getClass();
        Field field = myClass.getDeclaredField("viewComponents");
        field.setAccessible(true);
        List<ViewComponent> value = (List<ViewComponent>) field.get(historyList);
        ViewComponent viewComponent1 = mock(ViewComponent.class);
        ViewComponent viewComponent2 = mock(ViewComponent.class);
        value.add(viewComponent1);
        value.add(viewComponent2);

        Optional<ViewComponent> viewComponent = historyList.take();

        assertTrue(viewComponent.isPresent());
        assertEquals(viewComponent2, viewComponent.get());
        assertEquals(1, value.size());
        assertEquals(viewComponent1, value.get(0));
    }

    @Test
    void testTake_Empty() {
        Optional<ViewComponent> take = historyList.take();

        assertTrue(take.isEmpty());
    }
}