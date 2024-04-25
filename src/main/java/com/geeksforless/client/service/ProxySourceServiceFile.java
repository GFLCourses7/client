package com.geeksforless.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.service.validation.ProxyValidationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProxySourceServiceFile implements ProxySourceService {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourceServiceFile.class);

    private final String PROXY_CONFIG_HOLDER_JSON;

    private final ObjectMapper objectMapper;

    private final ProxyValidationService proxyValidationService;

    public ProxySourceServiceFile(
            @Value("${client.proxy.file}") String PROXY_CONFIG_HOLDER_JSON,
            ObjectMapper objectMapper,
            ProxyValidationService proxyValidationService
    ) {
        this.PROXY_CONFIG_HOLDER_JSON = PROXY_CONFIG_HOLDER_JSON;
        this.objectMapper = objectMapper;
        this.proxyValidationService = proxyValidationService;
    }

    @Override
    public List<ProxyConfigHolder> getProxies() {
        List<ProxyConfigHolder> proxyConfigHolders = new ArrayList<>();
        // Look for ProxyConfigHolder.json inside /resources folder
        byte[] file = null;
        try {
            file = Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream(PROXY_CONFIG_HOLDER_JSON)
            ).readAllBytes();
            LOGGER.trace("Reading proxy config file: {}", PROXY_CONFIG_HOLDER_JSON);
            proxyConfigHolders = objectMapper.readValue(file, new TypeReference<>() {
            });

        } catch (IOException e) {
            LOGGER.error(e);
        }
        return proxyConfigHolders.stream().filter(proxyValidationService::isValid).toList();
    }
}
