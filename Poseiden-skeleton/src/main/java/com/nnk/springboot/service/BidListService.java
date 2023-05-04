package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;

import java.util.List;

public interface BidListService {
    List<BidList> findAll();

    BidList findById(Integer id);

    void save(BidList bidList);

    void delete(Iterable<BidList> bidList);
}