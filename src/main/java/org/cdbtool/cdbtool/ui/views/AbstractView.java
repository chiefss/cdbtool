package org.cdbtool.cdbtool.ui.views;


import com.googlecode.lanterna.gui2.Panel;
import lombok.Getter;
import org.cdbtool.cdbtool.ui.UI;

public abstract class AbstractView extends Panel implements ViewComponent {

    @Getter
    private final UI ui;

    AbstractView(UI ui) {
        this.ui = ui;
    }
}
