package com.example.ranking_service.services;

import com.example.ranking_service.clients.ResultClient;
import com.example.ranking_service.clients.TourClient;
import com.example.ranking_service.exceptions.RankingExceptions;
import com.example.ranking_service.models.Ranking;
import com.example.ranking_service.models.dtos.TourDto;
import com.example.ranking_service.repositories.RankingRepository;
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
public class RankingServiceTest {

    @Mock private RankingRepository rankingRepository;
    @Mock private TourClient tourClient;
    @Mock private ResultClient resultClient;

    @InjectMocks
    private RankingServiceImpl rankingService;

    private Ranking rankingPrueba;
    private TourDto tourDto;

    @BeforeEach
    public void setUp() {
        this.rankingPrueba = new Ranking();
        this.rankingPrueba.setRankingId(1L);
        this.rankingPrueba.setTourId(100L);
        this.rankingPrueba.setParticipanteId(10L);
        this.rankingPrueba.setPuntos(0);
        this.rankingPrueba.setVictorias(0);
        this.rankingPrueba.setDerrotas(0);
        this.rankingPrueba.setDiferencia(0);
        this.rankingPrueba.setPosicion(0);

        this.tourDto = new TourDto();
        this.tourDto.setTourId(100L);
        this.tourDto.setEstado("EN_CURSO");
    }

    @Test
    @DisplayName("Debe listar todos los rankings")
    public void shouldFindAll() {
        when(rankingRepository.findAll()).thenReturn(List.of(rankingPrueba));
        List<Ranking> result = rankingService.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Debe encontrar ranking por ID")
    public void shouldFindById() {
        when(rankingRepository.findById(1L)).thenReturn(Optional.of(rankingPrueba));
        Ranking result = rankingService.findById(1L);
        assertThat(result.getPuntos()).isEqualTo(0);
    }

    @Test
    @DisplayName("Debe lanzar RankingExceptions si el ID no existe")
    public void shouldThrowWhenIdNotFound() {
        when(rankingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> rankingService.findById(99L))
                .isInstanceOf(RankingExceptions.class)
                .hasMessage("ranking no existe");
    }

    @Test
    @DisplayName("Debe guardar ranking con valores iniciales en cero")
    public void shouldSaveRankingWithZeroValues() {
        Ranking nuevo = new Ranking();
        nuevo.setTourId(100L); nuevo.setParticipanteId(10L);

        when(tourClient.findById(100L)).thenReturn(tourDto);
        when(rankingRepository.existsByTourIdAndParticipanteId(100L, 10L)).thenReturn(false);
        when(rankingRepository.save(any(Ranking.class))).thenReturn(rankingPrueba);

        Ranking result = rankingService.save(nuevo);

        assertThat(result.getPuntos()).isEqualTo(0);
        assertThat(result.getVictorias()).isEqualTo(0);
        verify(tourClient, times(1)).findById(100L);
        verify(rankingRepository, times(1)).save(any(Ranking.class));
    }

    @Test
    @DisplayName("Debe lanzar RankingExceptions si el torneo no existe (Feign falla)")
    public void shouldThrowWhenTourFeignFails() {
        Ranking nuevo = new Ranking();
        nuevo.setTourId(99L); nuevo.setParticipanteId(10L);

        when(tourClient.findById(99L)).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> rankingService.save(nuevo))
                .isInstanceOf(RankingExceptions.class)
                .hasMessage("el torneo no existe");

        verify(rankingRepository, never()).save(any(Ranking.class));
    }

    @Test
    @DisplayName("Debe lanzar RankingExceptions si el participante ya tiene ranking en el torneo")
    public void shouldThrowWhenParticipanteDuplicated() {
        Ranking nuevo = new Ranking();
        nuevo.setTourId(100L); nuevo.setParticipanteId(10L);

        when(tourClient.findById(100L)).thenReturn(tourDto);
        when(rankingRepository.existsByTourIdAndParticipanteId(100L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> rankingService.save(nuevo))
                .isInstanceOf(RankingExceptions.class)
                .hasMessage("el participante ya tiene un registro en el ranking");

        verify(rankingRepository, never()).save(any(Ranking.class));
    }

    @Test
    @DisplayName("Debe actualizar puntos correctamente")
    public void shouldActualizarPuntos() {
        rankingPrueba.setPuntos(3); rankingPrueba.setVictorias(1);
        rankingPrueba.setDerrotas(0); rankingPrueba.setDiferencia(1);

        when(rankingRepository.findById(1L)).thenReturn(Optional.of(rankingPrueba));
        when(rankingRepository.findByTourIdOrderByPuntosDesc(100L)).thenReturn(List.of(rankingPrueba));
        when(rankingRepository.save(any(Ranking.class))).thenAnswer(inv -> inv.getArgument(0));

        Ranking result = rankingService.actualizarPuntos(1L, 3, 1, 0);

        assertThat(result.getPuntos()).isEqualTo(6);
        assertThat(result.getVictorias()).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe lanzar RankingExceptions al cerrar ranking de torneo sin rankings")
    public void shouldThrowWhenCerrarRankingVacio() {
        when(rankingRepository.findByTourId(100L)).thenReturn(List.of());

        assertThatThrownBy(() -> rankingService.cerrarRanking(100L))
                .isInstanceOf(RankingExceptions.class)
                .hasMessage("No existe ranking para el torneo con id 100");
    }
}