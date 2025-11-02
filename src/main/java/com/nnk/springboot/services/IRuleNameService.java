package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;

import java.util.List;

public interface IRuleNameService {
    List<RuleName> findAll();
    RuleName save(RuleName ruleName);
    RuleName findById(Integer id);
    void delete(Integer id);
}
