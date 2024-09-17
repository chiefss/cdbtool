package org.cdbtool.cdbtool.ui.views;


import com.googlecode.lanterna.gui2.table.TableModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.dtos.ConnectionDto;
import org.cdbtool.cdbtool.dtos.ShortcutDto;
import org.cdbtool.cdbtool.enums.ShortcutFKeyEnum;
import org.cdbtool.cdbtool.exceptions.UIException;
import org.cdbtool.cdbtool.ui.UI;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
public class ConnectionListView extends AbstractListView<ConnectionDto> {

    public static final String SHORTCUT_EDIT_LABEL = "Edit";
    public static final String SHORTCUT_VIEW_LABEL = "View";
    public static final String SHORTCUT_MK_CNX_LABEL = "MkCnx";
    public static final String SHORTCUT_RM_CNX_LABEL = "RmCnx";

    private ConnectionListView(UI ui, String... columnLabels) {
        super(ui, columnLabels);
    }

    public void setRows(List<ConnectionDto> connectionDtos) {
        TableModel<String> tableModel = getTableModel();
        tableModel.clear();
        for (ConnectionDto connectionDto : connectionDtos) {
            tableModel.addRow(
                    Optional.ofNullable(connectionDto.getId()).map(UUID::toString).orElse(NULL_VALUE),
                    connectionDto.getName(),
                    String.valueOf(connectionDto.getAdapterId()),
                    connectionDto.getHost(),
                    String.valueOf(connectionDto.getPort()),
                    connectionDto.getDatabase(),
                    connectionDto.getUsername());
        }
    }

    @Override
    public List<ShortcutDto> getShortcuts() {
        return List.of(
                new ShortcutDto(ShortcutFKeyEnum.F3.getIndex(), SHORTCUT_VIEW_LABEL, () -> {
                    setCellSelection(!isCellSelection());
                    invalidate();
                }),
                new ShortcutDto(ShortcutFKeyEnum.F4.getIndex(), SHORTCUT_EDIT_LABEL, () -> {
                    int selectedRow = getSelectedRow();
                    UUID id;
                    try {
                        id = UUID.fromString(getTableModel().getRow(selectedRow).get(0));
                    } catch (IllegalArgumentException e) {
                        log.debug(e.getMessage());
                        getUi().getStatusPanel().setText("ID is not UUID");
                        return;
                    }
                    getUi().getEnvironment().getConnections()
                            .stream()
                            .filter(connectionDto -> Objects.nonNull(connectionDto.getId()) && connectionDto.getId().equals(id))
                            .findFirst()
                            .ifPresent(connectionDto -> {
                                ConnectionEditView component = ConnectionEditView.Builder.build(getUi(), connectionDto);
                                getUi().getHistoryList().add(this);
                                getUi().getContentPanel().setContent(component);
                            });
                }),
                new ShortcutDto(ShortcutFKeyEnum.F7.getIndex(), SHORTCUT_MK_CNX_LABEL, () -> getUi().getContentPanel().setContent(ConnectionEditView.Builder.build(getUi()))),
                new ShortcutDto(ShortcutFKeyEnum.F8.getIndex(), SHORTCUT_RM_CNX_LABEL, () -> {
                    int selectedRow = getSelectedRow();
                    UUID id;
                    try {
                        id = UUID.fromString(getTableModel().getRow(selectedRow).get(0));
                    } catch (IllegalArgumentException e) {
                        log.debug(e.getMessage());
                        getUi().getStatusPanel().setText("ID is not UUID");
                        return;
                    }
                    getTableModel().removeRow(selectedRow);
                    List<ConnectionDto> connections = getUi().getEnvironment().getConnections();
                    connections.removeIf(connectionDto -> Objects.nonNull(connectionDto.getId()) && connectionDto.getId().equals(id));
                    getUi().getConnectionUpdateObserver().update();
                })
        );
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        public static ConnectionListView build(UI ui, List<ConnectionDto> connectionDtos) {
            ConnectionListView connectionListView = new ConnectionListView(ui,"id", "name", "adapter_id", "host", "port", "database", "username");
            connectionListView.setCellSelection(false);
            connectionListView.setRows(connectionDtos);
            connectionListView.setSelectAction(() -> {
                UUID connectionId = UUID.fromString(connectionListView.getTableModel().getRow(connectionListView.getSelectedRow()).get(0));
                try {
                    ui.getHistoryList().add(connectionListView);
                    ui.getContentPanel().setContent(SchemaListView.Builder.build(ui, connectionId));
                } catch (UIException e) {
                    log.error(e.getMessage());
                    ui.getStatusPanel().setText(e.getMessage());
                }
            });
            connectionListView.resize();
            return connectionListView;
        }
    }
}
