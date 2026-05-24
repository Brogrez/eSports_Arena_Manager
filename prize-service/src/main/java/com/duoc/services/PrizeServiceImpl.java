package com.duoc.services;

import com.duoc.exceptions.PrizeException;
import com.duoc.models.Prize;
import com.duoc.repositories.PrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrizeServiceImpl implements PrizeService {

    @Autowired
    private PrizeRepository prizeRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Prize> findAll() {
        return this.prizeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Prize findById(Long id) {
        return this.prizeRepository.findById(id).orElseThrow(
                () -> new PrizeException("premio no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Prize> findByName(String name) {
        return this.prizeRepository.findByName(name)
                .map(List::of)
                .orElseThrow(() -> new PrizeException("name no encontrado"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Prize> findByEstado(String estado) {
        List<Prize> premios = this.prizeRepository.findByEstado(estado);
        if (premios.isEmpty()) {
            throw new PrizeException("estados no encontrado");
        }
        return premios;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Prize> findByTorneoId(Long torneoId) {
        return this.prizeRepository.findByTorneoId(torneoId);
    }

    @Transactional
    @Override
    public Prize save(Prize prize) {
        if (this.prizeRepository.existsByName(prize.getName())) {
            throw new PrizeException("ya existe este premio");
        }
        if (prize.getValor() < 0) {
            throw new PrizeException("el valor del premio debe ser mayor o igual a 0");
        }
        prize.setEstado("DISPONIBLE");
        return this.prizeRepository.save(prize);
    }

    @Override
    public void deleteById(Long id) {
        this.prizeRepository.deleteById(id);
    }

    @Override
    public Prize updateById(Long id, Prize prize) {
        return this.prizeRepository.findById(id).map(e -> {
            if (prize.getValor() < 0) {
                throw new PrizeException("el valor del premio debe ser mayor o igual a 0");
            }
            e.setName(prize.getName());
            e.setDescripcion(prize.getDescripcion());
            e.setValor(prize.getValor());
            e.setTorneoId(prize.getTorneoId());
            e.setEstado(prize.getEstado());
            return this.prizeRepository.save(e);
        }).orElseThrow(
                () -> new PrizeException("premio no encontrado")
        );
    }
}