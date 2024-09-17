package org.cdbtool.cdbtool.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoryList<V> {

    private final List<V> viewComponents;
    private final int historySize;

    public HistoryList(int historySize) {
        this.historySize = historySize;
        viewComponents = new ArrayList<>(historySize);
    }

    public void add(V viewComponent) {
        if (viewComponents.size() == historySize) {
            viewComponents.remove(0);
        }
        viewComponents.add(viewComponent);
    }

    public Optional<V> take() {
        if (viewComponents.isEmpty()) {
            return Optional.empty();
        }
        V viewComponent = viewComponents.get(viewComponents.size() - 1);
        viewComponents.remove(viewComponent);
        return Optional.of(viewComponent);
    }
}
