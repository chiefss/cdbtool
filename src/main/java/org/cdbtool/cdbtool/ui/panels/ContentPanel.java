package org.cdbtool.cdbtool.ui.panels;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Panel;
import org.apache.logging.log4j.util.Strings;
import org.cdbtool.cdbtool.exceptions.ResizeWindowException;
import org.cdbtool.cdbtool.ui.Defines;
import org.cdbtool.cdbtool.ui.UI;
import org.cdbtool.cdbtool.ui.views.ViewComponent;

import java.io.IOException;
import java.util.Optional;

public class ContentPanel extends Panel implements UIPanel {

    private final UI ui;
    private final Panel content;

    public ContentPanel(UI ui) {
        this.ui = ui;
        content = new Panel();
    }

    public void setContent(ViewComponent component) {
        content.removeAllComponents();
        content.addComponent(component);
        ui.getShortcutPanel().setReplacedShortcuts(component.getShortcuts());
        ui.getShortcutPanel().init();
        ui.getShortcutPanel().resize();
        ui.getWindow().setFocusedInteractable(this.nextFocus(null));
        ui.getStatusPanel().setText(Strings.EMPTY);
    }

    public void backOrSetContent(ViewComponent component) {
        Optional<ViewComponent> historyOptional = ui.getHistoryList().take();
        if (historyOptional.isPresent()) {
            setContent(historyOptional.get());
        } else {
            setContent(component);
        }
    }

    public void init() {
        SimpleTheme contentTheme = new SimpleTheme(TextColor.Factory.fromString(Defines.THEME_COLOR_WHITE), TextColor.Factory.fromString(Defines.THEME_COLOR_BLUE));
        contentTheme.getDefaultDefinition()
                .setSelected(TextColor.Factory.fromString(Defines.THEME_COLOR_DARK), TextColor.Factory.fromString(Defines.THEME_COLOR_GREEN))
                .setActive(TextColor.Factory.fromString(Defines.THEME_COLOR_DARK), TextColor.Factory.fromString(Defines.THEME_COLOR_GREEN));
        addComponent(content.withBorder(Borders.singleLine()))
                .setTheme(contentTheme);
    }

    public void resize() {
        TerminalSize terminalSize;
        try {
            int columns = ui.getTerminal().getTerminalSize().getColumns();
            int rows = ui.getTerminal().getTerminalSize().getRows() - Defines.STATUS_PANEL_SIZE_ROWS - Defines.SHORTCUT_PANEL_SIZE_ROWS - Defines.MENU_BAR_PANEL_SIZE_ROWS;
            terminalSize = new TerminalSize(columns, rows);
        } catch (IOException e) {
            throw new ResizeWindowException(e);
        }
        setPosition(new TerminalPosition(0, 0));
        setPreferredSize(terminalSize);
        setSize(terminalSize);
        getChildrenList().forEach(component -> {
            component.setPreferredSize(terminalSize);
            component.setSize(terminalSize);
        });
    }
}
