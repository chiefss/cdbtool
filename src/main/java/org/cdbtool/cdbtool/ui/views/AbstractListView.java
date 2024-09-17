package org.cdbtool.cdbtool.ui.views;


import com.googlecode.lanterna.gui2.table.Table;
import lombok.Getter;
import org.cdbtool.cdbtool.exceptions.ResizeWindowException;
import org.cdbtool.cdbtool.ui.UI;

import java.io.IOException;
import java.util.List;

public abstract class AbstractListView<T> extends Table<String> implements ViewComponent {

    public static final String BACK_ELEMENT_LABEL = "..";

    public static final String NULL_VALUE = "<null>";

    @Getter
    private final UI ui;

    AbstractListView(UI ui, String... columnLabels) {
        super(columnLabels);
        this.ui = ui;
        setEscapeByArrowKey(false);
    }

    public abstract void setRows(List<T> rows);

    public void resize() {
        try {
            setPreferredSize(ui.getTerminal().getTerminalSize());
        } catch (IOException e) {
            throw new ResizeWindowException(e);
        }
    }
}
