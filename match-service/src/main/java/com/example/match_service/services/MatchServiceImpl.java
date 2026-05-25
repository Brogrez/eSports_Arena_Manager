package com.example.match_service.services;

import com.example.match_service.clients.RegistrationClient;
import com.example.match_service.clients.TourClient;
import com.example.match_service.exceptions.MatchExceptions;
import com.example.match_service.models.Match;
import com.example.match_service.models.dtos.RegistrationDto;
import com.example.match_service.models.dtos.TourDto;
import com.example.match_service.repositories.MatchRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TourClient tourClient;

    @Autowired
    private RegistrationClient registrationClient;

    @Transactional(readOnly = true)
    @Override
    public List<Match> findAll() {
        return this.matchRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Match findById(Long id) {
        return this.matchRepository.findById(id).orElseThrow(
                () -> new MatchExceptions("La partida con id " + id + " no existe")
        );
    }

    @Transactional
    @Override
    public Match save(Match match) {
        try{
            TourDto tour = tourClient.findById(match.getTourId());
            if(!tour.getEstado().equals("EN_CURSO")){
                throw new MatchExceptions("el torneo no esta en curso");
            }
        }catch(FeignException e){
            throw new MatchExceptions("el torneo no existe");
        }
        try {
            RegistrationDto inscripcionA = registrationClient.findById(match.getParticipanteAId());
            if (!inscripcionA.getEstado().equals("ACTIVO")) {
                throw new MatchExceptions("El participante A no tiene una inscripción activa");
            }
        } catch (FeignException e) {
            throw new MatchExceptions("La inscripción del participante A con id " + match.getParticipanteAId() + " no existe");
        }

        try {
            RegistrationDto inscripcionB = registrationClient.findById(match.getParticipanteBId());
            if (!inscripcionB.getEstado().equals("ACTIVO")) {
                throw new MatchExceptions("El participante B no tiene una inscripción activa");
            }
        } catch (FeignException e) {
            throw new MatchExceptions("La inscripción del participante B con id " + match.getParticipanteBId() + " no existe");
        }

        if (matchRepository.existsByParticipanteAIdAndParticipanteBIdAndRound(
                match.getParticipanteAId(), match.getParticipanteBId(), match.getRound())) {
            throw new MatchExceptions("Ya existe un enfrentamiento entre estos participantes en la ronda " + match.getRound());
        }
        match.setEstado("PROGRAMADA");
        return this.matchRepository.save(match);
    }

    @Transactional
    @Override
    public Match updateById(Match match, Long id) {
        return this.matchRepository.findById(id).map(m -> {
            if(m.getEstado().equals("CANCELADO")){
                throw new MatchExceptions("no se puede modificar una partida cancelada");
            }

            m.setFechaHora(match.getFechaHora());
            m.setParticipanteAId(match.getParticipanteAId());
            m.setParticipanteBId(match.getParticipanteBId());
            m.setEstado(match.getEstado());

            return this.matchRepository.save(m);
        }).orElseThrow(
                () -> new MatchExceptions("la partida no existe")
        );
    }
    @Transactional
    @Override
    public void deleteById(Long id) {
        this.matchRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    @Override
    public List<Match> findByTorneoId(Long torneoId) {
        return this.matchRepository.findByTourId(torneoId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Match> findByEstado(String estado) {
        return this.matchRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Match> findByRonda(String ronda) {
        return this.matchRepository.findByRound(ronda);
    }

    @Transactional
    @Override
    public Match cancelar(Long id) {
        Match match = this.matchRepository.findById(id).orElseThrow(
                () -> new MatchExceptions("La partida con id " + id + " no existe")
        );

        if (match.getEstado().equals("CANCELADA")) {
            throw new MatchExceptions("La partida ya está cancelada");
        }

        match.setEstado("CANCELADA");
        return this.matchRepository.save(match);
    }
}
