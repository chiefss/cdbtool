package org.cdbtool.cdbtool.ui.views.dialogs;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;

public class TextInputExecuteDialogBuilder {

    public static final String EXEC_DIALOG_TITLE = "Execute SQL";
    public static final int EXEC_DIALOG_WIDTH = 100;
    public static final int EXEC_DIALOG_HEIGHT = 10;

    private final TextInputDialogBuilder textInputDialogBuilder;

    public TextInputExecuteDialogBuilder() {
        textInputDialogBuilder = new TextInputDialogBuilder()
                .setTitle(EXEC_DIALOG_TITLE)
                .setTextBoxSize(new TerminalSize(EXEC_DIALOG_WIDTH, EXEC_DIALOG_HEIGHT));
    }

    public TextInputExecuteDialogBuilder setInitialContent(String initialContent) {
        textInputDialogBuilder.setInitialContent(initialContent);
        return this;
    }

    public TextInputDialogBuilder build() {
        return textInputDialogBuilder;
    }
}