package com.currencyfair.config;

import com.currencyfair.repo.TradeRepo;
import com.currencyfair.service.TradeProcessor;
import com.currencyfair.service.TradeProcessorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TradeConfig {

    @Bean
    public TradeProcessor tradeProcessor(@Autowired TradeRepo tradeRepo) {
        return new TradeProcessorImpl(tradeRepo);
    }
}
