package com.geeksforless.client.exception;

public class UsernameIsAlreadyExist extends RuntimeException {

    public UsernameIsAlreadyExist(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
