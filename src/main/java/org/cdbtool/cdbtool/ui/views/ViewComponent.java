package org.cdbtool.cdbtool.ui.views;

import com.googlecode.lanterna.gui2.Component;
import org.cdbtool.cdbtool.dtos.ShortcutDto;

import java.util.List;

public interface ViewComponent extends Component {

    List<ShortcutDto> getShortcuts();
}
