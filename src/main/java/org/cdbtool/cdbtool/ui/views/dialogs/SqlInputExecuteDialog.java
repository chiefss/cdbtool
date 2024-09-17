package org.cdbtool.cdbtool.ui.views.dialogs;

import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.cdbtool.cdbtool.dtos.ConnectionDto;
import org.cdbtool.cdbtool.ui.UI;
import org.cdbtool.cdbtool.utils.SqlUtils;

@Log4j2
public class SqlInputExecuteDialog {

    private final UI ui;
    private final TextInputExecuteDialogHandler textInputExecuteDialogHandler;

    public SqlInputExecuteDialog(UI ui, ConnectionDto connectionDto, String schemaName) {
        this.ui = ui;
        textInputExecuteDialogHandler = new TextInputExecuteDialogHandler(ui, connectionDto, schemaName);
    }

    public void display() {
        TextInputDialogBuilder textInputDialogBuilder = new TextInputExecuteDialogBuilder()
                .build();
        String inputText = showDialogAndGetInput(textInputDialogBuilder);
        handleInput(inputText);
    }

    public void display(String initialSql) {
        TextInputDialogBuilder textInputDialogBuilder = new TextInputExecuteDialogBuilder()
                .setInitialContent(initialSql)
                .build();
        String inputText = showDialogAndGetInput(textInputDialogBuilder);
        handleInput(inputText);
    }

    private String showDialogAndGetInput(TextInputDialogBuilder textInputDialogBuilder) {
        return textInputDialogBuilder.build().showDialog(ui.getWindow().getTextGUI());
    }

    private void handleInput(String sql) {
        if (Strings.isBlank(sql)) {
            return;
        }
        if (!SqlUtils.isSelect(sql)) {
            textInputExecuteDialogHandler.handleUpdate(sql);
            return;
        }
        textInputExecuteDialogHandler.handleSelect(sql);
    }
}
