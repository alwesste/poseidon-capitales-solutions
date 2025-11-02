package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;

import java.util.List;

public interface IRatingService {
    List<Rating> findAll();
    Rating save(Rating rating);
    Rating findById(Integer id);
    void delete(Integer id);
}
