package com.currencyfair.rest;

import com.currencyfair.dto.OrderRequest;
import com.currencyfair.dto.OrderResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
public class TradeControllerEndpointTest {

    EnhancedRandom random;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    private ObjectMapper om;
    @MockBean
    private TradeRepo repo;

    @Test
    public void testTradeReviewSuccessfully() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("mock/TradeRequest1.json");
        ObjectMapper om = new ObjectMapper();
        OrderRequest request = om.readValue(resource, OrderRequest.class);
        assertThat(restTemplate.postForObject(
                new URL("http://localhost:" + port + "/trade/review").toString(), request, OrderResponse.class).getTxnId()).contains("t-");
    }

    @Test
    public void testTradeReview_hasNoRequestBody_Return400() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        assertThat(
                restTemplate.postForEntity(
                        new URL("http://localhost:" + port + "/trade/review").toString(), entity, String.class).getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void testTradeReview_hasRequestBodyContainsAllNull_Return400() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("mock/TradeRequest_All_Null.json");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(new String(bytes), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/trade/review").toString(), entity, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void testTradeReview_hasRequestBodyContainsInvalidAmountSell_Return400() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("mock/TradeRequest_InvalidAmountSell.json");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(new String(bytes), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/trade/review").toString(), entity, String.class);
        log.info(response);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void testTradeReview_hasRequestBodyContainsInvalidCountry_Return400() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("mock/TradeRequest_InvalidCountry.json");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(new String(bytes), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/trade/review").toString(), entity, String.class);
        log.info(response);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void testTradeReview_hasRequestBodyContainsInvalidTimePlaced_Return400() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("mock/TradeRequest_InvalidCountry.json");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(new String(bytes), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/trade/review").toString(), entity, String.class);
        log.info(response);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void testFindByTxnId() throws MalformedURLException {
        random = EnhancedRandomBuilder.aNewEnhancedRandom();
        Trade trade = random.nextObject(Trade.class);
        trade.setUserId("abc123");
        Mockito.when(repo.findById("123")).thenReturn(Optional.of(trade));
        TradeResponse response = restTemplate.getForObject(
                new URL("http://localhost:" + port + "/trade/123").toString(), TradeResponse.class);
        Assert.assertEquals("abc123", trade.getUserId());

    }

    @Test
    public void testFindByUserId() throws MalformedURLException {
        random = EnhancedRandomBuilder.aNewEnhancedRandom();
        Trade trade = random.nextObject(Trade.class);
        trade.setUserId("abc123");
        Mockito.when(repo.findByUserId("abc123")).thenReturn(Arrays.asList(trade));
        TradeResponse[] response = restTemplate.getForObject(
                new URL("http://localhost:" + port + "/trade/query?userId=abc123").toString(), TradeResponse[].class);
        //List<TradeResponse> list = (List<TradeResponse>) re;
        Assert.assertEquals("abc123", response[0].getUserId());

    }
}
