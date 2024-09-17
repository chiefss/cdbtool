package org.cdbtool.cdbtool.ui.panels;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.cdbtool.cdbtool.dtos.ShortcutDto;
import org.cdbtool.cdbtool.enums.ShortcutFKeyEnum;
import org.cdbtool.cdbtool.exceptions.ResizeWindowException;
import org.cdbtool.cdbtool.ui.Defines;
import org.cdbtool.cdbtool.ui.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShortcutPanel extends Panel implements UIPanel {

    public static final String SHORTCUT_ABOUT_LABEL = "About";
    public static final String SHORTCUT_MENU_LABEL = "Menu";
    public static final String SHORTCUT_EXIT_LABEL = "Exit";
    private final UI ui;

    @Setter
    private List<ShortcutDto> replacedShortcuts = new ArrayList<>();

    public ShortcutPanel(UI ui) {
        this.ui = ui;
    }

    public void runShortcut(int shortcutIndex) {
        Optional<ShortcutDto> replacedShortcutByIndex = getReplacedShortcutByIndex(shortcutIndex);
        if (replacedShortcutByIndex.isPresent()) {
            replacedShortcutByIndex.get().getAction().run();
        } else {
            getDefaultShortcutByIndex(shortcutIndex)
                    .ifPresent(shortcutDto -> shortcutDto.getAction().run());
        }
    }

    public void init() {
        removeAllComponents();
        GridLayout shortcutPanelLayoutManager = new GridLayout(ShortcutFKeyEnum.values().length);
        setLayoutManager(shortcutPanelLayoutManager);
        setTheme(new SimpleTheme(TextColor.Factory.fromString(Defines.THEME_COLOR_WHITE), TextColor.Factory.fromString(Defines.THEME_COLOR_DARK)));
        SimpleTheme theme = new SimpleTheme(TextColor.Factory.fromString(Defines.THEME_COLOR_DARK), TextColor.Factory.fromString(Defines.THEME_COLOR_GREEN));
        for (int shortcutIndex = 1; shortcutIndex < ShortcutFKeyEnum.values().length + 1; shortcutIndex++) {
            String text = getShortcutLabelOrDefault(shortcutIndex, Strings.EMPTY);
            Label buttonNumber = new Label(String.valueOf(shortcutIndex));
            Label shortcut = new Label(text);
            shortcut.setTheme(theme);
            Panel shortcutItemPanel = new Panel();
            LinearLayout panelLayoutManager = new LinearLayout(Direction.HORIZONTAL);
            shortcutItemPanel.setLayoutManager(panelLayoutManager);
            shortcutItemPanel.addComponent(buttonNumber);
            shortcutItemPanel.addComponent(shortcut);
            addComponent(shortcutItemPanel);
        }
    }

    public void resize() {
        try {
            setPosition(new TerminalPosition(0, ui.getTerminal().getTerminalSize().getRows() - Defines.SHORTCUT_PANEL_SIZE_ROWS - Defines.MENU_BAR_PANEL_SIZE_ROWS));
            TerminalSize terminalSize = new TerminalSize(ui.getTerminal().getTerminalSize().getColumns(), Defines.SHORTCUT_PANEL_SIZE_ROWS);
            setPreferredSize(terminalSize);
            setSize(terminalSize);
        } catch (IOException e) {
            throw new ResizeWindowException(e);
        }
        List<Component> shortcutPanelChildrenList = getChildrenList();
        int shortcutPanelChildrenSize = shortcutPanelChildrenList.size();
        int shortcutPanelSizeColumns = getPreferredSize().getColumns();
        for (Component shortcutPanelChild : shortcutPanelChildrenList) {
            Panel shortcutItemPanel = (Panel) shortcutPanelChild;
            Label shortcutItemLabel = (Label) shortcutItemPanel.getChildrenList().get(1);
            int componentSize = shortcutPanelSizeColumns / shortcutPanelChildrenSize - 2;
            shortcutItemLabel.setText(String.format("%-" + componentSize + "s", shortcutItemLabel.getText().trim()));
        }
    }

    private String getShortcutLabelOrDefault(int shortcutIndex, String defaultValue) {
        Optional<ShortcutDto> optionalShortcutDto = replacedShortcuts.stream()
                .filter(shortcutDto -> shortcutDto.getIndex() == shortcutIndex)
                .findFirst();
        if (optionalShortcutDto.isPresent()) {
            return optionalShortcutDto.get().getLabel();
        }
        return getDefaultShortcutByIndex(shortcutIndex)
                .map(ShortcutDto::getLabel)
                .orElse(defaultValue);
    }

    private Optional<ShortcutDto> getReplacedShortcutByIndex(int shortcutIndex) {
        return replacedShortcuts.stream()
                .filter(shortcutDto -> shortcutDto.getIndex() == shortcutIndex)
                .findFirst();
    }

    private Optional<ShortcutDto> getDefaultShortcutByIndex(int shortcutIndex) {
        return getDefaultShortcuts().stream()
                .filter(shortcutDto -> shortcutDto.getIndex() == shortcutIndex)
                .findFirst();
    }

    private List<ShortcutDto> getDefaultShortcuts() {
        return List.of(
                new ShortcutDto(ShortcutFKeyEnum.F1.getIndex(), SHORTCUT_ABOUT_LABEL, () -> {}),
                new ShortcutDto(ShortcutFKeyEnum.F9.getIndex(), SHORTCUT_MENU_LABEL, () -> {
                    MenuBar menubar = ui.getWindow().getMenuBar();
                    if (!menubar.isEmptyMenuBar()) {
                        Menu menu = menubar.getMenu(0);
                        menu.takeFocus();
                    }
                }),
                new ShortcutDto(ShortcutFKeyEnum.F10.getIndex(), SHORTCUT_EXIT_LABEL, () -> System.exit(0))
        );
    }
}
