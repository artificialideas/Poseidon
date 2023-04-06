package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameServiceImpl implements RuleNameService{
    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Override
    public List<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }

    @Override
    public Optional<RuleName> findById(Integer id) {
        return ruleNameRepository.findById(id);
    }

    @Override
    public void save(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    @Override
    public void delete(RuleName ruleName) {
        ruleNameRepository.delete(ruleName);
    }
}
