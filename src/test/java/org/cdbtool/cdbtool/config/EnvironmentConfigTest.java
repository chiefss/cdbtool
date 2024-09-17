package org.cdbtool.cdbtool.config;

import org.cdbtool.cdbtool.dtos.EnvironmentConfigDto;
import org.cdbtool.cdbtool.exceptions.ConfigException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentConfigTest {

    private Path tmpFile;
    private EnvironmentConfig environmentConfig;

    @BeforeEach
    void setUp() throws IOException {
        tmpFile = Files.createTempFile("test-config", ".yml");
        environmentConfig = new EnvironmentConfig(tmpFile.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tmpFile);
    }

    @Test
    void testLoad_ConfigFileNotExists() throws ConfigException, IOException {
        Files.deleteIfExists(tmpFile);

        EnvironmentConfigDto environmentConfigDto = environmentConfig.load();

        assertNotNull(environmentConfigDto);
        assertNotNull(environmentConfigDto.getConnections());
    }

    @Test
    void testLoad_ConfigFileExists() throws IOException, ConfigException {
        String yamlContent = "connections: []\n";
        Files.writeString(tmpFile, yamlContent);

        EnvironmentConfigDto environmentConfigDto = environmentConfig.load();

        assertNotNull(environmentConfigDto);
        assertNotNull(environmentConfigDto.getConnections());
    }

    @Test
    void testLoad_ConfigFileParseException() throws IOException {
        Files.writeString(tmpFile, "key: value: invalid\n");

        ConfigException exception = assertThrows(ConfigException.class,
                () -> environmentConfig.load());

        assertEquals("Cannot parse config file", exception.getMessage());
    }

    @Test
    void testSave_Config() throws IOException {
        EnvironmentConfigDto environmentConfigDto = new EnvironmentConfigDto();
        environmentConfigDto.setConnections(new ArrayList<>());

        environmentConfig.save(environmentConfigDto);

        String content = Files.readString(tmpFile);
        assertTrue(content.contains("---\nconnections: []\n"));
    }
}