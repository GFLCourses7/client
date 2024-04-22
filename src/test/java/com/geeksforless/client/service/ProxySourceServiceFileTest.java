package com.geeksforless.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.ProxyCredentials;
import com.geeksforless.client.model.ProxyNetworkConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class ProxySourceServiceFileTest {

    @Mock
    private ObjectMapper objectMapper;

    private ProxySourceServiceFile proxySourceServiceFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String FILE = "ProxyConfigHolder_test.json";
        proxySourceServiceFile = new ProxySourceServiceFile(FILE, objectMapper);

    }

    @Test
    public void testGetProxies() throws IOException {
        ProxyConfigHolder proxyConfigHolder = createProxyConfigHolder();

        when(objectMapper.readValue(any(byte[].class), eq(ProxyConfigHolder[].class)))
                .thenReturn(new ProxyConfigHolder[]{proxyConfigHolder});

        List<ProxyConfigHolder> proxies = proxySourceServiceFile.getProxies();

        assertEquals(1, proxies.size());
        assertNotNull(proxies);
        assertEquals(List.of(proxyConfigHolder), proxies);
    }

    private static ProxyConfigHolder createProxyConfigHolder() {
        String hostname = "hostname";
        Integer port = 8080;
        String username = "username";
        String password = "password";

        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig(hostname, port);
        ProxyCredentials proxyCredentials = new ProxyCredentials(username, password);


        return new ProxyConfigHolder(proxyNetworkConfig, proxyCredentials);
    }
}
