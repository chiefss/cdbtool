package org.cdbtool.cdbtool.ui.views;


import com.googlecode.lanterna.gui2.table.TableModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.connectors.Connector;
import org.cdbtool.cdbtool.connectors.ConnectorFactory;
import org.cdbtool.cdbtool.dtos.ConnectionDto;
import org.cdbtool.cdbtool.dtos.ShortcutDto;
import org.cdbtool.cdbtool.dtos.TableDto;
import org.cdbtool.cdbtool.enums.ShortcutFKeyEnum;
import org.cdbtool.cdbtool.exceptions.UIException;
import org.cdbtool.cdbtool.ui.UI;
import org.cdbtool.cdbtool.ui.views.dialogs.SqlInputExecuteDialog;
import org.cdbtool.cdbtool.utils.ListUtils;

import java.util.List;
import java.util.UUID;

@Log4j2
public class TableListView extends AbstractListView<TableDto> {

    public static final String SHORTCUT_VIEW_LABEL = "View";
    public static final String SHORTCUT_EXEC_LABEL = "Exec";
    private final ConnectionDto connectionDto;
    private final String schemaName;

    private TableListView(UI ui, ConnectionDto connectionDto, String schemaName, String... columnLabels) {
        super(ui, columnLabels);
        this.connectionDto = connectionDto;
        this.schemaName = schemaName;
    }

    public void setRows(List<TableDto> tables) {
        TableModel<String> tableModel = getTableModel();
        tableModel.clear();
        tableModel.addRow(BACK_ELEMENT_LABEL);
        for (TableDto table : tables) {
            tableModel.addRow(table.getName());
        }
    }

    @Override
    public List<ShortcutDto> getShortcuts() {
        return List.of(
                new ShortcutDto(ShortcutFKeyEnum.F2.getIndex(), SHORTCUT_EXEC_LABEL, () -> {
                    SqlInputExecuteDialog sqlInputExecuteDialog = new SqlInputExecuteDialog(getUi(), connectionDto, schemaName);
                    sqlInputExecuteDialog.display();
                }),
                new ShortcutDto(ShortcutFKeyEnum.F3.getIndex(), SHORTCUT_VIEW_LABEL, () -> {
                    setCellSelection(!isCellSelection());
                    invalidate();
                }));
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        public static final String INIT_SQL_QUERY = "SELECT * FROM %s LIMIT 100";

        public static TableListView build(UI ui, UUID connectionId, String schemaName) throws UIException {
            ConnectionDto connectionDto = ListUtils.find(ui.getEnvironment().getConnections(), connectionId, ConnectionDto::getId)
                    .orElseThrow(() -> new UIException(String.format("Cannot find connection by id \"%s\"", connectionId)));
            try (Connector connector = ConnectorFactory.create(connectionDto, schemaName)) {
                List<TableDto> tableDtos = connector.getTables(schemaName);
                TableListView tableListView = new TableListView(ui, connectionDto, schemaName, "name");
                tableListView.setCellSelection(true);
                tableListView.setRows(tableDtos);
                tableListView.setSelectAction(() -> {
                    int selectedRow = tableListView.getSelectedRow();
                    String tableName = tableListView.getTableModel().getRow(selectedRow).get(0);
                    try {
                        if (selectedRow == 0 && tableName.equals(BACK_ELEMENT_LABEL)) {
                            ui.getContentPanel().backOrSetContent(SchemaListView.Builder.build(ui, connectionId));
                        } else {
                            ui.getHistoryList().add(tableListView);
                            ui.getContentPanel().setContent(DataGridView.Builder.build(ui, connectionId, schemaName, String.format(INIT_SQL_QUERY, tableName), true));
                        }
                    } catch (UIException e) {
                        log.error(e.getMessage());
                        ui.getStatusPanel().setText(e.getMessage());
                    }
                });
                tableListView.resize();
                return tableListView;
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new UIException(String.format("Cannot build TableList by connection id \"%s\" and schema name \"%s\"", connectionId, schemaName));
            }
        }
    }
}
