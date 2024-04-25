package com.geeksforless.client.service.proxysource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforless.client.exception.ProxyNetworkParsingException;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.ProxyCredentials;
import com.geeksforless.client.model.ProxyNetworkConfig;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service

public class ProxySourceServiceUrl implements ProxySourceService {
    private static final Logger LOGGER = LogManager.getLogger(ProxySourceServiceUrl.class);
    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;


    public ProxySourceServiceUrl(ObjectMapper objectMapper, OkHttpClient okHttpClient) {
        this.objectMapper = objectMapper;
        this.okHttpClient = okHttpClient;

    }

    @Override
    @Async
    @Scheduled(fixedRateString = "${client.proxy.url.fixedRate}")
    public List<ProxyConfigHolder> getProxies()  {
        List<ProxyConfigHolder> proxyConfigHolderList = new ArrayList<>();

        Request request = createRequest();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null && response.isSuccessful()) {
                String responseBody = response.body().string();
                List<ProxyConfigHolder> proxiesFromResponse = parseProxyConfig(responseBody);
                proxyConfigHolderList.addAll(proxiesFromResponse);
            }
        } catch (IOException e) {
            LOGGER.error("Exception when receiving proxy from URL");
            throw new RuntimeException(e);
        }
        return proxyConfigHolderList;
    }

    private List<ProxyConfigHolder> parseProxyConfig(String response) throws JsonProcessingException {
        List<ProxyConfigHolder> proxyConfigHolderList = new ArrayList<>();
        Map<String, Object> configMap = objectMapper.readValue(response, new TypeReference<>() {});

        String host = null;
        Integer port = null;

        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.contains("host")) {
                host = value.toString();
            } else if (key.contains("port")) {
                port = Integer.valueOf(value.toString());
            }
        }
        if (host != null && port != null) {
            ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig();
            proxyNetworkConfig.setHostname(host);
            proxyNetworkConfig.setPort(port);

            proxyConfigHolderList.add(new ProxyConfigHolder(proxyNetworkConfig, new ProxyCredentials()));
            LOGGER.info("ProxyConfigHolder with hostname " + host + " and port " + port + " added");
        } else {
            LOGGER.error("Hostname or port not found when parsing");
            throw new ProxyNetworkParsingException("Hostname or port not found when parsing");
        }

        return proxyConfigHolderList;
    }

    private Request createRequest(){
        URL url;
        try {
            url = new URL("https://www.abcproxy.com/getproxy/api.html");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return new Request.Builder()
                .get()
                .url(url)
                .build();
    }
}
