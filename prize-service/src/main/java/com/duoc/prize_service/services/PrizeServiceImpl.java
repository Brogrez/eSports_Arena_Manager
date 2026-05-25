package com.duoc.prize_service.services;

import com.duoc.prize_service.clients.TournamentClient;
import com.duoc.prize_service.exceptions.PrizeException;
import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.models.dtos.PrizeSaveDTO;
import com.duoc.prize_service.models.dtos.TournamentDTO;
import com.duoc.prize_service.repositories.PrizeRepository;
import com.duoc.prize_service.services.PrizeService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrizeServiceImpl implements PrizeService {

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private TournamentClient tournamentClient;

    @Override
    public List<PrizeSaveDTO> findAll() {
        return prizeRepository.findAll().stream().map(p ->{
          PrizeSaveDTO prizeSaveDTO = new PrizeSaveDTO();
          prizeSaveDTO.setMonto(p.getMonto());

          prizeSaveDTO.setTorneoId(p.getTorneoId());
          try{
              TournamentDTO tournamentDTO = tournamentClient.findById(p.getTorneoId());

            }catch(FeignException e){
              e.getMessage();
          }
          TournamentDTO tournamentDTO = new TournamentDTO();
          tournamentDTO.setTorneoId(p.getTorneoId());
          prizeSaveDTO.setTorneoId(tournamentDTO.getTorneoId());
          prizeSaveDTO.setNombre(p.getNombre());
          prizeSaveDTO.setEstado(p.getEstado());
          return prizeSaveDTO;
        }).toList();


    }

    @Override
    @Transactional(readOnly = true)
    public Prize findById(Long id) {
        return prizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premio no encontrado con el ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prize> findByTorneoId(Long torneoId) {
        return prizeRepository.findByTorneoId(torneoId).stream().toList();
    }

    @Override
    @Transactional

    public Prize save(Prize prize) {
        try {
            TournamentDTO torneoExterno = tournamentClient.findById(prize.getTorneoId());
        } catch (FeignException e) {
            throw new PrizeException("Torneo no encontrado con el ID: " + prize.getTorneoId());
        }

            prize.setNombre(prize.getNombre());
            prize.setMonto(prize.getMonto());
            prize.setTorneoId(prize.getTorneoId());
            prize.setEstado(prize.getEstado());

            return prizeRepository.save(prize);
        }

    @Override
    public void deleteById(Long id) {
        if (!prizeRepository.existsById(id)) {
            throw new RuntimeException("Premio no encontrado con el ID: " + id);
        }
        prizeRepository.deleteById(id);
    }


    @Override
    public Prize update(Long id, PrizeSaveDTO prizeSaveDTO) {
        return this.prizeRepository.findById(id).map(prize -> {
            prize.setNombre(prizeSaveDTO.getNombre());
            prize.setMonto(prizeSaveDTO.getMonto());
            prize.setEstado(prizeSaveDTO.getEstado());
            return this.prizeRepository.save(prize);
        }).orElseThrow(
                () -> new RuntimeException("Premio no encontrado con el ID: " + id)
        );
    }
}