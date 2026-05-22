package eSports_Arena_Manager.result_service.services;

import eSports_Arena_Manager.result_service.exceptions.ResultException;
import eSports_Arena_Manager.result_service.models.Result;
import eSports_Arena_Manager.result_service.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Result> findAll() {
        return this.resultRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Result findById(Long id) {
        return this.resultRepository.findById(id).orElseThrow(
                () -> new ResultException("resultado no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Result findByMatchId(Long matchId) {
        return this.resultRepository.findByMatchId(matchId).orElseThrow(
                () -> new ResultException("resultado de partido no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Result> findByEstado(String estado) {
        return this.resultRepository.findByEstado(estado);
    }

    @Transactional
    @Override
    public Result save(Result result) {
        // Validación de duplicados (Estilo GameService existsByName)
        if (this.resultRepository.existsByMatchId(result.getMatchId())) {
            throw new ResultException("ya existe un resultado para este partido");
        }

        if (result.getScoreA() < 0 || result.getScoreB() < 0) {
            throw new ResultException("el puntaje no puede ser negativo");
        }

        result.setEstado("FINALIZADO");

        this.calculateWinner(result);

        return this.resultRepository.save(result);
    }

    @Override
    public Result updateById(Long id, Result result) {
        return this.resultRepository.findById(id).map(r -> {
            r.setScoreA(result.getScoreA());
            r.setScoreB(result.getScoreB());
            r.setEstado(result.getEstado());
            r.setMatchId(result.getMatchId());
            r.setTeamAId(result.getTeamAId());
            r.setTeamBId(result.getTeamBId());

            this.calculateWinner(r);

            return this.resultRepository.save(r);
        }).orElseThrow(
                () -> new ResultException("resultado no encontrado")
        );
    }

    @Override
    public void deleteById(Long id) {
        this.resultRepository.deleteById(id);
    }

    /**
     * Método de apoyo para centralizar la lógica de negocio
     */git che
    private void calculateWinner(Result result) {
        if (result.getScoreA() > result.getScoreB()) {
            result.setWinnerId(result.getTeamAId());
        } else if (result.getScoreB() > result.getScoreA()) {
            result.setWinnerId(result.getTeamBId());
        } else {
            result.setWinnerId(null);
        }
    }
}