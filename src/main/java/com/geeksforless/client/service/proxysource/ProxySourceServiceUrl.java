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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProxySourceServiceUrl implements ProxySourceService {
    private static final Logger LOGGER = LogManager.getLogger(ProxySourceServiceUrl.class);
    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;
    private final Request request;

    public ProxySourceServiceUrl(ObjectMapper objectMapper, OkHttpClient okHttpClient, Request request) {
        this.objectMapper = objectMapper;
        this.okHttpClient = okHttpClient;
        this.request = request;
    }

    @Override
    @Async
    @Scheduled(fixedRateString = "${client.proxy.url.fixedRate}")
    public List<ProxyConfigHolder> getProxies() {
        List<ProxyConfigHolder> proxyConfigHolderList = new ArrayList<>();
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
        Map<String, Object> configMap = objectMapper.readValue(response, new TypeReference<>() {
        });
        for (String s : configMap.keySet()) {
            if (s.contains("host")) {
                ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig();
                proxyNetworkConfig.setHostname(configMap.get(s).toString());
                if (configMap.containsKey("port")) {
                    proxyNetworkConfig.setPort(Integer.valueOf(configMap.get("port").toString()));
                }
                proxyConfigHolderList.add(new ProxyConfigHolder(proxyNetworkConfig, new ProxyCredentials()));
                LOGGER.info("ProxyConfigHolder with hostname " + proxyNetworkConfig.getHostname() + " and port " + proxyNetworkConfig.getPort() + " added");
            } else {
                LOGGER.error("Hostname or port not found when parsing");
                throw new ProxyNetworkParsingException("Hostname or port not found when parsing");
            }
        }
        return proxyConfigHolderList;
    }
}
