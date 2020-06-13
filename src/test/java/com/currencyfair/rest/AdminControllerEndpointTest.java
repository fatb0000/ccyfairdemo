package com.currencyfair.rest;

import com.currencyfair.dto.TradeResponse;
import com.currencyfair.entity.Trade;
import com.currencyfair.repo.TradeRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
public class AdminControllerEndpointTest {

    EnhancedRandom random;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    private ObjectMapper om;
    @MockBean
    private TradeRepo repo;

    @Test
    public void testAdminListAllWithAdminRole() throws MalformedURLException {
        random = EnhancedRandomBuilder.aNewEnhancedRandom();
        Trade trade = random.nextObject(Trade.class);
        trade.setUserId("abc123");
        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(trade));
        TradeResponse[] response = restTemplate.withBasicAuth("admin", "admin").getForObject(
                new URL("http://localhost:" + port + "/admin/trade/list").toString(), TradeResponse[].class);
        Assert.assertEquals(trade.getUserId(), response[0].getUserId());
    }

    @Test
    public void testFailedAdminListAllWithUserRole() throws MalformedURLException {
        random = EnhancedRandomBuilder.aNewEnhancedRandom();
        Trade trade = random.nextObject(Trade.class);
        trade.setUserId("abc123");
        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(trade));
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("user", "user").getForEntity(
                new URL("http://localhost:" + port + "/admin/trade/list").toString(), String.class);
        Assert.assertEquals(403, responseEntity.getStatusCodeValue());

    }

    @Test
    public void testFailedAdminListAllWithoutAuth() throws MalformedURLException {
        random = EnhancedRandomBuilder.aNewEnhancedRandom();
        Trade trade = random.nextObject(Trade.class);
        trade.setUserId("abc123");
        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(trade));
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                new URL("http://localhost:" + port + "/admin/trade/list").toString(), String.class);
        Assert.assertEquals(401, responseEntity.getStatusCodeValue());

    }
}
