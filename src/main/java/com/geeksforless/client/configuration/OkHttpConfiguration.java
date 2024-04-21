package com.geeksforless.client.configuration;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfiguration {

    @Value("${client.proxy.url.source}")
    private String SOURCE_URL;
    @Bean
    public OkHttpClient initClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(3, TimeUnit.SECONDS);
        client.setReadTimeout(3, TimeUnit.SECONDS);
        return client;
    }
    @Bean
    public Request createRequest() {
        return new Request.Builder()
                .url(SOURCE_URL)
                .build();
    }
}
