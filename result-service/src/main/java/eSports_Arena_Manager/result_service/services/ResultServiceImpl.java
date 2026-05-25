package eSports_Arena_Manager.result_service.services;

import eSports_Arena_Manager.result_service.clients.MatchClient;
import eSports_Arena_Manager.result_service.exceptions.ResultException;
import eSports_Arena_Manager.result_service.models.Result;
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
                () -> new ResultException("EL RESULTADO NO EXISTE")
        );
    }

    @Transactional
    @Override
    public Result save(Result result) {
        // Validación cruzada con OpenFeign usando el bloque try-catch oficial de tu grupo
        try {
            matchClient.findById(result.getPartidaId());
        } catch (FeignException e) {
            throw new ResultException("LA PARTIDA NO EXISTE EN EL SISTEMA");
        }

        result.setEstado("PROCESADO");
        return this.resultRepository.save(result);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Result result = this.resultRepository.findById(id).orElseThrow(
                () -> new ResultException("EL RESULTADO NO EXISTE")
        );
        this.resultRepository.delete(result);
    }

    @Transactional
    @Override
    public Result updateById(Long id, Result result) {
        return this.resultRepository.findById(id).map(r -> {
            r.setPuntaje(result.getPuntaje());
            r.setGanador(result.getGanador());
            r.setEstado(result.getEstado());
            return this.resultRepository.save(r);
        }).orElseThrow(
                () -> new ResultException("EL RESULTADO NO EXISTE")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Result> findByEstado(String estado) {
        return this.resultRepository.findByEstado(estado);
    }
}