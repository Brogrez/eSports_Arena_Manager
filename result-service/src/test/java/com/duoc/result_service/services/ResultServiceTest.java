package com.duoc.result_service.services;


import eSports_Arena_Manager.result_service.clients.MatchClient;
import eSports_Arena_Manager.result_service.models.Result;
import eSports_Arena_Manager.result_service.models.dtos.MatchDTO;
import eSports_Arena_Manager.result_service.repositories.ResultRepository;
import eSports_Arena_Manager.result_service.services.ResultServiceImpl;
import feign.FeignException;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResultServiceTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private MatchClient matchClient;

    @InjectMocks
    private ResultServiceImpl resultService;

    private Result resultPrueba;
    private List<Result> resultList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker(Locale.of("es", "CL"));

        this.resultPrueba = new Result();
        this.resultPrueba.setResultadoId(1L);
        this.resultPrueba.setPartidaId(100L);
        this.resultPrueba.setTeamAId(10L);
        this.resultPrueba.setTeamBId(20L);
        this.resultPrueba.setScoreA(2);
        this.resultPrueba.setScoreB(1);
        this.resultPrueba.setEstado("PENDIENTE");

        for (int i = 0; i < 5; i++) {
            Result res = new Result();
            res.setResultadoId((long) (i + 2));
            res.setPartidaId((long) (101 + i));
            res.setTeamAId(10L);
            res.setTeamBId(20L);
            res.setScoreA(faker.number().numberBetween(0, 3));
            res.setScoreB(faker.number().numberBetween(0, 3));
            res.setEstado(i % 2 == 0 ? "VALIDADO" : "PENDIENTE");
            resultList.add(res);
        }
    }


    @Test
    @DisplayName("Debe listar todos los resultados")
    public void shouldFindAll() {
        when(resultRepository.findAll()).thenReturn(resultList);

        List<Result> results = resultService.findAll();

        assertThat(results).hasSize(5);
        verify(resultRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar un resultado por su ID")
    public void shouldFindById() {
        when(resultRepository.findById(1L)).thenReturn(Optional.of(resultPrueba));

        Result result = resultService.findById(1L);

        assertThat(result.getResultadoId()).isEqualTo(1L);
        verify(resultRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar ID inexistente")
    public void shouldThrowWhenIdNotFound() {
        when(resultRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resultService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El resultado no existe");
    }

    @Test
    @DisplayName("Debe buscar un resultado por el ID de la Partida")
    public void shouldFindByPartidaId() {
        when(resultRepository.findByPartidaId(100L)).thenReturn(Optional.of(resultPrueba));

        Result result = resultService.findByPartidaId(100L);

        assertThat(result.getPartidaId()).isEqualTo(100L);
        verify(resultRepository, times(1)).findByPartidaId(100L);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar por Partida ID inexistente")
    public void shouldThrowWhenPartidaIdNotFound() {
        when(resultRepository.findByPartidaId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resultService.findByPartidaId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No existe resultado para la partida");
    }

    @Test
    @DisplayName("Debe buscar resultados por Estado")
    public void shouldFindByEstado() {
        when(resultRepository.findByEstado("PENDIENTE")).thenReturn(List.of(resultPrueba));
        List<Result> results = resultService.findByEstado("PENDIENTE");
        assertThat(results).hasSize(1);
    }


    @Test
    @DisplayName("Debe guardar resultado, calcular que gana Equipo A y dejar PENDIENTE")
    public void shouldSaveResultAndCalculateWinnerA() {
        when(matchClient.findById(100L)).thenReturn(new MatchDTO());
        when(resultRepository.existsByPartidaId(100L)).thenReturn(false);
        when(resultRepository.save(any(Result.class))).thenAnswer(i -> i.getArgument(0));

        Result result = resultService.save(resultPrueba);

        assertThat(result.getEstado()).isEqualTo("PENDIENTE");

        assertThat(result.getWinnerId()).isEqualTo(10L);
        verify(resultRepository, times(1)).save(resultPrueba);
    }

    @Test
    @DisplayName("Debe calcular que gana Equipo B al guardar")
    public void shouldSaveResultAndCalculateWinnerB() {
        resultPrueba.setScoreA(1);
        resultPrueba.setScoreB(3);

        when(matchClient.findById(100L)).thenReturn(new MatchDTO());
        when(resultRepository.existsByPartidaId(100L)).thenReturn(false);
        when(resultRepository.save(any(Result.class))).thenAnswer(i -> i.getArgument(0));

        Result result = resultService.save(resultPrueba);


        assertThat(result.getWinnerId()).isEqualTo(20L);
    }

    @Test
    @DisplayName("Debe calcular empate (Winner Null) al guardar")
    public void shouldSaveResultAndCalculateDraw() {
        resultPrueba.setScoreA(2);
        resultPrueba.setScoreB(2);

        when(matchClient.findById(100L)).thenReturn(new MatchDTO());
        when(resultRepository.existsByPartidaId(100L)).thenReturn(false);
        when(resultRepository.save(any(Result.class))).thenAnswer(i -> i.getArgument(0));

        Result result = resultService.save(resultPrueba);


        assertThat(result.getWinnerId()).isNull();
    }

    @Test
    @DisplayName("Debe lanzar excepcion al guardar si MatchClient falla")
    public void shouldThrowWhenMatchClientFails() {
        when(matchClient.findById(100L)).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> resultService.save(resultPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("la partida no existe");

        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al guardar si ya existe resultado para la partida")
    public void shouldThrowWhenResultAlreadyExists() {
        when(matchClient.findById(100L)).thenReturn(new MatchDTO());
        when(resultRepository.existsByPartidaId(100L)).thenReturn(true);

        assertThatThrownBy(() -> resultService.save(resultPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("ya existe un resultado para esa partida");

        verify(resultRepository, never()).save(any(Result.class));
    }



    @Test
    @DisplayName("Debe actualizar resultado y recalcular ganador")
    public void shouldUpdateResult() {
        Long id = 1L;
        resultPrueba.setEstado("PENDIENTE");

        Result cambios = new Result();
        cambios.setScoreA(0);
        cambios.setScoreB(2);
        cambios.setEstado("REVISADO");

        when(resultRepository.findById(id)).thenReturn(Optional.of(resultPrueba));
        when(resultRepository.save(any(Result.class))).thenAnswer(inv -> inv.getArgument(0));

        Result result = resultService.updateById(id, cambios);

        assertThat(result.getEstado()).isEqualTo("REVISADO");
        assertThat(result.getScoreA()).isEqualTo(0);
        assertThat(result.getScoreB()).isEqualTo(2);
        assertThat(result.getWinnerId()).isEqualTo(20L);
        verify(resultRepository, times(1)).save(any(Result.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al intentar actualizar un resultado VALIDADO")
    public void shouldThrowWhenUpdatingValidado() {
        Long id = 1L;
        resultPrueba.setEstado("VALIDADO");

        when(resultRepository.findById(id)).thenReturn(Optional.of(resultPrueba));

        assertThatThrownBy(() -> resultService.updateById(id, new Result()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("un resultado valido no puede modificarse");

        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al actualizar ID inexistente")
    public void shouldThrowWhenUpdateIdNotFound() {
        when(resultRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resultService.updateById(99L, new Result()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("el resultado no existe");
    }

    @Test
    @DisplayName("Debe eliminar un resultado por su ID")
    public void shouldDeleteById() {
        resultService.deleteById(1L);
        verify(resultRepository, times(1)).deleteById(1L);
    }
}