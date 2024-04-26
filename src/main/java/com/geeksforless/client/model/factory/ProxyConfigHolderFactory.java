package com.geeksforless.client.model.factory;

import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.ProxyCredentials;
import com.geeksforless.client.model.ProxyNetworkConfig;
import org.springframework.stereotype.Service;

@Service
public class ProxyConfigHolderFactory {

    public ProxyConfigHolder createEmpty() {
        return new ProxyConfigHolder(
                new ProxyNetworkConfig(),
                new ProxyCredentials()
        );
    }
}
