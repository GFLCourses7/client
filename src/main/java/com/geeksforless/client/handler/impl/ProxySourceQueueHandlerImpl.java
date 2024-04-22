package com.geeksforless.client.handler.impl;

import com.geeksforless.client.handler.ProxySourceQueueHandler;
import com.geeksforless.client.model.ProxyConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ProxySourceQueueHandlerImpl implements ProxySourceQueueHandler {
    private static final Logger LOGGER = LogManager.getLogger(ProxySourceQueueHandlerImpl.class);

    private final LinkedBlockingQueue<ProxyConfigHolder> proxyQueue = new LinkedBlockingQueue<>();

    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        proxyQueue.add(proxyConfigHolder);
        LOGGER.trace("Added proxy to the queue: {}", proxyConfigHolder.toString());
    }

    @Override
    public ProxyConfigHolder getProxy() {
        ProxyConfigHolder proxy = proxyQueue.poll();
        if (proxy != null)
            LOGGER.info("Retrieved proxy from the queue: {}", proxy.toString());
        else
            LOGGER.info("Proxy queue is empty, retrieve null");
        return proxy;
    }

    public LinkedBlockingQueue<ProxyConfigHolder> getProxyQueue() {
        return proxyQueue;
    }
}
