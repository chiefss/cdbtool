package org.cdbtool.cdbtool.ui.panels;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import org.apache.logging.log4j.util.Strings;
import org.cdbtool.cdbtool.exceptions.ResizeWindowException;
import org.cdbtool.cdbtool.ui.Defines;
import org.cdbtool.cdbtool.ui.UI;

import java.io.IOException;

public class StatusPanel extends Panel implements UIPanel {

    private final Label statusLabel;
    private final UI ui;

    public StatusPanel(UI ui) {
        this.ui = ui;
        statusLabel = new Label(Strings.EMPTY);
    }

    public void setText(String text) {
        statusLabel.setText(text);
    }

    public void init() {
        addComponent(statusLabel)
                .setTheme(new SimpleTheme(TextColor.Factory.fromString(Defines.THEME_COLOR_WHITE), TextColor.Factory.fromString(Defines.THEME_COLOR_DARK)));
    }

    public void resize() {
        try {
            setPosition(new TerminalPosition(0, ui.getTerminal().getTerminalSize().getRows() - Defines.STATUS_PANEL_SIZE_ROWS - Defines.SHORTCUT_PANEL_SIZE_ROWS - 1));
            TerminalSize terminalSize = new TerminalSize(ui.getTerminal().getTerminalSize().getColumns(), Defines.STATUS_PANEL_SIZE_ROWS);
            setPreferredSize(terminalSize);
            setSize(terminalSize);
        } catch (IOException e) {
            throw new ResizeWindowException(e);
        }
    }
}
