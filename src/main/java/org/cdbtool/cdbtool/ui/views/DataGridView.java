package org.cdbtool.cdbtool.ui.views;


import com.googlecode.lanterna.gui2.table.TableModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.connectors.Connector;
import org.cdbtool.cdbtool.connectors.ConnectorFactory;
import org.cdbtool.cdbtool.dtos.*;
import org.cdbtool.cdbtool.enums.ShortcutFKeyEnum;
import org.cdbtool.cdbtool.exceptions.UIException;
import org.cdbtool.cdbtool.ui.UI;
import org.cdbtool.cdbtool.ui.views.dialogs.SqlInputExecuteDialog;
import org.cdbtool.cdbtool.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
public class DataGridView extends AbstractListView<TableRowDto> {
    public static final String SHORTCUT_EXEC_LABEL = "Exec";
    public static final String SHORTCUT_VIEW_LABEL = "View";
    public static final String SHORTCUT_REFRESH_LABEL = "Refresh";
    public static final String SHORTCUT_EDIT_LABEL = "Edit";
    public static final String SHORTCUT_CREATE_LABEL = "Add";
    public static final String SHORTCUT_DELETE_LABEL = "Remove";
    private final ConnectionDto connectionDto;
    private final String schemaName;
    private final String initialSql;
    private final TableSchemaDto tableSchemaDto;

    @Setter
    private boolean editable;

    private DataGridView(UI ui, ConnectionDto connectionDto, String schemaName, String initialSql, TableSchemaDto tableSchemaDto) {
        super(ui, tableSchemaDto.getItems().stream()
                .map(itemDto -> String.format("%s<%s>", itemDto.getName(), itemDto.getType()))
                .toArray(String[]::new));
        this.tableSchemaDto = tableSchemaDto;
        this.connectionDto = connectionDto;
        this.schemaName = schemaName;
        this.initialSql = initialSql;
    }

    public void setRows(List<TableRowDto> rows) {
        TableModel<String> tableModel = getTableModel();
        tableModel.clear();
        int columnCount = rows.get(0).getValue().size();
        List<String> firstRow = new ArrayList<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            firstRow.add(BACK_ELEMENT_LABEL);
        }
        tableModel.addRow(firstRow);
        for (TableRowDto row : rows) {
            for (int i = 0; i < row.getValue().size(); i++) {
                String type = tableSchemaDto.getItems().get(i).getType();
                if (type.equals("bool")) {
                    row.getValue().set(i, row.getValue().get(i).equals("t") ? "true" : "false");
                }
            }
            tableModel.addRow(row.getValue());
        }
    }

    @Override
    public List<ShortcutDto> getShortcuts() {
        List<ShortcutDto> shortcutDtos = new ArrayList<>(List.of(
                new ShortcutDto(ShortcutFKeyEnum.F2.getIndex(), SHORTCUT_EXEC_LABEL, () -> {
                    SqlInputExecuteDialog sqlInputExecuteDialog = new SqlInputExecuteDialog(getUi(), connectionDto, schemaName);
                    sqlInputExecuteDialog.display(initialSql);
                }),
                new ShortcutDto(ShortcutFKeyEnum.F3.getIndex(), SHORTCUT_VIEW_LABEL, () -> {
                    setCellSelection(!isCellSelection());
                    invalidate();
                }),
                new ShortcutDto(ShortcutFKeyEnum.F5.getIndex(), SHORTCUT_REFRESH_LABEL, () -> {
                    try {
                        getUi().getContentPanel().setContent(Builder.build(getUi(), connectionDto.getId(), schemaName, initialSql, false));
                    } catch (UIException e) {
                        log.error(e.getMessage());
                        getUi().getStatusPanel().setText(e.getMessage());
                    }
                })
        ));
        if (editable) {
            shortcutDtos.addAll(List.of(
                    new ShortcutDto(ShortcutFKeyEnum.F4.getIndex(), SHORTCUT_EDIT_LABEL, () -> {
                        log.debug("No process yet");
                    }),
                    new ShortcutDto(ShortcutFKeyEnum.F7.getIndex(), SHORTCUT_CREATE_LABEL, () -> {
                        log.debug("No process yet");
                    }),
                    new ShortcutDto(ShortcutFKeyEnum.F8.getIndex(), SHORTCUT_DELETE_LABEL, () -> {
                        log.debug("No process yet");
                    })
            ));
        }
        return shortcutDtos;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        public static DataGridView build(UI ui, UUID connectionId, String schemaName, String sql, boolean editable) throws UIException {
            ConnectionDto connectionDto = ListUtils.find(ui.getEnvironment().getConnections(), connectionId, ConnectionDto::getId)
                    .orElseThrow(() -> new UIException(String.format("Cannot find connection by id \"%s\"", connectionId)));
            try (Connector connector = ConnectorFactory.create(connectionDto, schemaName)) {
                ResultSetDto resultSetDto = connector.execute(sql);
                DataGridView dataGridView = new DataGridView(ui, connectionDto, schemaName, sql, resultSetDto.getSchema());
                dataGridView.setCellSelection(true);
                dataGridView.setEditable(editable);
                dataGridView.setRows(resultSetDto.getRows());
                dataGridView.setSelectAction(() -> {
                    int selectedRow = dataGridView.getSelectedRow();
                    String selectedItemValue = dataGridView.getTableModel().getRow(selectedRow).get(0);
                    try {
                        if (selectedRow == 0 && selectedItemValue.equals(BACK_ELEMENT_LABEL)) {
                            ui.getContentPanel().backOrSetContent(TableListView.Builder.build(ui, connectionId, schemaName));
                        } else if (!dataGridView.isCellSelection()) {
//                            dataGridView.setCellSelection(true);
                        } else {

                        }
                    } catch (UIException e) {
                        log.error(e.getMessage());
                        ui.getStatusPanel().setText(e.getMessage());
                    }
                });
                dataGridView.resize();
                return dataGridView;
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new UIException(String.format("Cannot build DataGrid by connection id \"%s\" and schema name \"%s\" and sql \"%s\"", connectionId, schemaName, sql));
            }
        }
    }
}
