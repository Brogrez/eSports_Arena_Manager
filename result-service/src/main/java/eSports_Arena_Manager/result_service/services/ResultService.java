package eSports_Arena_Manager.result_service.services;

import eSports_Arena_Manager.result_service.models.Result;
import java.util.List;

public interface ResultService {
    List<Result> findAll();
    Result findById(Long id);
    Result findByMatchId(Long matchId);
    List<Result> findByEstado(String estado);
    Result save(Result result);
    void deleteById(Long id);
    Result updateById(Long id, Result result);
}
