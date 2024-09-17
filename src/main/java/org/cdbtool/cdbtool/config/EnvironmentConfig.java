package org.cdbtool.cdbtool.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.cdbtool.cdbtool.dtos.EnvironmentConfigDto;
import org.cdbtool.cdbtool.exceptions.ConfigException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnvironmentConfig {

    private final String filename;

    public EnvironmentConfig(String filename) {
        this.filename = filename;
    }

    public EnvironmentConfigDto load() throws ConfigException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Path filepath = Path.of(filename);
        if (Files.notExists(filepath)) {
            return new EnvironmentConfigDto();
        }
        try {
            return mapper.readValue(filepath.toFile(), EnvironmentConfigDto.class);
        } catch (IOException e) {
            throw new ConfigException("Cannot parse config file");
        }
    }

    public void save(EnvironmentConfigDto environmentConfigDto) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Path filepath = Path.of(filename);
        try {
            mapper.writeValue(filepath.toFile(), environmentConfigDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
