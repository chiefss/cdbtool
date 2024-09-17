package org.cdbtool.cdbtool.ui.views;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.cdbtool.cdbtool.dtos.ConnectionDto;
import org.cdbtool.cdbtool.dtos.ShortcutDto;
import org.cdbtool.cdbtool.enums.AdapterEnum;
import org.cdbtool.cdbtool.enums.ShortcutFKeyEnum;
import org.cdbtool.cdbtool.ui.UI;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
public class ConnectionEditView extends AbstractView {

    public static final String BUTTON_ADD_LABEL = "Add";
    public static final String BUTTON_EDIT_LABEL = "Edit";
    public static final String NEW_RECORD_EMPTY_ID_LABEL = "<new>";
    public static final String SHORTCUT_CLOSE_LABEL = "Close";
    @Setter
    private ConnectionDto connectionDto;

    private ConnectionEditView(UI ui) {
        super(ui);
    }

    @Override
    public List<ShortcutDto> getShortcuts() {
        return List.of(
                new ShortcutDto(ShortcutFKeyEnum.F4.getIndex(), SHORTCUT_CLOSE_LABEL,
                        () -> getUi().getContentPanel().backOrSetContent(ConnectionListView.Builder.build(getUi(), getUi().getEnvironment().getConnections())))
        );
    }

    public void render() {
        setLayoutManager(new GridLayout(2));

        final Label textBoxId = new Label(Objects.isNull(connectionDto.getId()) ? NEW_RECORD_EMPTY_ID_LABEL : connectionDto.getId().toString());
        final TextBox textBoxName = new TextBox(Optional.ofNullable(connectionDto.getName()).orElse(Strings.EMPTY));
        final RadioBoxList<String> textBoxAdapter = new RadioBoxList<>();
        for (AdapterEnum value : AdapterEnum.values()) {
            String adapterName = value.getName();
            textBoxAdapter.addItem(adapterName);
            if (connectionDto.getAdapterId() == value.getId()) {
                textBoxAdapter.setCheckedItem(adapterName);
            }
        }
        final TextBox textBoxHost = new TextBox(Optional.ofNullable(connectionDto.getHost()).orElse(Strings.EMPTY));
        final TextBox textBoxPort = new TextBox(connectionDto.getPort() == 0 ? Strings.EMPTY : String.valueOf(connectionDto.getPort()));
        final TextBox textBoxDatabase = new TextBox(Optional.ofNullable(connectionDto.getDatabase()).orElse(Strings.EMPTY));
        final TextBox textBoxUsername = new TextBox(Optional.ofNullable(connectionDto.getUsername()).orElse(Strings.EMPTY));
        final TextBox textBoxPassword = new TextBox(Optional.ofNullable(connectionDto.getPassword()).orElse(Strings.EMPTY));
        Button addButton = new Button(Objects.isNull(connectionDto.getId()) ? BUTTON_ADD_LABEL : BUTTON_EDIT_LABEL, () -> {
            connectionDto.setName(textBoxName.getText());
            try {
                connectionDto.setAdapterId(AdapterEnum.findByName(textBoxAdapter.getCheckedItem()).getId());
            } catch (Exception e) {
                log.debug(e.getMessage());
                getUi().getStatusPanel().setText(e.getMessage());
                return;
            }
            connectionDto.setHost(textBoxHost.getText());
            connectionDto.setPort(Integer.parseInt(textBoxPort.getText()));
            connectionDto.setDatabase(textBoxDatabase.getText());
            connectionDto.setUsername(textBoxUsername.getText());
            connectionDto.setPassword(textBoxPassword.getText());

            List<ConnectionDto> connections = getUi().getEnvironment().getConnections();
            if (Objects.isNull(connectionDto.getId())) {
                connectionDto.setId(UUID.randomUUID());
                connections.add(connectionDto);
            } else {
                for (int i = 0; i < connections.size(); i++) {
                    ConnectionDto cDto = connections.get(i);
                    if (Objects.equals(cDto.getId(), connectionDto.getId())) {
                        connections.set(i, connectionDto);
                    }
                }
            }

            getUi().getConnectionUpdateObserver().update();
            getUi().getContentPanel().setContent(ConnectionListView.Builder.build(getUi(), connections));
        });

        addComponent(new Label("ID"));
        addComponent(textBoxId);
        addComponent(new Label("Name"));
        addComponent(textBoxName);
        addComponent(new Label("Adapter"));
        addComponent(textBoxAdapter);
        addComponent(new Label("Host"));
        addComponent(textBoxHost);
        addComponent(new Label("Port"));
        addComponent(textBoxPort);
        addComponent(new Label("Username"));
        addComponent(textBoxUsername);
        addComponent(new Label("Password"));
        addComponent(textBoxPassword);
        addComponent(new Label("Database"));
        addComponent(textBoxDatabase);
        addComponent(new EmptySpace(new TerminalSize(0, 0)));
        addComponent(addButton);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        public static ConnectionEditView build(UI ui) {
            ConnectionEditView connectionEditView = new ConnectionEditView(ui);
            connectionEditView.setConnectionDto(new ConnectionDto());
            connectionEditView.render();
            return connectionEditView;
        }

        public static ConnectionEditView build(UI ui, ConnectionDto connectionDto) {
            ConnectionEditView connectionEditView = new ConnectionEditView(ui);
            connectionEditView.setConnectionDto(connectionDto);
            connectionEditView.render();
            return connectionEditView;
        }
    }
}
