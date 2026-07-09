package com.example.match_service.services;

import com.example.match_service.clients.RegistrationClient;
import com.example.match_service.clients.TourClient;
import com.example.match_service.exceptions.MatchExceptions;
import com.example.match_service.models.Match;
import com.example.match_service.models.dtos.RegistrationDto;
import com.example.match_service.models.dtos.TourDto;
import com.example.match_service.repositories.MatchRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock private MatchRepository matchRepository;
    @Mock private TourClient tourClient;
    @Mock private RegistrationClient registrationClient;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Match matchPrueba;
    private TourDto tourEnCurso;
    private RegistrationDto inscripcionActiva;

    @BeforeEach
    public void setUp() {
        this.matchPrueba = new Match();
        this.matchPrueba.setMatchId(1L);
        this.matchPrueba.setTourId(100L);
        this.matchPrueba.setParticipanteAId(10L);
        this.matchPrueba.setParticipanteBId(20L);
        this.matchPrueba.setRound("CUARTOS");
        this.matchPrueba.setEstado("PROGRAMADA");

        this.tourEnCurso = new TourDto();
        this.tourEnCurso.setTourId(100L);
        this.tourEnCurso.setEstado("EN_CURSO");

        this.inscripcionActiva = new RegistrationDto();
        this.inscripcionActiva.setEstado("ACTIVO");
    }

    @Test
    @DisplayName("Debe listar todas las partidas")
    public void shouldFindAll() {
        when(matchRepository.findAll()).thenReturn(List.of(matchPrueba));
        List<Match> result = matchService.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Debe encontrar una partida por ID")
    public void shouldFindById() {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(matchPrueba));
        Match result = matchService.findById(1L);
        assertThat(result.getEstado()).isEqualTo("PROGRAMADA");
    }

    @Test
    @DisplayName("Debe lanzar MatchExceptions si el ID no existe")
    public void shouldThrowWhenIdNotFound() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> matchService.findById(99L))
                .isInstanceOf(MatchExceptions.class)
                .hasMessage("La partida con id 99 no existe");
    }

    @Test
    @DisplayName("Debe guardar partida válida con estado PROGRAMADA")
    public void shouldSaveMatch() {
        Match nuevo = new Match();
        nuevo.setTourId(100L);
        nuevo.setParticipanteAId(10L); nuevo.setParticipanteBId(20L);
        nuevo.setRound("CUARTOS");

        when(tourClient.findById(100L)).thenReturn(tourEnCurso);
        when(registrationClient.findById(10L)).thenReturn(inscripcionActiva);
        when(registrationClient.findById(20L)).thenReturn(inscripcionActiva);
        when(matchRepository.existsByParticipanteAIdAndParticipanteBIdAndRound(10L, 20L, "CUARTOS")).thenReturn(false);
        when(matchRepository.save(any(Match.class))).thenReturn(matchPrueba);

        Match result = matchService.save(nuevo);

        assertThat(result.getEstado()).isEqualTo("PROGRAMADA");
        verify(tourClient, times(1)).findById(100L);
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    @DisplayName("Debe lanzar MatchExceptions si el torneo no está EN_CURSO")
    public void shouldThrowWhenTorneoNotEnCurso() {
        Match nuevo = new Match();
        nuevo.setTourId(100L);

        TourDto tourBorrador = new TourDto();
        tourBorrador.setEstado("BORRADOR");
        when(tourClient.findById(100L)).thenReturn(tourBorrador);

        assertThatThrownBy(() -> matchService.save(nuevo))
                .isInstanceOf(MatchExceptions.class)
                .hasMessage("el torneo no esta en curso");

        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    @DisplayName("Debe lanzar MatchExceptions si ya existe enfrentamiento en la misma ronda")
    public void shouldThrowWhenDuplicateMatch() {
        Match nuevo = new Match();
        nuevo.setTourId(100L);
        nuevo.setParticipanteAId(10L); nuevo.setParticipanteBId(20L);
        nuevo.setRound("CUARTOS");

        when(tourClient.findById(100L)).thenReturn(tourEnCurso);
        when(registrationClient.findById(10L)).thenReturn(inscripcionActiva);
        when(registrationClient.findById(20L)).thenReturn(inscripcionActiva);
        when(matchRepository.existsByParticipanteAIdAndParticipanteBIdAndRound(10L, 20L, "CUARTOS")).thenReturn(true);

        assertThatThrownBy(() -> matchService.save(nuevo))
                .isInstanceOf(MatchExceptions.class)
                .hasMessageContaining("Ya existe un enfrentamiento");

        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    @DisplayName("Debe cancelar una partida PROGRAMADA")
    public void shouldCancelarMatch() {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(matchPrueba));
        when(matchRepository.save(any(Match.class))).thenAnswer(inv -> inv.getArgument(0));

        Match result = matchService.cancelar(1L);

        assertThat(result.getEstado()).isEqualTo("CANCELADA");
    }

    @Test
    @DisplayName("Debe lanzar MatchExceptions al cancelar una partida ya CANCELADA")
    public void shouldThrowWhenAlreadyCanceled() {
        matchPrueba.setEstado("CANCELADA");
        when(matchRepository.findById(1L)).thenReturn(Optional.of(matchPrueba));

        assertThatThrownBy(() -> matchService.cancelar(1L))
                .isInstanceOf(MatchExceptions.class)
                .hasMessage("La partida ya está cancelada");

        verify(matchRepository, never()).save(any(Match.class));
    }
}
