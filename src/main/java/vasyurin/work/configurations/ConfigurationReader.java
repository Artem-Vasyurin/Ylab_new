package vasyurin.work.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.experimental.StandardException;
import vasyurin.work.dto.ConfigDto;

import java.io.FileInputStream;
import java.io.IOException;

public class ConfigurationReader {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static ConfigDto readConfiguration() {
        try(FileInputStream fis = new FileInputStream("./configuration.yaml")) {
            return mapper.readValue(fis, ConfigDto.class);

        } catch (IOException e) {
            throw new ConfigurationReaderException(e.getMessage(),e);
        }
    }

    @StandardException
    public static class ConfigurationReaderException extends RuntimeException {}
}
