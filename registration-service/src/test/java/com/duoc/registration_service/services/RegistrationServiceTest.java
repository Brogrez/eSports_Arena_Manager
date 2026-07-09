package com.duoc.registration_service.services;

import com.duoc.registration_service.clients.EquipoClient;
import com.duoc.registration_service.clients.SancionClient;
import com.duoc.registration_service.clients.TorneoClient;
import com.duoc.registration_service.clients.UsuarioClient;
import com.duoc.registration_service.exceptions.InscripcionException;
import com.duoc.registration_service.models.Inscripcion;
import com.duoc.registration_service.models.dtos.EquipoDTO;
import com.duoc.registration_service.models.dtos.InscripcionDTO;
import com.duoc.registration_service.models.dtos.SancionDTO;
import com.duoc.registration_service.models.dtos.TorneoDTO;
import com.duoc.registration_service.models.dtos.UsuarioDTO;
import com.duoc.registration_service.repositories.InscripcionRepository;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private EquipoClient equipoClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private TorneoClient torneoClient;

    @Mock
    private SancionClient sancionClient;

    @InjectMocks
    private InscripcionServiceImpl inscripcionService;

    private Inscripcion inscripcionPrueba;
    private List<Inscripcion> inscripcionList = new ArrayList<>();
    private Date fechaPasada;
    private Date fechaFutura;

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker(Locale.of("es", "CL"));

        long ahora = System.currentTimeMillis();
        this.fechaPasada = new Date(ahora - 86400000L);
        this.fechaFutura = new Date(ahora + 86400000L);

        this.inscripcionPrueba = new Inscripcion();
        this.inscripcionPrueba.setInscripcionId(1L);
        this.inscripcionPrueba.setTorneoId(10L);
        this.inscripcionPrueba.setEquipoId(5L);
        this.inscripcionPrueba.setJugadorId(20L);
        this.inscripcionPrueba.setTipoParticipante("TITULAR");
        this.inscripcionPrueba.setEstado("PENDIENTE");
        this.inscripcionPrueba.setFechaInscripcion(fechaPasada);

        for (int i = 0; i < 5; i++) {
            Inscripcion ins = new Inscripcion();
            ins.setInscripcionId((long) (i + 2));
            ins.setTorneoId(10L);
            ins.setEquipoId(faker.number().numberBetween(1L, 10L));
            ins.setJugadorId(faker.number().numberBetween(1L, 50L));
            ins.setEstado("ACTIVO");
            ins.setFechaInscripcion(fechaPasada);
            inscripcionList.add(ins);
        }
    }


    @Test
    @DisplayName("Debe listar todas las inscripciones y mapear a DTO")
    public void shouldFindAll() {
        when(inscripcionRepository.findAll()).thenReturn(inscripcionList);

        List<InscripcionDTO> result = inscripcionService.findAll();

        assertThat(result).hasSize(5);

        assertThat(result.get(0).getUsuarioId()).isEqualTo(inscripcionList.get(0).getJugadorId());
        verify(inscripcionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar por ID de Inscripcion")
    public void shouldFindByInscripcionId() {
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcionPrueba));

        Inscripcion result = inscripcionService.findByInscripcionId(1L);

        assertThat(result.getInscripcionId()).isEqualTo(1L);
        verify(inscripcionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar Inscripcion ID inexistente")
    public void shouldThrowWhenInscripcionIdNotFound() {
        when(inscripcionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inscripcionService.findByInscripcionId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Inscripcion no encontrada");
    }

    @Test
    @DisplayName("Debe buscar por Jugador ID")
    public void shouldFindByJugadorId() {
        when(inscripcionRepository.findByJugadorId(20L)).thenReturn(Optional.of(inscripcionPrueba));

        Inscripcion result = inscripcionService.findByJugadorId(20L);

        assertThat(result.getJugadorId()).isEqualTo(20L);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar Jugador ID inexistente")
    public void shouldThrowWhenJugadorIdNotFound() {
        when(inscripcionRepository.findByJugadorId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inscripcionService.findByJugadorId(99L))
                .isInstanceOf(InscripcionException.class)
                .hasMessage("jugador no encontrado");
    }

    @Test
    @DisplayName("Debe buscar por Equipo ID")
    public void shouldFindByEquipoId() {
        when(inscripcionRepository.findByEquipoId(5L)).thenReturn(Optional.of(inscripcionPrueba));
        Inscripcion result = inscripcionService.findByEquipoId(5L);
        assertThat(result.getEquipoId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Debe buscar por Torneo ID")
    public void shouldFindByTorneoId() {
        when(inscripcionRepository.findByTorneoId(10L)).thenReturn(Optional.of(inscripcionPrueba));
        Inscripcion result = inscripcionService.findByTorneoId(10L);
        assertThat(result.getTorneoId()).isEqualTo(10L);
    }


    @Test
    @DisplayName("Debe guardar inscripcion correctamente (Sin sancion, todo OK)")
    public void shouldSaveInscripcionSuccessfully() {

        TorneoDTO torneoDTO = new TorneoDTO();
        torneoDTO.setFechaInicio(fechaFutura);
        when(torneoClient.findById(10L)).thenReturn(torneoDTO);


        when(sancionClient.findbyid(20L)).thenThrow(mock(FeignException.NotFound.class));


        when(equipoClient.findById(5L)).thenReturn(new EquipoDTO());
        when(usuarioClient.findById(20L)).thenReturn(new UsuarioDTO());


        when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(i -> i.getArgument(0));

        Inscripcion result = inscripcionService.save(inscripcionPrueba);

        assertThat(result.getEstado()).isEqualTo("ACTIVO");
        verify(inscripcionRepository, times(1)).save(inscripcionPrueba);
    }

    @Test
    @DisplayName("Debe lanzar excepcion si el torneo ya empezó")
    public void shouldThrowWhenTorneoStarted() {
        TorneoDTO torneoDTO = new TorneoDTO();

        torneoDTO.setFechaInicio(fechaPasada);
        inscripcionPrueba.setFechaInscripcion(fechaFutura);

        when(torneoClient.findById(10L)).thenReturn(torneoDTO);

        assertThatThrownBy(() -> inscripcionService.save(inscripcionPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El torneo ya empezó, no se pueden inscribir jugadores");

        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepcion si torneo no existe")
    public void shouldThrowWhenTorneoClientFails() {
        when(torneoClient.findById(10L)).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> inscripcionService.save(inscripcionPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El torneo con id 10 no existe");
    }

    @Test
    @DisplayName("Debe lanzar excepcion si jugador tiene sancion ACTIVA")
    public void shouldThrowWhenSancionActiva() {
        TorneoDTO torneoDTO = new TorneoDTO();
        torneoDTO.setFechaInicio(fechaFutura);
        when(torneoClient.findById(10L)).thenReturn(torneoDTO);

        SancionDTO sancionDTO = new SancionDTO();
        sancionDTO.setEstado("ACTIVA"); // Esto activa tu IF
        when(sancionClient.findbyid(20L)).thenReturn(sancionDTO);

        assertThatThrownBy(() -> inscripcionService.save(inscripcionPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El jugador tiene una sancion vigente, no puede inscribirse");
    }

    @Test
    @DisplayName("Debe lanzar excepcion si equipo no existe")
    public void shouldThrowWhenEquipoClientFails() {
        TorneoDTO torneoDTO = new TorneoDTO();
        torneoDTO.setFechaInicio(fechaFutura);
        when(torneoClient.findById(10L)).thenReturn(torneoDTO);

        when(sancionClient.findbyid(20L)).thenThrow(mock(FeignException.NotFound.class));
        when(equipoClient.findById(5L)).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> inscripcionService.save(inscripcionPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El equipo con id 5 no existe");
    }

    @Test
    @DisplayName("Debe lanzar excepcion si usuario no existe")
    public void shouldThrowWhenUsuarioClientFails() {
        TorneoDTO torneoDTO = new TorneoDTO();
        torneoDTO.setFechaInicio(fechaFutura);
        when(torneoClient.findById(10L)).thenReturn(torneoDTO);

        when(sancionClient.findbyid(20L)).thenThrow(mock(FeignException.NotFound.class));
        when(equipoClient.findById(5L)).thenReturn(new EquipoDTO());
        when(usuarioClient.findById(20L)).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> inscripcionService.save(inscripcionPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El usuario con id 20 no existe");
    }



    @Test
    @DisplayName("Debe actualizar una inscripcion existente")
    public void shouldUpdateInscripcion() {
        Long id = 1L;
        Inscripcion cambios = new Inscripcion();
        cambios.setEstado("CANCELADO");
        cambios.setTipoParticipante("SUPLENTE");
        cambios.setJugadorId(20L);
        cambios.setEquipoId(5L);
        cambios.setTorneoId(10L);

        when(inscripcionRepository.findById(id)).thenReturn(Optional.of(inscripcionPrueba));
        when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(inv -> inv.getArgument(0));

        Inscripcion result = inscripcionService.updateById(id, cambios);

        assertThat(result.getEstado()).isEqualTo("CANCELADO");
        assertThat(result.getTipoParticipante()).isEqualTo("SUPLENTE");
        verify(inscripcionRepository, times(1)).save(any(Inscripcion.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al actualizar ID inexistente")
    public void shouldThrowWhenUpdateIdNotFound() {
        when(inscripcionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inscripcionService.updateById(99L, new Inscripcion()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Inscripcion no encontrada");
    }

    @Test
    @DisplayName("Debe eliminar una inscripcion por su ID")
    public void shouldDeleteById() {
        inscripcionService.deleteById(1L);
        verify(inscripcionRepository, times(1)).deleteById(1L);
    }
}