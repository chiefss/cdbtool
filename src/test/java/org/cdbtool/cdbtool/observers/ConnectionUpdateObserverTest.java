package org.cdbtool.cdbtool.observers;

import org.cdbtool.cdbtool.ui.views.ViewComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectionUpdateObserverTest {

    private ConnectionUpdateObserver observer;

    @BeforeEach
    void setUp() {
        observer = new ConnectionUpdateObserver();
    }

    @Test
    void testAddListener() throws NoSuchFieldException, IllegalAccessException {
        Class<?> myClass = observer.getClass();
        Field field = myClass.getDeclaredField("listeners");
        field.setAccessible(true);
        List<ConnectionUpdateListener> value = (List<ConnectionUpdateListener>) field.get(observer);

        observer.addListener(mock(ConnectionUpdateListener.class));

        assertEquals(1, value.size());
    }

    @Test
    void testUpdate_NotifiesListeners() {
        ConnectionUpdateListener listener = mock(ConnectionUpdateListener.class);
        observer.addListener(listener);

        observer.update();

        verify(listener, times(1)).onConnectionUpdated();
    }

    @Test
    void testUpdate_NotifiesMultipleListeners() {
        ConnectionUpdateListener listener1 = mock(ConnectionUpdateListener.class);
        ConnectionUpdateListener listener2 = mock(ConnectionUpdateListener.class);
        observer.addListener(listener1);
        observer.addListener(listener2);

        observer.update();

        verify(listener1, times(1)).onConnectionUpdated();
        verify(listener2, times(1)).onConnectionUpdated();
    }
}