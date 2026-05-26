package eSports_Arena_Manager.result_service.services;

import eSports_Arena_Manager.result_service.clients.MatchClient;
import eSports_Arena_Manager.result_service.exceptions.ResultException;
import eSports_Arena_Manager.result_service.models.Result;
import eSports_Arena_Manager.result_service.models.dtos.MatchDTO;
import eSports_Arena_Manager.result_service.repositories.ResultRepository;
import eSports_Arena_Manager.result_service.services.ResultService;
import feign.FeignException; // <-- Captura el error exacto de OpenFeign
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private MatchClient matchClient;

    @Transactional(readOnly = true)
    @Override
    public List<Result> findAll() {
        return this.resultRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Result findById(Long id) {
        return this.resultRepository.findById(id).orElseThrow(
                () -> new ResultException("El resultado no existe")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Result findByPartidaId(Long partidaId) {
        return this.resultRepository.findByPartidaId(partidaId).orElseThrow(
                () -> new ResultException("No existe resultado para la partida")
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
        try{
            matchClient.findById(result.getPartidaId());
        }catch(FeignException e){
            throw new ResultException("la partida no existe");
        }

        if(resultRepository.existsByPartidaId(result.getPartidaId())){
            throw new ResultException("ya existe un resultaod para esa partida");
        }

        calcularGanador(result);
        return this.resultRepository.save(result);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        this.resultRepository.deleteById(id);
    }

    @Override
    public Result updateById(Long id, Result result) {
        return this.resultRepository.findById(id).map(r -> {
            if(r.getEstado().equals("VALIDADO")){
                throw new ResultException("un resultado valido no puede modificarse");
            }
            r.setScoreA(result.getScoreA());
            r.setScoreB(result.getScoreB());
            r.setEstado(result.getEstado());
            calcularGanador(r);
            return this.resultRepository.save(r);
        }).orElseThrow(
                () -> new ResultException("el resultado no existe")
        );
    }

    private void calcularGanador(Result result) {
        if (result.getScoreA() > result.getScoreB()) {
            result.setWinnerId(result.getTeamAId());
        } else if (result.getScoreB() > result.getScoreA()) {
            result.setWinnerId(result.getTeamBId());
        } else {
            result.setWinnerId(null);
        }
    }

}