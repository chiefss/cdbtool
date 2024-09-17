package org.cdbtool.cdbtool.observers;

import java.util.ArrayList;
import java.util.List;

public class ConnectionUpdateObserver {

    private final List<ConnectionUpdateListener> listeners = new ArrayList<>();

    public void addListener(ConnectionUpdateListener listener) {
        listeners.add(listener);
    }

    public void update() {
        for (ConnectionUpdateListener listener : listeners) {
            listener.onConnectionUpdated();
        }
    }
}
