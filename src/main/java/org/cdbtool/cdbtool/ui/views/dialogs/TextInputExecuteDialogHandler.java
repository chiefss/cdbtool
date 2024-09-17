package org.cdbtool.cdbtool.ui.views.dialogs;

import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.connectors.Connector;
import org.cdbtool.cdbtool.connectors.ConnectorFactory;
import org.cdbtool.cdbtool.dtos.ConnectionDto;
import org.cdbtool.cdbtool.exceptions.UIException;
import org.cdbtool.cdbtool.ui.UI;
import org.cdbtool.cdbtool.ui.views.DataGridView;

@Log4j2
public class TextInputExecuteDialogHandler {

    public static final String SQL_QUERY_EXECUTED_STATUS_LABEL = "Sql query executed";
    private final UI ui;
    private final ConnectionDto connectionDto;
    private final String schemaName;

    public TextInputExecuteDialogHandler(UI ui, ConnectionDto connectionDto, String schemaName) {
        this.ui = ui;
        this.connectionDto = connectionDto;
        this.schemaName = schemaName;
    }

    public void handleSelect(String sql) {
        try {
            ui.getContentPanel().setContent(DataGridView.Builder.build(ui, connectionDto.getId(), schemaName, sql, false));
        } catch (UIException e) {
            log.error(e.getMessage());
            ui.getStatusPanel().setText(e.getMessage());
        }
    }

    public void handleUpdate(String sql) {
        try (Connector connector = ConnectorFactory.create(connectionDto, schemaName)) {
            connector.execute(sql);
            ui.getStatusPanel().setText(SQL_QUERY_EXECUTED_STATUS_LABEL);
        } catch (Exception e) {
            log.error(e.getMessage());
            ui.getStatusPanel().setText(e.getMessage());
        }
    }
}