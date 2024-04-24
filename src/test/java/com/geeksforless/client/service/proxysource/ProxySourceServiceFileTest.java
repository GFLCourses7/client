package com.geeksforless.client.service.proxysource;

import com.geeksforless.client.model.ProxyConfigHolder;
import com.geeksforless.client.model.ProxyCredentials;
import com.geeksforless.client.model.ProxyNetworkConfig;

import com.geeksforless.client.service.JsonConfigReader;
import com.geeksforless.client.service.proxysource.ProxySourceServiceFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProxySourceServiceFileTest {

    private final String FILE = "ProxyConfigHolder_test.json";

    @Test
    public void testGetProxies1() {

        ProxySourceServiceFile proxySourceServiceFile = new ProxySourceServiceFile(new JsonConfigReader(), FILE);

        int expected = 4;
        int actual = proxySourceServiceFile.getProxies().size();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetProxy2() {

        String hostname = "hostname1";
        Integer port = 8080;
        String username = "username1";
        String password = "password1";

        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig(hostname, port);
        ProxyCredentials proxyCredentials = new ProxyCredentials(username, password);

        ProxySourceServiceFile proxySourceServiceFile = new ProxySourceServiceFile(new JsonConfigReader(), FILE);

        ProxyConfigHolder expected = new ProxyConfigHolder(proxyNetworkConfig, proxyCredentials);
        ProxyConfigHolder actual = proxySourceServiceFile.getProxies().get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetProxies3() {

        String hostname = "hostname3";
        Integer port = 8080;
        String username = "username3";
        String password = "password3";

        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig(hostname, port);
        ProxyCredentials proxyCredentials = new ProxyCredentials(username, password);

        ProxySourceServiceFile proxySourceServiceFile = new ProxySourceServiceFile(new JsonConfigReader(), FILE);

        ProxyConfigHolder expected = new ProxyConfigHolder(proxyNetworkConfig, proxyCredentials);
        ProxyConfigHolder actual = proxySourceServiceFile.getProxies().get(2);

        assertEquals(expected, actual);
    }
}
