package com.duoc.sanction_service.services;

import eSports_Arena_Manager.sanction_service.clients.TeamClient;
import eSports_Arena_Manager.sanction_service.clients.UserClient;
import eSports_Arena_Manager.sanction_service.models.Sanction;
import eSports_Arena_Manager.sanction_service.repositories.SanctionRepository;
import eSports_Arena_Manager.sanction_service.services.SanctionServiceImpl;
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
public class SanctionServiceTest {

    @Mock
    private SanctionRepository sanctionRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private TeamClient teamClient;

    @InjectMocks
    private SanctionServiceImpl sanctionService;

    private Sanction sanctionPrueba;
    private List<Sanction> sanctionList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker(Locale.of("es", "CL"));

        this.sanctionPrueba = new Sanction();
        this.sanctionPrueba.setSancionId(1L);
        this.sanctionPrueba.setUsuarioId(10L);
        this.sanctionPrueba.setEquipoId(5L); // Con equipo asignado
        this.sanctionPrueba.setMotivo("Uso de software de terceros (Hacks)");
        this.sanctionPrueba.setSeveridad("ALTA");
        this.sanctionPrueba.setEstado("PENDIENTE");
        this.sanctionPrueba.setFechaInicio(LocalDate.now());
        this.sanctionPrueba.setFechaFin(LocalDate.now().plusDays(30));

