package com.geeksforless.client.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class JsonConfigReader {


    private static final Logger LOGGER = LogManager.getLogger(JsonConfigReader.class.getName());

    public <T> List<T> readFile(byte[] configFile, Class<T> valueType) {
        LOGGER.info("Reading config file");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            return objectMapper.readValue(configFile, objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (IOException e) {
            String msg = "Error reading JSON file: " + new String(configFile);
            LOGGER.error(msg);
        }
        return null;
    }
}
