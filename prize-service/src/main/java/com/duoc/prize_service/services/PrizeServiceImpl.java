package com.duoc.prize_service.services;

import com.duoc.prize_service.clients.TournamentClient;
import com.duoc.prize_service.clients.RankingClient;
import com.duoc.prize_service.exceptions.PrizeException;
import com.duoc.prize_service.models.PremioAsignado;
import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.models.dtos.TournamentDTO;
import com.duoc.prize_service.repositories.PremioAsignadoRepository;
import com.duoc.prize_service.repositories.PrizeRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrizeServiceImpl implements PrizeService {

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private PremioAsignadoRepository premioAsignadoRepository;

    @Autowired
    private TournamentClient tournamentClient;

    @Autowired
    private RankingClient rankingClient;

    @Transactional(readOnly = true)
    @Override
    public List<Prize> findAll() {
        return this.prizeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Prize findById(Long id) {
        return this.prizeRepository.findById(id).orElseThrow(
                () ->new PrizeException("premio no existe")
        );
    }

    @Transactional
    @Override
    public Prize save(Prize prize) {
        try{
            TournamentDTO torneo = tournamentClient.findById(prize.getTorneoId());
        }catch (FeignException e){
            throw new PrizeException("el torneo no existe");
        }

        if(prizeRepository.existsByTorneoIdAndPosicion(prize.getTorneoId(), prize.getPosicion())){
            throw new PrizeException("ya existe un premio para la posicion ");
        }

        if (prize.getValor() < 0) {
            throw new PrizeException("El valor del premio no puede ser negativo");
        }

        prize.setEstado("DISPONIBLE");
        return this.prizeRepository.save(prize);
    }

    @Transactional
    @Override
    public Prize updateById(Long id, Prize prize) {
        return this.prizeRepository.findById(id).map(p ->{
            if(p.getEstado().equals("ASIGNADO")){
                throw new PrizeException("no se puede modificar un premio ya asignado");
            }
            p.setDescripcion(prize.getDescripcion());
            p.setValor(prize.getValor());
            p.setPosicion(prize.getPosicion());
            p.setEstado(prize.getEstado());
            return this.prizeRepository.save(p);
        }).orElseThrow(
                () -> new PrizeException("premio no existe")
        );
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Prize prize = this.prizeRepository.findById(id).orElseThrow(
                () -> new PrizeException("premio no existe")
        );
        if (prize.getEstado().equals("ASIGNADO")){
            throw new PrizeException("no se peude eliminar un premio ya asignado");
        }
        this.prizeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Prize> findByTorneoId(Long torneoId) {
        return this.prizeRepository.findByTorneoId(torneoId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Prize> findByPosicion(Integer posicion) {
        return this.prizeRepository.findByPosicion(posicion);
    }

    @Transactional
    @Override
    public PremioAsignado asignarPremio(Long premioId, Long participanteId) {
        Prize prize = this.prizeRepository.findById(premioId).orElseThrow(
                () -> new PrizeException("premio no existe")
        );

        try{
            TournamentDTO torneo = tournamentClient.findById(prize.getTorneoId());
            if(!torneo.getEstado().equals("FINALIZADO")){
                throw new PrizeException("no pueden asignar premios antes de finalizar el torneo");
            }
        }catch(FeignException e){
            throw new PrizeException("el torneo no existe");
        }
        if(premioAsignadoRepository.existsByPremioId(premioId)){
            throw new PrizeException("este premio ya fue asignado");
        }

        try{
            rankingClient.findById(participanteId);
        }catch(FeignException e){
            throw new PrizeException("el participante no existe en el ranking");
        }

        PremioAsignado asignado = new PremioAsignado();
        asignado.setPremioId(premioId);
        asignado.setParticipanteId(participanteId);
        asignado.setFechaAsignacion(LocalDate.now());

        prize.setEstado("ASIGNADO");
        this.prizeRepository.save(prize);

        return this.premioAsignadoRepository.save(asignado);
    }
}