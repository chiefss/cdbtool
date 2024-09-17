package org.cdbtool.cdbtool.ui.views;


import com.googlecode.lanterna.gui2.table.TableModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.connectors.Connector;
import org.cdbtool.cdbtool.connectors.ConnectorFactory;
import org.cdbtool.cdbtool.dtos.ConnectionDto;
import org.cdbtool.cdbtool.dtos.SchemaDto;
import org.cdbtool.cdbtool.dtos.ShortcutDto;
import org.cdbtool.cdbtool.enums.ShortcutFKeyEnum;
import org.cdbtool.cdbtool.exceptions.UIException;
import org.cdbtool.cdbtool.ui.UI;
import org.cdbtool.cdbtool.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
public class SchemaListView extends AbstractListView<SchemaDto> {

    public static final String SHORTCUT_VIEW_LABEL = "View";

    private SchemaListView(UI ui, String... columnLabels) {
        super(ui, columnLabels);
    }

    public void setRows(List<SchemaDto> schemaDtos) {
        TableModel<String> tableModel = getTableModel();
        tableModel.clear();
        tableModel.addRow(BACK_ELEMENT_LABEL);
        for (SchemaDto schemaDto : schemaDtos) {
            tableModel.addRow(schemaDto.getName());
        }
    }

    @Override
    public List<ShortcutDto> getShortcuts() {
        return List.of(
                new ShortcutDto(ShortcutFKeyEnum.F3.getIndex(), SHORTCUT_VIEW_LABEL, () -> {
                    setCellSelection(!isCellSelection());
                    invalidate();
                }));
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        public static SchemaListView build(UI ui, UUID connectionId) throws UIException {
            SchemaListView schemaListView = new SchemaListView(ui, "name");
            schemaListView.setCellSelection(true);
            List<SchemaDto> schemaDtos = new ArrayList<>();
            ConnectionDto connectionDto = ListUtils.find(ui.getEnvironment().getConnections(), connectionId, ConnectionDto::getId)
                    .orElseThrow(() -> new UIException(String.format("Cannot find connection by id \"%s\"", connectionId)));
            try (Connector connector = ConnectorFactory.create(connectionDto, null)) {
                schemaDtos = connector.getSchemas();
            } catch (Exception e) {
                log.error(e.getMessage());
                ui.getStatusPanel().setText(e.getMessage());
            }
            schemaListView.setRows(schemaDtos);
            schemaListView.setSelectAction(() -> {
                int selectedRow = schemaListView.getSelectedRow();
                String schemaName = schemaListView.getTableModel().getRow(selectedRow).get(0);
                try {
                    if (selectedRow == 0 && schemaName.equals(BACK_ELEMENT_LABEL)) {
                        ui.getContentPanel().backOrSetContent(ConnectionListView.Builder.build(ui, ui.getEnvironment().getConnections()));
                    } else {
                        ui.getHistoryList().add(schemaListView);
                        ui.getContentPanel().setContent(TableListView.Builder.build(ui, connectionId, schemaName));
                    }
                } catch (UIException e) {
                    log.error(e.getMessage());
                    ui.getStatusPanel().setText(e.getMessage());
                }
            });
            schemaListView.resize();
            return schemaListView;
        }
    }
}
