package com.geeksforless.client.handler.impl;

import com.geeksforless.client.handler.ProxySourceQueueHandler;
import com.geeksforless.client.model.ProxyConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class ProxySourceQueueHandlerImpl implements ProxySourceQueueHandler {
    private static final Logger LOGGER = LogManager.getLogger(ProxySourceQueueHandlerImpl.class);

    private final LinkedBlockingQueue<ProxyConfigHolder> proxyQueue = new LinkedBlockingQueue<>();

    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        proxyQueue.add(proxyConfigHolder);
        LOGGER.info("Added proxy to the queue: {}", proxyConfigHolder.toString());
    }

    @Override
    public ProxyConfigHolder getProxy() throws InterruptedException {
        ProxyConfigHolder proxy = proxyQueue.take();
        LOGGER.info("Retrieved proxy from the queue: {}", proxy.toString());
        return proxy;
    }

    public LinkedBlockingQueue<ProxyConfigHolder> getProxyQueue() {
        return proxyQueue;
    }
}
