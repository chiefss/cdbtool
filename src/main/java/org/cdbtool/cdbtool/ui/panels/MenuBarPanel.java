package org.cdbtool.cdbtool.ui.panels;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.ui.Defines;

@Log4j2
public class MenuBarPanel extends MenuBar implements UIPanel {

    public void init() {
        SimpleTheme menuTheme = new SimpleTheme(TextColor.Factory.fromString(Defines.THEME_COLOR_DARK), TextColor.Factory.fromString(Defines.THEME_COLOR_GREEN));
        menuTheme.getDefaultDefinition()
                .setSelected(TextColor.Factory.fromString(Defines.THEME_COLOR_WHITE), TextColor.Factory.fromString(Defines.THEME_COLOR_DARK));

        Menu file = new Menu("File");
        file.setTheme(menuTheme);
        file.add(new MenuItem("Open...", () -> log.debug("123")));
        file.add(new MenuItem("Save...", () -> log.debug("1234")));

        Menu about = new Menu("About");
        about.setTheme(menuTheme);
        about.add(new MenuItem("About tool", () -> log.debug("234")));
        about.add(new MenuItem("About tool 2", () -> log.debug("345")));

        setTheme(new SimpleTheme(TextColor.Factory.fromString(Defines.THEME_COLOR_BLACK), TextColor.Factory.fromString(Defines.THEME_COLOR_GREEN)));
        add(file);
        add(about);
    }

    public void resize() {
        // automatically resize by library
    }
}
