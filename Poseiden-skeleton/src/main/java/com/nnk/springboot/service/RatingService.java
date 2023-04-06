package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    List<Rating> findAll();

    Optional<Rating> findById(Integer id);

    void save(Rating rating);

    void delete(Rating rating);
}