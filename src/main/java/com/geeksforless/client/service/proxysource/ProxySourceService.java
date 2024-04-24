package com.geeksforless.client.service.proxysource;

import com.geeksforless.client.model.ProxyConfigHolder;

import java.util.List;

public interface ProxySourceService {
    List<ProxyConfigHolder> getProxies();
}
