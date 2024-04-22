package com.geeksforless.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforless.client.model.ProxyConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ProxySourceServiceFile implements ProxySourceService {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourceServiceFile.class);

    private final String PROXY_CONFIG_HOLDER_JSON;

    private final ObjectMapper objectMapper;

    public ProxySourceServiceFile(
            @Value("${client.proxy.file}") String PROXY_CONFIG_HOLDER_JSON,
            ObjectMapper objectMapper
    ) {
        this.PROXY_CONFIG_HOLDER_JSON = PROXY_CONFIG_HOLDER_JSON;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ProxyConfigHolder> getProxies() {

        // Look for ProxyConfigHolder.json inside /resources folder
        byte[] file = null;
        try {
            file = Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream(PROXY_CONFIG_HOLDER_JSON)
            ).readAllBytes();
            LOGGER.trace("Reading proxy config file: {}", PROXY_CONFIG_HOLDER_JSON);
            return Arrays.asList(objectMapper.readValue(file, ProxyConfigHolder[].class));

        } catch (IOException e) {
            LOGGER.error(e);
        }
        return Collections.emptyList();
    }
}
