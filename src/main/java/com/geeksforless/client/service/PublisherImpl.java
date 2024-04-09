package com.geeksforless.client.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class PublisherImpl implements Publisher{

    private final String workerUrl;
    private final OkHttpClient client = new OkHttpClient();

    public PublisherImpl(@Value("${worker.url}")String workerUrl) {
        this.workerUrl = workerUrl;
    }

    @Override
    public void sendMessage() {
        Request request = new Request.Builder()
                .url(workerUrl + "/notify-scenario")
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
