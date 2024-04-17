package com.geeksforless.client.handler;

import com.geeksforless.client.handler.impl.ProxySourceQueueHandlerImpl;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.ProxyCredentials;
import com.geeksforless.client.model.ProxyNetworkConfig;
import com.geeksforless.client.service.validation.ProxyValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProxySourceQueueHandlerImplTests {

    private ProxySourceQueueHandlerImpl queueHandler;

    private ProxyConfigHolder proxyConfigHolder;
    @Mock
    private ProxyValidationService proxyValidationService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queueHandler = new ProxySourceQueueHandlerImpl(proxyValidationService);
        proxyConfigHolder = new ProxyConfigHolder(new ProxyNetworkConfig(), new ProxyCredentials());
    }

    @Test
    public void testAddProxy() {
        when(proxyValidationService.isValid(any(ProxyConfigHolder.class))).thenReturn(true);

        queueHandler.addProxy(proxyConfigHolder);

        assertEquals(1, queueHandler.getProxyQueue().size());
        assertEquals(proxyConfigHolder, queueHandler.getProxyQueue().peek());

        verify(proxyValidationService, times(1)).isValid(proxyConfigHolder);
    }

    @Test
    public void testGetProxy() {

        when(proxyValidationService.isValid(any(ProxyConfigHolder.class))).thenReturn(true);

        queueHandler.addProxy(proxyConfigHolder);

        ProxyConfigHolder proxy = queueHandler.getProxy();

        assertEquals(proxyConfigHolder, proxy);
        verify(proxyValidationService, times(1)).isValid(proxyConfigHolder);
    }
}
