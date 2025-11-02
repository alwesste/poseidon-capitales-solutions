package com.nnk.springboot.services;


import com.nnk.springboot.domain.Trade;

import java.util.List;

public interface ITradeService {
    List<Trade> findAll();
    Trade save(Trade trade);
    Trade findById(Integer id);
    void delete(Integer id);
}
