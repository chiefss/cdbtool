package org.cdbtool.cdbtool.ui.listeners;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.Terminal;
import org.cdbtool.cdbtool.ui.UI;

public class ResizeListener implements com.googlecode.lanterna.terminal.TerminalResizeListener {

    private final UI ui;

    public ResizeListener(UI ui) {
        this.ui = ui;
    }

    @Override
    public void onResized(Terminal terminal, TerminalSize terminalSize) {
        ui.resize();
    }

}
