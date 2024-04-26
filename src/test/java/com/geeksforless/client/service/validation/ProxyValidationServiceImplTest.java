package com.geeksforless.client.service.validation;

import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.ProxyCredentials;
import com.geeksforless.client.model.ProxyNetworkConfig;
import com.geeksforless.client.security.config.OkHttpBeanFactory;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProxyValidationServiceImplTest {

    @Mock
    private Response mockResponse;

    @Mock
    private OkHttpBeanFactory okHttpBeanFactory;

    @InjectMocks
    private ProxyValidationServiceImpl proxyValidationService;

    @Test
    public void isValid_WithValidProxy_ReturnsTrue() throws Exception {
        ProxyConfigHolder configHolder = new ProxyConfigHolder(new ProxyNetworkConfig("example.com", 8080), new ProxyCredentials());
        when(mockResponse.isSuccessful()).thenReturn(true);
        proxyValidationService = new ProxyValidationServiceImpl(okHttpBeanFactory, "validationUrl") {
            void createResponse(ProxyConfigHolder configHolder) {
                setResponse(mockResponse);
            }
        };
        boolean isValid = proxyValidationService.isValid(configHolder);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void isNotValid_WithInvalidProxy_ReturnsFalse() throws Exception {
        ProxyConfigHolder configHolder = new ProxyConfigHolder(new ProxyNetworkConfig("example.com", 8080), new ProxyCredentials());
        when(mockResponse.isSuccessful()).thenReturn(false);
        proxyValidationService = new ProxyValidationServiceImpl(okHttpBeanFactory, "validationUrl") {
            void createResponse(ProxyConfigHolder configHolder) {
                setResponse(mockResponse);
            }
        };
        boolean isValid = proxyValidationService.isValid(configHolder);
        assertFalse(isValid);
    }

    @Test
    public void isNotValid_WithNullProxyConfigHolder_ReturnsFalse() {
        boolean isValid = proxyValidationService.isValid(null);
        assertFalse(isValid);
    }

    @Test
    public void isNotValid_WithNullProxyConfigHolderGetHostName_ReturnsFalse() {
        ProxyConfigHolder configHolder = new ProxyConfigHolder(new ProxyNetworkConfig(null, 1), new ProxyCredentials());
        boolean isValid = proxyValidationService.isValid(configHolder);
        assertFalse(isValid);
    }

    @Test
    public void isNotValid_WithNullProxyConfigHolderGetPort_ReturnsFalse() {
        ProxyConfigHolder configHolder = new ProxyConfigHolder(new ProxyNetworkConfig("example.com", null), new ProxyCredentials());
        boolean isValid = proxyValidationService.isValid(configHolder);
        assertFalse(isValid);
    }
}
