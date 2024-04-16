package com.geeksforless.client.service.validation;

import com.geeksforless.client.model.ProxyConfigHolder;

public interface ProxyValidationService {
    boolean isValid(ProxyConfigHolder configHolder);
}