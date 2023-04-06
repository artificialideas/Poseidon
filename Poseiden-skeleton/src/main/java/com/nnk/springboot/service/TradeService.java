package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;

import java.util.List;
import java.util.Optional;

public interface TradeService {
    List<Trade> findAll();

    Optional<Trade> findById(Integer id);

    void save(Trade trade);

    void delete(Trade trade);
}