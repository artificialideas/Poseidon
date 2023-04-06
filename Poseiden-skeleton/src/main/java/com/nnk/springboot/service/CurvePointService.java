package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;

import java.util.List;
import java.util.Optional;

public interface CurvePointService {
    List<CurvePoint> findAll();

    Optional<CurvePoint> findById(Integer id);

    void save(CurvePoint curvePoint);

    void delete(CurvePoint curvePoint);
}