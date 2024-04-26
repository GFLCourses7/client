package com.geeksforless.client.security.config;

import com.squareup.okhttp.OkHttpClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class OkHttpBeanFactory implements FactoryBean<OkHttpClient> {

    @Override
    public OkHttpClient getObject() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return OkHttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
