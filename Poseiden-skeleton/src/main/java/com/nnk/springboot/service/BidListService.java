package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;

import java.util.List;
import java.util.Optional;

public interface BidListService {
    List<BidList> findAll();

    Optional<BidList> findById(Integer id);

    void save(BidList bidList);

    void delete(BidList bidList);
}