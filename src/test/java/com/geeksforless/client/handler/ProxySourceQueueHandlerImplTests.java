package com.geeksforless.client.handler;

import com.geeksforless.client.handler.impl.ProxySourceQueueHandlerImpl;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.ProxyCredentials;
import com.geeksforless.client.model.ProxyNetworkConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProxySourceQueueHandlerImplTests {

    private ProxySourceQueueHandlerImpl queueHandler;

    @BeforeEach
    void setUp() {
        queueHandler = new ProxySourceQueueHandlerImpl();
    }

    @Test
    public void testAddProxy() {
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder(new ProxyNetworkConfig(), new ProxyCredentials());

        queueHandler.addProxy(proxyConfigHolder);

        assertEquals(1, queueHandler.getProxyQueue().size());
        assertEquals(proxyConfigHolder, queueHandler.getProxyQueue().peek());
    }

    @Test
    public void testGetProxy() throws InterruptedException {
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder(new ProxyNetworkConfig(), new ProxyCredentials());
        queueHandler.addProxy(proxyConfigHolder);

        ProxyConfigHolder proxy = queueHandler.getProxy();

        assertEquals(proxyConfigHolder, proxy);
    }
}