        for (int i = 0; i < 5; i++) {
            Sanction sancion = new Sanction();
            sancion.setSancionId((long) (i + 2));
            sancion.setUsuarioId(faker.number().numberBetween(1L, 100L));
            sancion.setEquipoId(i % 2 == 0 ? faker.number().numberBetween(1L, 20L) : null);
            sancion.setEstado(i % 2 == 0 ? "ACTIVA" : "CERRADA");
            sanctionList.add(sancion);
        }
    }


    @Test
    @DisplayName("Debe listar todas las sanciones")
    public void shouldFindAll() {
        when(sanctionRepository.findAll()).thenReturn(sanctionList);

        List<Sanction> result = sanctionService.findAll();

        assertThat(result).hasSize(5);
        verify(sanctionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar una sancion por su ID")
    public void shouldFindById() {
        when(sanctionRepository.findById(1L)).thenReturn(Optional.of(sanctionPrueba));

        Sanction result = sanctionService.findById(1L);

        assertThat(result.getSancionId()).isEqualTo(1L);
        verify(sanctionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar ID inexistente")
    public void shouldThrowWhenIdNotFound() {
        when(sanctionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sanctionService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("ESA SANCION NO EXISTE");
    }

    @Test
    @DisplayName("Debe buscar sanciones por Usuario ID")
    public void shouldFindByUsuarioId() {
        when(sanctionRepository.findByUsuarioId(10L)).thenReturn(List.of(sanctionPrueba));
        List<Sanction> result = sanctionService.findByUsuarioId(10L);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Debe buscar sanciones por Team ID")
    public void shouldFindByTeamId() {
        when(sanctionRepository.findByEquipoId(5L)).thenReturn(List.of(sanctionPrueba));
        List<Sanction> result = sanctionService.findByTeamId(5L);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Debe buscar sanciones por Estado")
    public void shouldFindByEstado() {
        when(sanctionRepository.findByEstado("ACTIVA")).thenReturn(List.of(sanctionPrueba));
        List<Sanction> result = sanctionService.findByEstado("ACTIVA");
        assertThat(result).hasSize(1);
    }



    @Test
    @DisplayName("Debe guardar sancion CON equipo")
    public void shouldSaveSanctionWithTeam() {
        when(sanctionRepository.save(any(Sanction.class))).thenAnswer(i -> i.getArgument(0));

        Sanction result = sanctionService.save(sanctionPrueba);

        assertThat(result.getEstado()).isEqualTo("ACTIVA");
        verify(userClient, times(1)).findById(10L);
        verify(teamClient, times(1)).findById(5L);
        verify(sanctionRepository, times(1)).save(sanctionPrueba);
    }

    @Test
    @DisplayName("Debe guardar sancion SIN equipo")
    public void shouldSaveSanctionWithoutTeam() {
        sanctionPrueba.setEquipoId(null);

        when(sanctionRepository.save(any(Sanction.class))).thenAnswer(i -> i.getArgument(0));

        Sanction result = sanctionService.save(sanctionPrueba);

        assertThat(result.getEstado()).isEqualTo("ACTIVA");
        verify(userClient, times(1)).findById(10L);
        verify(teamClient, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Debe lanzar excepcion si usuario no existe en guardado")
    public void shouldThrowWhenUserNotFoundInSave() {
        when(userClient.findById(anyLong())).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> sanctionService.save(sanctionPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("EL USUARIO NO EXISTE");
    }

    @Test
    @DisplayName("Debe lanzar excepcion si equipo no existe en guardado")
    public void shouldThrowWhenTeamNotFoundInSave() {
        when(teamClient.findById(anyLong())).thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> sanctionService.save(sanctionPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("EL EQUIPO NO EXISTE");
    }

    @Test
    @DisplayName("Debe lanzar excepcion si la fecha fin es antes que la inicio")
    public void shouldThrowWhenFechaFinBeforeInicio() {
        sanctionPrueba.setFechaInicio(LocalDate.now());
        sanctionPrueba.setFechaFin(LocalDate.now().minusDays(5));

        assertThatThrownBy(() -> sanctionService.save(sanctionPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("LA FECHA FIN DEBE SER POSTERIOR A LA FECHA INICIO");
    }



    @Test
    @DisplayName("Debe actualizar una sancion existente")
    public void shouldUpdateById() {
        Long id = 1L;
        Sanction cambios = new Sanction();
        cambios.setMotivo("Toxicidad en chat");
        cambios.setFechaFin(LocalDate.now().plusDays(10));
        cambios.setSeveridad("BAJA");
        cambios.setEstado("ACTIVA");

        when(sanctionRepository.findById(id)).thenReturn(Optional.of(sanctionPrueba));
        when(sanctionRepository.save(any(Sanction.class))).thenAnswer(inv -> inv.getArgument(0));

        Sanction result = sanctionService.updateById(id, cambios);

        assertThat(result.getMotivo()).isEqualTo("Toxicidad en chat");
        assertThat(result.getSeveridad()).isEqualTo("BAJA");
        verify(sanctionRepository, times(1)).save(any(Sanction.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al actualizar ID inexistente")
    public void shouldThrowWhenUpdateIdNotFound() {
        when(sanctionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sanctionService.updateById(99L, new Sanction()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("sancion no existe");
    }

    @Test
    @DisplayName("Debe cerrar una sancion correctamente")
    public void shouldCerrarSanction() {
        Long id = 1L;
        sanctionPrueba.setEstado("ACTIVA");

        when(sanctionRepository.findById(id)).thenReturn(Optional.of(sanctionPrueba));
        when(sanctionRepository.save(any(Sanction.class))).thenAnswer(inv -> inv.getArgument(0));

        Sanction result = sanctionService.cerrar(id);

        assertThat(result.getEstado()).isEqualTo("CERRADA");
    }

    @Test
    @DisplayName("Debe lanzar excepcion al cerrar una sancion ya cerrada")
    public void shouldThrowWhenCerrarAlreadyCerrada() {
        Long id = 1L;
        sanctionPrueba.setEstado("CERRADA");

        when(sanctionRepository.findById(id)).thenReturn(Optional.of(sanctionPrueba));

        assertThatThrownBy(() -> sanctionService.cerrar(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("la sancion ya esta cerrada");
    }

    @Test
    @DisplayName("Debe eliminar una sancion por su ID")
    public void shouldDeleteById() {

        sanctionService.deleteById(1L);

        verify(sanctionRepository, times(1)).deleteById(1L);
    }
}