package vasyurin.work.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.experimental.StandardException;
import vasyurin.work.dto.ConfigDto;

import java.io.FileInputStream;
import java.io.IOException;

public class ConfigurationReader {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private static final String CONFIG_PATH = "./configuration.yaml";


    public static ConfigDto readConfiguration() {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            return mapper.readValue(fis, ConfigDto.class);
        } catch (IOException e) {
            throw new ConfigurationReaderException(e.getMessage(), e);
        }
    }

    @StandardException
    public static class ConfigurationReaderException extends RuntimeException {
    }
}
