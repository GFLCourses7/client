package com.geeksforless.client.handler;

import com.geeksforless.client.model.ProxyConfigHolder;


public interface ProxySourceQueueHandler {

    void addProxy(ProxyConfigHolder proxyConfigHolder);

    ProxyConfigHolder getProxy() throws InterruptedException;
}
