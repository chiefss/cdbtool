package org.cdbtool.cdbtool.ui.listeners;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.enums.ShortcutFKeyEnum;
import org.cdbtool.cdbtool.ui.UI;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class WindowListener extends WindowListenerAdapter {

    private final UI ui;

    public WindowListener(UI ui) {
        this.ui = ui;
    }

    @Override
    public void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {

    }

    @Override
    public void onMoved(Window window, TerminalPosition oldPosition, TerminalPosition newPosition) {

    }

    @Override
    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {

    }

    @Override
    public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
        Optional<Integer> fKeyValue = ShortcutFKeyEnum.getIndexByKey(keyStroke.getKeyType());
        if (fKeyValue.isPresent()) {
            ui.getShortcutPanel().runShortcut(fKeyValue.get());
        } else if (keyStroke.getKeyType() == KeyType.Escape) {
            ui.getWindow().setFocusedInteractable(ui.getContentPanel().nextFocus(null));
        }
    }
}
