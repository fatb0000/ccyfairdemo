package com.currencyfair.repo;

import com.currencyfair.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepo extends JpaRepository<Trade, String> {
    List<Trade> findAll();

    List<Trade> findByUserId(String userId);
}
