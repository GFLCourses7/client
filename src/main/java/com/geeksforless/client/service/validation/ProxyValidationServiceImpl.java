package com.geeksforless.client.service.validation;

import com.geeksforless.client.model.ProxyConfigHolder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Service
public class ProxyValidationServiceImpl implements ProxyValidationService {

    private static final Logger logger = LogManager.getLogger(ProxyValidationServiceImpl.class);

    @Value("${client.proxy.validation.url}")
    private String validationUrl;
    private Response response;

    @Override
    public boolean isValid(ProxyConfigHolder configHolder) {
        if (configHolder == null ||
                configHolder.getProxyNetworkConfig().getHostname() == null ||
                configHolder.getProxyNetworkConfig().getPort() == null) {
            logger.warn("ProxyConfigHolder is null or not specified hostname and port");
            return false;
        }
        try {
            createResponse(configHolder);
        } catch (IOException e) {
            logger.error("Error while validating proxy: " + e.getMessage());
            return false;
        }
        return response.isSuccessful();
    }

    private Proxy createProxy(String hostname, Integer port) {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, port));
    }

    private Request createRequest() {
        return new Request.Builder()
                .url(validationUrl)
                .build();
    }

    void createResponse(ProxyConfigHolder configHolder) throws IOException {
        Proxy proxy = createProxy(configHolder.getProxyNetworkConfig().getHostname(),
                configHolder.getProxyNetworkConfig().getPort());
        OkHttpClient client = new OkHttpClient();
        client.setProxy(proxy);
        client.setConnectTimeout(1, TimeUnit.SECONDS);
        client.setReadTimeout(1, TimeUnit.SECONDS);
        response = client.newCall(createRequest()).execute();
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}