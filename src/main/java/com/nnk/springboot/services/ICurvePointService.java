package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;

import java.util.List;

public interface ICurvePointService {
    List<CurvePoint> findAll();
    CurvePoint save(CurvePoint curvePoint);
    CurvePoint findById(Integer id);
    void delete(Integer id);

}
