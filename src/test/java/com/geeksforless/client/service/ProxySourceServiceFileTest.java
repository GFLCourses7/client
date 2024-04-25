package com.geeksforless.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.ProxyCredentials;
import com.geeksforless.client.model.ProxyNetworkConfig;
import com.geeksforless.client.service.validation.ProxyValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class ProxySourceServiceFileTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ProxyValidationService proxyValidationService;

    @InjectMocks
    private ProxySourceServiceFile proxySourceServiceFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String FILE = "ProxyConfigHolder_test.json";
        proxySourceServiceFile = new ProxySourceServiceFile(FILE, objectMapper, proxyValidationService);
    }

    @Test
    public void testGetProxies() throws IOException {

        List<ProxyConfigHolder> proxyConfigHolders = List.of(new ProxyConfigHolder(
                new ProxyNetworkConfig("hostname", 8080),
                new ProxyCredentials()
        ));


        when(objectMapper.readValue(any(byte[].class), any(TypeReference.class)))
                .thenReturn(proxyConfigHolders);
        when(proxyValidationService.isValid(any())).thenReturn(true);

        List<ProxyConfigHolder> proxies = proxySourceServiceFile.getProxies();

        assertNotNull(proxies);
        assertEquals(proxyConfigHolders, proxies);

    }
}
