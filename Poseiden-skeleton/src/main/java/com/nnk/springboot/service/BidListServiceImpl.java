package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidListServiceImpl implements BidListService{
    @Autowired
    private BidListRepository bidListRepository;

    @Override
    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }

    @Override
    public Optional<BidList> findById(Integer id) {
        return bidListRepository.findById(id);
    }

    @Override
    public void save(BidList bidList) {
        bidListRepository.save(bidList);
    }

    @Override
    public void delete(BidList bidList) {
        bidListRepository.delete(bidList);
    }
}
