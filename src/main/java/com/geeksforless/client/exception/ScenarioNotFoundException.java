package com.geeksforless.client.exception;

public class ScenarioNotFoundException extends RuntimeException {
    public ScenarioNotFoundException(String s, Long id) {
        super(s);
    }
}
