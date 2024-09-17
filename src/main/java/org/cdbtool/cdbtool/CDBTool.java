package org.cdbtool.cdbtool;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.cdbtool.cdbtool.config.EnvironmentConfig;
import org.cdbtool.cdbtool.dtos.EnvironmentConfigDto;
import org.cdbtool.cdbtool.dtos.ConnectionDto;
import org.cdbtool.cdbtool.exceptions.ConfigException;
import org.cdbtool.cdbtool.observers.ConnectionUpdateListener;
import org.cdbtool.cdbtool.ui.UI;
import org.cdbtool.cdbtool.ui.views.ConnectionListView;

import java.io.IOException;
import java.util.List;

@Getter
@Log4j2
public class CDBTool implements ConnectionUpdateListener {

    public static final String CONFIG_FILENAME = "config.yml";
    private final EnvironmentConfig environmentConfig;
    private EnvironmentConfigDto environmentConfigDto;
    private final UI ui;
    private final Environment environment;

    CDBTool() throws IOException {
        environmentConfig = new EnvironmentConfig(CONFIG_FILENAME);
        environment = new Environment();
        ui = new UI();
    }

    public void loadConfig() throws ConfigException {
        environmentConfigDto = environmentConfig.load();
        environment.setConnections(environmentConfigDto.getConnections());
    }

    public void initUi() throws IOException {
        ui.setEnvironment(environment);
        ui.init();
        ui.initListeners();
        ui.getConnectionUpdateObserver().addListener(this);
        ui.resize();
    }

    public void initDefaultContent() {
        ui.getContentPanel().setContent(ConnectionListView.Builder.build(ui, environment.getConnections()));
    }

    public void startUi() {
        ui.initGui();
    }

    @Override
    public void onConnectionUpdated() {
        List<ConnectionDto> connections = environment.getConnections();
        environmentConfigDto.setConnections(connections);
        environmentConfig.save(environmentConfigDto);
    }
}