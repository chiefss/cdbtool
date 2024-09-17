package org.cdbtool.cdbtool.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.AbsoluteLayout;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.cdbtool.cdbtool.exceptions.ResizeWindowException;
import org.cdbtool.cdbtool.ui.panels.UIPanel;

import java.io.IOException;
import java.util.List;

public class BaseWindow extends BasicWindow implements UIPanel {

    private final Panel windowComponent;
    private final UI ui;

    public BaseWindow(UI ui) {
        this.ui = ui;
        windowComponent = new Panel();
    }

    @Override
    public boolean handleInput(KeyStroke key) {
        KeyType keyType = key.getKeyType();
        if (keyType == KeyType.ArrowUp || keyType == KeyType.ArrowDown || keyType == KeyType.ArrowLeft || keyType == KeyType.ArrowRight) {
//            return true;
        }
        if (keyType == KeyType.Tab) {
            return false;
        }
        return super.handleInput(key);
    }

    public void init() {
        AbsoluteLayout layoutManager = new AbsoluteLayout();
        windowComponent.setLayoutManager(layoutManager);
        setPosition(TerminalPosition.TOP_LEFT_CORNER);
        setHints(List.of(
                Window.Hint.CENTERED,
                Window.Hint.FIXED_POSITION,
                Window.Hint.NO_DECORATIONS
        ));
        windowComponent.addComponent(ui.getContentPanel());
        windowComponent.addComponent(ui.getStatusPanel());
        windowComponent.addComponent(ui.getShortcutPanel());
        setMenuBar(ui.getMenubar());
        setComponent(windowComponent);
    }

    public void resize() {
        try {
            windowComponent.setPreferredSize(new TerminalSize(ui.getTerminal().getTerminalSize().getColumns(), ui.getTerminal().getTerminalSize().getRows()));
        } catch (IOException e) {
            throw new ResizeWindowException(e);
        }
    }
}
