package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

import java.util.List;

public interface IBidListService {
    List<BidList> findAll();
    BidList save(BidList bidList);
    BidList findById(Integer id);
    void delete(Integer id);
}
