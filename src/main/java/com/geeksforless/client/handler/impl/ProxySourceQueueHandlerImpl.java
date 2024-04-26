package com.geeksforless.client.handler.impl;

import com.geeksforless.client.exception.ProxyNotValidException;
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

    private final ProxyValidationService proxyValidationService;

    private final LinkedBlockingQueue<ProxyConfigHolder> proxyQueue = new LinkedBlockingQueue<>();

    public ProxySourceQueueHandlerImpl(ProxyValidationService proxyValidationService) {
        this.proxyValidationService = proxyValidationService;
    }

    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        if(proxyValidationService.isValid(proxyConfigHolder)){
            proxyQueue.add(proxyConfigHolder);
            LOGGER.trace("Added proxy to the queue: {}", proxyConfigHolder.toString());
        } else {
            LOGGER.error("Proxy not valid and not added to the queue");
            throw new ProxyNotValidException("Proxy not valid and not added to the queue");
        }
    }

    @Override
    public ProxyConfigHolder getProxy() {
        ProxyConfigHolder proxy = proxyQueue.poll();
        if (proxy != null)
            LOGGER.trace("Retrieved proxy from the queue: {}", proxy.toString());
        else
            LOGGER.trace("Proxy queue is empty, retrieve null");
        return proxy;
    }

    public LinkedBlockingQueue<ProxyConfigHolder> getProxyQueue() {
        return proxyQueue;
    }
}
