package com.geeksforless.client.handler.impl;

import com.geeksforless.client.handler.ProxySourceQueueHandler;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.service.validation.ProxyValidationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ProxySourceQueueHandlerImpl implements ProxySourceQueueHandler {
    private static final Logger LOGGER = LogManager.getLogger(ProxySourceQueueHandlerImpl.class);

    private final LinkedBlockingQueue<ProxyConfigHolder> proxyQueue = new LinkedBlockingQueue<>();
    private final ProxyValidationService proxyValidationService;

    public ProxySourceQueueHandlerImpl(ProxyValidationService proxyValidationService) {
        this.proxyValidationService = proxyValidationService;
    }

    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        boolean valid = proxyValidationService.isValid(proxyConfigHolder);
        if (!valid) {
            LOGGER.error("Proxy validation failed for {}", proxyConfigHolder);
            return;
        }
        proxyQueue.add(proxyConfigHolder);
        LOGGER.info("Added proxy to the queue: {}", proxyConfigHolder.toString());
    }

    @Override
    public ProxyConfigHolder getProxy() {
        LOGGER.info("Taking proxy from the queue.");
        return proxyQueue.poll();
    }

    public LinkedBlockingQueue<ProxyConfigHolder> getProxyQueue() {
        return proxyQueue;
    }
}
