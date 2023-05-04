package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListServiceImpl implements BidListService {
    @Autowired
    private BidListRepository bidListRepository;

    @Override
    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }

    @Override
    public BidList findById(Integer id) {
        return bidListRepository.getOne(id);
    }

    @Override
    public void save(BidList bidList) {
        bidListRepository.saveAndFlush(bidList);
    }

    @Override
    public void delete(Iterable<BidList> bidList) {
        bidListRepository.deleteInBatch(bidList);
    }
}
