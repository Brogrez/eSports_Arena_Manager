package com.duoc.services;

import com.duoc.prize_service.clients.RankingClient;
import com.duoc.prize_service.clients.TournamentClient;
import com.duoc.prize_service.exceptions.PrizeException;
import com.duoc.prize_service.models.PremioAsignado;
import com.duoc.prize_service.models.Prize;
import com.duoc.prize_service.models.dtos.TournamentDTO;
import com.duoc.prize_service.repositories.PremioAsignadoRepository;
import com.duoc.prize_service.repositories.PrizeRepository;
import com.duoc.prize_service.services.PrizeServiceImpl;
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
public class PrizeServiceTest {

    @Mock private PrizeRepository prizeRepository;
    @Mock private PremioAsignadoRepository premioAsignadoRepository;
    @Mock private TournamentClient tournamentClient;
    @Mock private RankingClient rankingClient;

    @InjectMocks
    private PrizeServiceImpl prizeService;

    private Prize prizePrueba;
    private TournamentDTO torneoFinalizado;

    @BeforeEach
    public void setUp() {
        this.prizePrueba = new Prize();
        this.prizePrueba.setPremioId(1L);
        this.prizePrueba.setTorneoId(100L);
        this.prizePrueba.setPosicion(1);
        this.prizePrueba.setDescripcion("Medalla de oro");
        this.prizePrueba.setValor(500000.0);
        this.prizePrueba.setEstado("DISPONIBLE");

        this.torneoFinalizado = new TournamentDTO();
        this.torneoFinalizado.setTorneoId(100L);
        this.torneoFinalizado.setEstado("FINALIZADO");
    }

    @Test
    @DisplayName("Debe listar todos los premios")
    public void shouldFindAll() {
        when(prizeRepository.findAll()).thenReturn(List.of(prizePrueba));
        List<Prize> result = prizeService.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Debe encontrar un premio por ID")
    public void shouldFindById() {
        when(prizeRepository.findById(1L)).thenReturn(Optional.of(prizePrueba));
        Prize result = prizeService.findById(1L);
        assertThat(result.getEstado()).isEqualTo("DISPONIBLE");
    }

    @Test
    @DisplayName("Debe lanzar PrizeException si el ID no existe")
    public void shouldThrowWhenIdNotFound() {
        when(prizeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> prizeService.findById(99L))
                .isInstanceOf(PrizeException.class)
                .hasMessage("premio no existe");
    }

    @Test
    @DisplayName("Debe guardar premio con estado DISPONIBLE")
    public void shouldSavePrize() {
        Prize nuevo = new Prize();
        nuevo.setTorneoId(100L); nuevo.setPosicion(2);
        nuevo.setDescripcion("Plata"); nuevo.setValor(200000.0);

        when(tournamentClient.findById(100L)).thenReturn(torneoFinalizado);
        when(prizeRepository.existsByTorneoIdAndPosicion(100L, 2)).thenReturn(false);
        Prize guardado = new Prize();
        guardado.setPremioId(2L); guardado.setEstado("DISPONIBLE");
        when(prizeRepository.save(any(Prize.class))).thenReturn(guardado);

        Prize result = prizeService.save(nuevo);

        assertThat(result.getEstado()).isEqualTo("DISPONIBLE");
        verify(tournamentClient, times(1)).findById(100L);
        verify(prizeRepository, times(1)).save(any(Prize.class));
    }

    @Test
    @DisplayName("Debe lanzar PrizeException si el torneo no existe (Feign falla)")
    public void shouldThrowWhenTournamentFeignFails() {
        Prize nuevo = new Prize();
        nuevo.setTorneoId(99L); nuevo.setPosicion(1);

        when(tournamentClient.findById(99L)).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> prizeService.save(nuevo))
                .isInstanceOf(PrizeException.class)
                .hasMessage("el torneo no existe");

        verify(prizeRepository, never()).save(any(Prize.class));
    }

    @Test
    @DisplayName("Debe lanzar PrizeException si ya existe premio para esa posición")
    public void shouldThrowWhenPosicionDuplicated() {
        Prize nuevo = new Prize();
        nuevo.setTorneoId(100L); nuevo.setPosicion(1); nuevo.setValor(500.0);

        when(tournamentClient.findById(100L)).thenReturn(torneoFinalizado);
        when(prizeRepository.existsByTorneoIdAndPosicion(100L, 1)).thenReturn(true);

        assertThatThrownBy(() -> prizeService.save(nuevo))
                .isInstanceOf(PrizeException.class)
                .hasMessageContaining("ya existe un premio para la posicion");

        verify(prizeRepository, never()).save(any(Prize.class));
    }

    @Test
    @DisplayName("Debe lanzar PrizeException si el valor es negativo")
    public void shouldThrowWhenValorNegativo() {
        Prize nuevo = new Prize();
        nuevo.setTorneoId(100L); nuevo.setPosicion(3); nuevo.setValor(-100.0);

        when(tournamentClient.findById(100L)).thenReturn(torneoFinalizado);
        when(prizeRepository.existsByTorneoIdAndPosicion(100L, 3)).thenReturn(false);

        assertThatThrownBy(() -> prizeService.save(nuevo))
                .isInstanceOf(PrizeException.class)
                .hasMessage("El valor del premio no puede ser negativo");

        verify(prizeRepository, never()).save(any(Prize.class));
    }

    @Test
    @DisplayName("Debe lanzar PrizeException al modificar premio ASIGNADO")
    public void shouldThrowWhenModifyingAsignado() {
        prizePrueba.setEstado("ASIGNADO");
        when(prizeRepository.findById(1L)).thenReturn(Optional.of(prizePrueba));

        assertThatThrownBy(() -> prizeService.updateById(1L, new Prize()))
                .isInstanceOf(PrizeException.class)
                .hasMessage("no se puede modificar un premio ya asignado");

        verify(prizeRepository, never()).save(any(Prize.class));
    }

    @Test
    @DisplayName("Debe asignar premio cuando el torneo está FINALIZADO")
    public void shouldAsignarPremio() {
        when(prizeRepository.findById(1L)).thenReturn(Optional.of(prizePrueba));
        when(tournamentClient.findById(100L)).thenReturn(torneoFinalizado);
        when(premioAsignadoRepository.existsByPremioId(1L)).thenReturn(false);
        doNothing().when(rankingClient).findById(10L);

        PremioAsignado asignado = new PremioAsignado();
        asignado.setPremioId(1L); asignado.setParticipanteId(10L);
        when(premioAsignadoRepository.save(any(PremioAsignado.class))).thenReturn(asignado);
        when(prizeRepository.save(any(Prize.class))).thenAnswer(inv -> inv.getArgument(0));

        PremioAsignado result = prizeService.asignarPremio(1L, 10L);

        assertThat(result.getPremioId()).isEqualTo(1L);
        verify(premioAsignadoRepository, times(1)).save(any(PremioAsignado.class));
    }

    @Test
    @DisplayName("Debe lanzar PrizeException si el torneo no está FINALIZADO al asignar")
    public void shouldThrowWhenTorneoNotFinalizado() {
        TournamentDTO torneoEnCurso = new TournamentDTO();
        torneoEnCurso.setEstado("EN_CURSO");

        when(prizeRepository.findById(1L)).thenReturn(Optional.of(prizePrueba));
        when(tournamentClient.findById(100L)).thenReturn(torneoEnCurso);

        assertThatThrownBy(() -> prizeService.asignarPremio(1L, 10L))
                .isInstanceOf(PrizeException.class)
                .hasMessage("no pueden asignar premios antes de finalizar el torneo");
    }
}
