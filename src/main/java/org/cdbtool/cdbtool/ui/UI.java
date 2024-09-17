package org.cdbtool.cdbtool.ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.Environment;
import org.cdbtool.cdbtool.observers.ConnectionUpdateObserver;
import org.cdbtool.cdbtool.ui.listeners.ResizeListener;
import org.cdbtool.cdbtool.ui.listeners.WindowListener;
import org.cdbtool.cdbtool.ui.panels.ContentPanel;
import org.cdbtool.cdbtool.ui.panels.MenuBarPanel;
import org.cdbtool.cdbtool.ui.panels.ShortcutPanel;
import org.cdbtool.cdbtool.ui.panels.StatusPanel;
import org.cdbtool.cdbtool.ui.views.ViewComponent;
import org.cdbtool.cdbtool.utils.HistoryList;

import java.io.IOException;

@Log4j2
@Getter
public class UI {

    public static final int HISTORY_SIZE = 10;
    public static final int DEFAULT_TERMINAL_WIDTH = 120;
    public static final int DEFAULT_TERMINAL_HEIGHT = 40;
    private final Terminal terminal;
    private final TerminalScreen screen;
    private final BaseWindow window;
    private final MenuBarPanel menubar;
    private final ContentPanel contentPanel;
    private final StatusPanel statusPanel;
    private final ShortcutPanel shortcutPanel;

    private final ConnectionUpdateObserver connectionUpdateObserver;

    private final HistoryList<ViewComponent> historyList;

    @Setter
    private Environment environment;

    public UI() throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        defaultTerminalFactory.setTerminalEmulatorTitle(Defines.WINDOW_TITLE);
        defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(DEFAULT_TERMINAL_WIDTH, DEFAULT_TERMINAL_HEIGHT));
        terminal = defaultTerminalFactory.createTerminal();
        screen = new TerminalScreen(terminal);
        menubar = new MenuBarPanel();
        window = new BaseWindow(this);
        contentPanel = new ContentPanel(this);
        statusPanel = new StatusPanel(this);
        shortcutPanel = new ShortcutPanel(this);
        connectionUpdateObserver = new ConnectionUpdateObserver();
        historyList = new HistoryList<>(HISTORY_SIZE);
    }

    public void init() throws IOException {
        screen.startScreen();
        menubar.init();
        window.init();
        contentPanel.init();
        statusPanel.init();
        shortcutPanel.init();
    }

    public void initListeners() {
        ResizeListener resizeListener = new ResizeListener(this);
        terminal.addResizeListener(resizeListener);

        window.addWindowListener(new WindowListener(this));
    }

    public void resize() {
        window.resize();
        menubar.resize();
        contentPanel.resize();
        statusPanel.resize();
        shortcutPanel.resize();
    }

    public void initGui() {
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));
//        window.setFocusedInteractable(null);
        window.setFocusedInteractable(getContentPanel().nextFocus(null));
        gui.addWindowAndWait(window);
    }
}