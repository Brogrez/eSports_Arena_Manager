package com.duoc.tournament_service.services;

import com.duoc.tournament_service.clients.GameClient;
import com.duoc.tournament_service.exceptions.TourException;
import com.duoc.tournament_service.models.Tour;
import com.duoc.tournament_service.models.dtos.GameDto;
import com.duoc.tournament_service.repositories.TourRepository;
import feign.FeignException;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
public class TournamentServiceTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private GameClient gameClient;

    @InjectMocks
    private TourServiceImpl tourService;

    private Tour tourPrueba;
    private List<Tour> tourList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker(Locale.of("es", "CL"));


        this.tourPrueba = new Tour();
        this.tourPrueba.setTourId(1L);
        this.tourPrueba.setGameId(5L);
        this.tourPrueba.setEstado("BORRADOR");
        this.tourPrueba.setCupoMaximo(16);
        this.tourPrueba.setFechaInicio(LocalDate.of(2026, 10, 15));
        this.tourPrueba.setFechaFin(LocalDate.of(2026, 10, 20));

        for (int i = 0; i < 10; i++) {
            Tour tour = new Tour();
            tour.setTourId((long) (i + 2));
            tour.setGameId(faker.number().numberBetween(1L, 10L));
            tour.setEstado("PUBLICADO");
            tour.setFechaInicio(LocalDate.of(2026, 11, 1));
            tour.setFechaFin(LocalDate.of(2026, 11, 10));
            tourList.add(tour);
        }
    }



    @Test
    @DisplayName("Debe listar todos los torneos")
    public void shouldFindAll() {
        when(tourRepository.findAll()).thenReturn(tourList);

        List<Tour> result = tourService.findAll();

        assertThat(result).hasSize(10);
        verify(tourRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar un torneo por su ID")
    public void shouldFindById() {
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tourPrueba));

        Tour result = tourService.findById(1L);

        assertThat(result.getTourId()).isEqualTo(1L);
        verify(tourRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar un ID inexistente")
    public void shouldThrowWhenIdNotFound() {
        when(tourRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tourService.findById(99L))
                .isInstanceOf(TourException.class)
                .hasMessage("Torneo no encontrado");
    }


    @Test
    @DisplayName("Debe guardar un torneo si pasa todas las validaciones")
    public void shouldSaveTour() {
        GameDto gameActivo = new GameDto();
        gameActivo.setEstado("ACTIVO");


        when(gameClient.findByid(5L)).thenReturn(gameActivo);
        when(tourRepository.save(any(Tour.class))).thenAnswer(inv -> inv.getArgument(0));

        Tour result = tourService.save(tourPrueba);

        assertThat(result.getEstado()).isEqualTo("BORRADOR");
        verify(tourRepository, times(1)).save(tourPrueba);
    }

    @Test
    @DisplayName("Debe lanzar excepcion si el juego está inactivo")
    public void shouldThrowWhenGameIsInactive() {
        GameDto gameInactivo = new GameDto();
        gameInactivo.setEstado("INACTIVO");

        when(gameClient.findByid(5L)).thenReturn(gameInactivo);

        assertThatThrownBy(() -> tourService.save(tourPrueba))
                .isInstanceOf(TourException.class)
                .hasMessage("el juego esta inactivo");

        verify(tourRepository, never()).save(any(Tour.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion si el microservicio de juegos falla")
    public void shouldThrowWhenGameClientFails() {
        when(gameClient.findByid(5L)).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> tourService.save(tourPrueba))
                .isInstanceOf(TourException.class)
                .hasMessage("el juego no existe");

        verify(tourRepository, never()).save(any(Tour.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion si la fecha de inicio es en el pasado")
    public void shouldThrowWhenFechaInicioIsPast() {
        GameDto gameActivo = new GameDto();
        gameActivo.setEstado("ACTIVO");
        when(gameClient.findByid(5L)).thenReturn(gameActivo);

        tourPrueba.setFechaInicio(LocalDate.of(2020, 1, 1));

        assertThatThrownBy(() -> tourService.save(tourPrueba))
                .isInstanceOf(TourException.class)
                .hasMessage("la fecha de inicio debe ser posterior a la fecha actual");
    }

    @Test
    @DisplayName("Debe lanzar excepcion si la fecha de fin es antes del inicio")
    public void shouldThrowWhenFechaFinBeforeInicio() {
        GameDto gameActivo = new GameDto();
        gameActivo.setEstado("ACTIVO");
        when(gameClient.findByid(5L)).thenReturn(gameActivo);

        tourPrueba.setFechaInicio(LocalDate.of(2026, 12, 10));
        tourPrueba.setFechaFin(LocalDate.of(2026, 12, 1));

        assertThatThrownBy(() -> tourService.save(tourPrueba))
                .isInstanceOf(TourException.class)
                .hasMessage("la fecha fin debe ser posterior a la fecha de inicio");
    }


    @Test
    @DisplayName("Debe actualizar un torneo si no está en curso")
    public void shouldUpdateTour() {
        Long id = 1L;
        tourPrueba.setEstado("BORRADOR");

        Tour cambios = new Tour();
        cambios.setCupoMaximo(32);
        cambios.setEstado("PUBLICADO");
        cambios.setFechaInicio(LocalDate.of(2026, 11, 1));
        cambios.setFechaFin(LocalDate.of(2026, 11, 5));

        when(tourRepository.findById(id)).thenReturn(Optional.of(tourPrueba));
        when(tourRepository.save(any(Tour.class))).thenAnswer(inv -> inv.getArgument(0));

        Tour result = tourService.updateById(id, cambios);

        assertThat(result.getCupoMaximo()).isEqualTo(32);
        assertThat(result.getEstado()).isEqualTo("PUBLICADO");
        verify(tourRepository, times(1)).save(any(Tour.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al actualizar torneo EN CURSO")
    public void shouldThrowWhenUpdatingEnCurso() {
        Long id = 1L;
        tourPrueba.setEstado("EN_CURSO");

        when(tourRepository.findById(id)).thenReturn(Optional.of(tourPrueba));

        assertThatThrownBy(() -> tourService.updateById(id, new Tour()))
                .isInstanceOf(TourException.class)
                .hasMessage("no se puede modificar torneos en curso");

        verify(tourRepository, never()).save(any(Tour.class));
    }


    @Test
    @DisplayName("Debe cancelar un torneo correctamente")
    public void shouldCancelarTour() {
        Long id = 1L;
        tourPrueba.setEstado("PUBLICADO");

        when(tourRepository.findById(id)).thenReturn(Optional.of(tourPrueba));
        when(tourRepository.save(any(Tour.class))).thenAnswer(inv -> inv.getArgument(0));

        Tour result = tourService.cancelar(id);

        assertThat(result.getEstado()).isEqualTo("CANCELADO");
    }

    @Test
    @DisplayName("Debe lanzar excepcion al cancelar torneo ya FINALIZADO")
    public void shouldThrowWhenCancelarFinalizado() {
        Long id = 1L;
        tourPrueba.setEstado("FINALIZADO");

        when(tourRepository.findById(id)).thenReturn(Optional.of(tourPrueba));

        assertThatThrownBy(() -> tourService.cancelar(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("el torneo no se puede cancelar");
    }

    @Test
    @DisplayName("Debe cerrar un torneo correctamente")
    public void shouldCerrarTour() {
        Long id = 1L;
        tourPrueba.setEstado("EN_CURSO");

        when(tourRepository.findById(id)).thenReturn(Optional.of(tourPrueba));
        when(tourRepository.save(any(Tour.class))).thenAnswer(inv -> inv.getArgument(0));

        Tour result = tourService.cerrar(id);

        assertThat(result.getEstado()).isEqualTo("FINALIZADO");
    }

    @Test
    @DisplayName("Debe lanzar excepcion al cerrar torneo que no está en curso")
    public void shouldThrowWhenCerrarNotInCurso() {
        Long id = 1L;
        tourPrueba.setEstado("BORRADOR");

        when(tourRepository.findById(id)).thenReturn(Optional.of(tourPrueba));

        assertThatThrownBy(() -> tourService.cerrar(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("solo se pueden cerrar torneos que esten en curso");
    }



    @Test
    @DisplayName("Debe eliminar un torneo por su ID")
    public void shouldDeleteById() {
        tourService.deleteById(1L);
        verify(tourRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe listar torneos por Game ID")
    public void shouldFindByGameId() {
        when(tourRepository.findByGameId(5L)).thenReturn(tourList);
        List<Tour> result = tourService.findByGameId(5L);
        assertThat(result).hasSize(10);
    }

    @Test
    @DisplayName("Debe listar torneos por Estado")
    public void shouldFindByEstado() {
        when(tourRepository.findByEstado("PUBLICADO")).thenReturn(tourList);
        List<Tour> result = tourService.findByEstado("PUBLICADO");
        assertThat(result).hasSize(10);
    }

    @Test
    @DisplayName("Debe listar torneos por Fecha de Inicio")
    public void shouldFindByFechaInicio() {
        LocalDate fecha = LocalDate.of(2026, 11, 1);
        when(tourRepository.findByFechaInicio(fecha)).thenReturn(tourList);
        List<Tour> result = tourService.findByFechaInicio(fecha);
        assertThat(result).hasSize(10);
    }
}