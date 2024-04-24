package com.geeksforless.client.service.proxysource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeksforless.client.exception.ProxyNetworkParsingException;
import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.ProxyCredentials;
import com.geeksforless.client.model.ProxyNetworkConfig;
import com.squareup.okhttp.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProxySourceServiceUrlTest {

    @Mock
    private OkHttpClient okHttpClientMock;

    @Mock
    private Call callMock;

    @Mock
    private Response responseMock;

    @Mock
    private ObjectMapper objectMapperMock;

    @InjectMocks
    private ProxySourceServiceUrl proxySourceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testGetProxies_SuccessfulResponseParsing() throws IOException {
        String jsonResponse = "{\"host\":\"example.com\",\"port\":8080}";
        List<ProxyConfigHolder> expectedProxies = Collections.singletonList(new ProxyConfigHolder(
                new ProxyNetworkConfig("example.com", 8080), new ProxyCredentials()));

        Map<String, Object> mockConfigMap = new HashMap<>();
        mockConfigMap.put("host", "example.com");
        mockConfigMap.put("port", 8080);

        when(okHttpClientMock.newCall(any(Request.class))).thenReturn(callMock);
        when(callMock.execute()).thenReturn(responseMock);
        when(responseMock.isSuccessful()).thenReturn(true);
        when(responseMock.body()).thenReturn(ResponseBody.create(MediaType.parse("application/json"), jsonResponse));
        when(objectMapperMock.readValue(eq(jsonResponse), any(TypeReference.class))).thenReturn(mockConfigMap);

        List<ProxyConfigHolder> actualProxies = proxySourceService.getProxies();

        assertEquals(expectedProxies, actualProxies);
    }

    @Test
    public void testParseProxyConfig_ThrowsException() throws IOException {

        Map<String, Object> mockConfigMap = new HashMap<>();
        mockConfigMap.put("invalid", "data");

        String responseBody = "{\"invalid\": \"data\"}"; // invalid JSON response
        when(okHttpClientMock.newCall(any(Request.class))).thenReturn(callMock);
        when(callMock.execute()).thenReturn(responseMock);
        when(responseMock.isSuccessful()).thenReturn(true);
        when(responseMock.body()).thenReturn(ResponseBody.create(MediaType.parse("application/json"), responseBody));
        when(objectMapperMock.readValue(eq(responseBody), any(TypeReference.class))).thenReturn(mockConfigMap);


        assertThrows(ProxyNetworkParsingException.class, () -> proxySourceService.getProxies());
    }
}
