package com.geeksforless.client.handler;

import com.geeksforless.client.exception.ProxyNotValidException;
import com.geeksforless.client.handler.impl.ProxySourceQueueHandlerImpl;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.service.validation.ProxyValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProxySourceQueueHandlerImplTests {

    @Mock
    private ProxyValidationService proxyValidationService;

    @Mock
    private Queue<ProxyConfigHolder> proxyQueue;

    @InjectMocks
    private ProxySourceQueueHandlerImpl queueHandler;

    private ProxyConfigHolder validProxyConfigHolder;
    private ProxyConfigHolder invalidProxyConfigHolder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validProxyConfigHolder = new ProxyConfigHolder();
        invalidProxyConfigHolder = new ProxyConfigHolder();
    }


    @Test
    void testAddProxy_ValidProxy_SuccessfullyAdded() {
        when(proxyValidationService.isValid(validProxyConfigHolder)).thenReturn(true);

        queueHandler.addProxy(validProxyConfigHolder);

        assertEquals(queueHandler.getProxyQueue().peek(),validProxyConfigHolder);
    }

    @Test
    void testAddProxy_InvalidProxy_ExceptionThrown() {
        when(proxyValidationService.isValid(invalidProxyConfigHolder)).thenReturn(false);

        assertThrows(ProxyNotValidException.class, () -> queueHandler.addProxy(invalidProxyConfigHolder));

        assertEquals(0, queueHandler.getProxyQueue().size());
    }

    @Test
    public void testGetProxy() throws InterruptedException {
        when(proxyValidationService.isValid(validProxyConfigHolder)).thenReturn(true);
        queueHandler.addProxy(validProxyConfigHolder);

        ProxyConfigHolder proxy = queueHandler.getProxy();

        assertEquals(validProxyConfigHolder, proxy);
    }
}
