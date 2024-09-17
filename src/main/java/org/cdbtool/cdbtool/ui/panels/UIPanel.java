package org.cdbtool.cdbtool.ui.panels;

import java.io.IOException;

public interface UIPanel {

    void init();

    void resize() throws IOException;
}
