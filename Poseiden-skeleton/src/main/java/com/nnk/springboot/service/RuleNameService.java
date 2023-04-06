package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;

import java.util.List;
import java.util.Optional;

public interface RuleNameService {
    List<RuleName> findAll();

    Optional<RuleName> findById(Integer id);

    void save(RuleName ruleName);

    void delete(RuleName ruleName);
}