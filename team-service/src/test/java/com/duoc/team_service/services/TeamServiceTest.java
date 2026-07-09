package com.duoc.team_service.services;

import com.duoc.team_service.clients.GameClient;
import com.duoc.team_service.clients.UsuarioClient;
import com.duoc.team_service.models.Equipo;
import com.duoc.team_service.models.MiembroEquipo;
import com.duoc.team_service.models.dtos.EquipoDTO;
import com.duoc.team_service.models.dtos.GameDTO;
import com.duoc.team_service.models.dtos.MiembroEquipoDTO;
import com.duoc.team_service.models.dtos.UsuarioDTO;
import com.duoc.team_service.repositories.EquipoRepository;
import com.duoc.team_service.repositories.MiembroEquipoRepository;
import feign.FeignException;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {


    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private MiembroEquipoRepository miembroEquipoRepository;


    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private GameClient gameClient;

    @InjectMocks
    private EquipoServiceImpl equipoService;

    @InjectMocks
    private MiembroEquipoServiceImpl miembroEquipoService;


    private Equipo equipoPrueba;
    private MiembroEquipo miembroPrueba;
    private List<Equipo> equipoList = new ArrayList<>();
    private List<MiembroEquipo> miembroList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker(Locale.of("es", "CL"));

        this.equipoPrueba = new Equipo();
        this.equipoPrueba.setEquipoId(1L);
        this.equipoPrueba.setNombreEquipo("Furious Gaming");
        this.equipoPrueba.setEstado("INSCRITO");
        this.equipoPrueba.setCapitanId(10L);
        this.equipoPrueba.setJuegoPrincipalId(5L);

        for (int i = 0; i < 50; i++) {
            Equipo equipo = new Equipo();
            equipo.setEquipoId(((long) (i + 2)));
            equipo.setNombreEquipo(faker.esports().team());
            equipo.setEstado(i % 2 == 0 ? "INSCRITO" : "ELIMINADO");
            equipo.setCapitanId(faker.number().numberBetween(1L, 100L));
            equipo.setJuegoPrincipalId(faker.number().numberBetween(1L, 20L));
            equipoList.add(equipo);
        }


        this.miembroPrueba = new MiembroEquipo();
        this.miembroPrueba.setMiembroId(1L);
        this.miembroPrueba.setUsuarioId(10L);
        this.miembroPrueba.setMEquipoId(1L);
        this.miembroPrueba.setRolDentroEquipo("IGL (In-Game Leader)");

        String[] rolesEsports = {"Entry Fragger", "Support", "Lurker", "Coach", "IGL"};
        for (int i = 0; i < 50; i++) {
            MiembroEquipo miembro = new MiembroEquipo();
            miembro.setUsuarioId(faker.number().numberBetween(1L, 100L));
            miembro.setMEquipoId(faker.number().numberBetween(1L, 50L));
            miembro.setRolDentroEquipo(rolesEsports[i % rolesEsports.length]);
            miembroList.add(miembro);
        }
    }



    @Test
    @DisplayName("Debe listar todos los equipos validando la existencia del juego")
    public void shouldFindAllEquipos() {

        when(equipoRepository.findAll()).thenReturn(List.of(equipoPrueba));

        GameDTO mockGame = new GameDTO();
        when(gameClient.findById(5L)).thenReturn(mockGame);

        List<EquipoDTO> result = equipoService.findAll();

        assertThat(result).hasSize(1);
        EquipoDTO dto = result.get(0);

        assertThat(dto.getNombreEquipo()).isEqualTo("Furious Gaming");
        assertThat(dto.getEstado()).isEqualTo("INSCRITO");


        assertThat(dto.getCapitanId()).isEqualTo(10L);
        assertThat(dto.getJuegoPrincipalId()).isEqualTo(5L);


        verify(equipoRepository, times(1)).findAll();
        verify(gameClient, times(1)).findById(5L);


    }

    @Test
    @DisplayName("Debe buscar un equipo por el id del capitan")
    public void shouldFindByCapitanId() {
        Long capitanId = 10L;
        when(equipoRepository.findByCapitanId(capitanId)).thenReturn(Optional.of(equipoPrueba));

        Equipo result = equipoService.findByCapitanId(capitanId);

        assertThat(result.getCapitanId()).isEqualTo(capitanId);
        verify(equipoRepository, times(1)).findByCapitanId(capitanId);
    }

    @Test
    @DisplayName("Debe buscar un equipo por su estado en el torneo")
    public void shouldFindByEstado() {
        String estado = "INSCRITO";
        when(equipoRepository.findByEstado(estado)).thenReturn(Optional.of(equipoPrueba));

        Equipo result = equipoService.findByEstado(estado);

        assertThat(result.getEstado()).isEqualTo(estado);
        verify(equipoRepository, times(1)).findByEstado(estado);
    }

    @Test
    @DisplayName("Debe buscar un equipo por su ID")
    public void shouldFindByEquipoId() {

        Long equipoId = 1L;

        when(equipoRepository.findByEquipoId(anyLong())).thenReturn(Optional.of(equipoPrueba));

        Equipo result = equipoService.findByEquipoId(equipoId);

        assertThat(result.getNombreEquipo()).isEqualTo("Furious Gaming");
        verify(equipoRepository, times(1)).findByEquipoId(equipoId);
    }
    @Test
    @DisplayName("Debe guardar un equipo validando capitan en el MS Usuarios")
    public void shouldSaveEquipo() {
        when(equipoRepository.save(equipoPrueba)).thenReturn(equipoPrueba);

        Equipo result = equipoService.save(equipoPrueba);

        assertThat(result).isNotNull();
        verify(equipoRepository, times(1)).save(equipoPrueba);
    }

    @Test
    @DisplayName("Debe actualizar un equipo existente")
    public void shouldUpdateEquipo() {
        Long id = 1L;
        Equipo cambios = new Equipo();
        cambios.setNombreEquipo("Leviatan");

        when(equipoRepository.findById(id)).thenReturn(Optional.of(equipoPrueba));
        when(equipoRepository.save(any(Equipo.class))).thenAnswer(inv -> inv.getArgument(0));

        Equipo result = equipoService.update(id, cambios);

        assertThat(result.getNombreEquipo()).isEqualTo("Leviatan");
        verify(equipoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debe eliminar un equipo por su ID")
    public void shouldDeletebyId() {
        Long id = 1L;
        equipoService.deletebyId(id);
        verify(equipoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Debe buscar un equipo por su nombre")
    public void shouldFindByNombreEquipo() {
        String nombreEquipo = "Furious Gaming";
        when(equipoRepository.findByNombreEquipo(nombreEquipo)).thenReturn(Optional.of(equipoPrueba));

        Equipo result = equipoService.findByNombreEquipo(nombreEquipo);

        assertThat(result.getNombreEquipo()).isEqualTo(nombreEquipo);
        verify(equipoRepository, times(1)).findByNombreEquipo(nombreEquipo);
    }

    @Test
    @DisplayName("Debe listar equipos por ID de juego principal")
    public void shouldFindByJuegoPrincipalId() {
        Long juegoId = 5L;
        when(equipoRepository.findByJuegoPrincipalId(juegoId)).thenReturn(equipoList);

        List<Equipo> result = equipoService.findByJuegoPrincipalId(juegoId);

        assertThat(result).hasSize(50);
        verify(equipoRepository, times(1)).findByJuegoPrincipalId(juegoId);
    }



    @Test
    @DisplayName("Debe listar todos los miembros de equipo enriquecidos")
    public void shouldFindAllMiembros() {
        when(miembroEquipoRepository.findAll()).thenReturn(List.of(miembroPrueba));

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(10L);
        usuarioDTO.setNickname("TenZ");
        when(usuarioClient.findById(10L)).thenReturn(usuarioDTO);

        List<MiembroEquipoDTO> result = miembroEquipoService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRolEnEquipo()).isEqualTo("IGL (In-Game Leader)");
        verify(miembroEquipoRepository, times(1)).findAll();
        verify(usuarioClient, times(1)).findById(10L);
    }

    @Test
    @DisplayName("Debe listar miembros por ID de usuario")
    public void shouldFindByUsuarioId() {

        Long usuarioId = 10L;

        when(miembroEquipoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(miembroPrueba));

        List<MiembroEquipo> result = miembroEquipoService.findByUsuarioId(usuarioId);

        assertThat(result).hasSize(1);
        verify(miembroEquipoRepository, times(1)).findByUsuarioId(usuarioId);
    }
    @Test
    @DisplayName("Debe buscar un miembro por su rol ingame")
    public void shouldFindByRolDentroEquipo() {
        String rolDentroEquipo = "IGL (In-Game Leader)";
        when(miembroEquipoRepository.findByRolDentroEquipo(rolDentroEquipo)).thenReturn(Optional.of(miembroPrueba));

        MiembroEquipo result = miembroEquipoService.findByRolDentroEquipo(rolDentroEquipo);

        assertThat(result.getRolDentroEquipo()).isEqualTo(rolDentroEquipo);
        verify(miembroEquipoRepository, times(1)).findByRolDentroEquipo(rolDentroEquipo);
    }

    @Test
    @DisplayName("Debe buscar un miembro por ID de equipo")
    public void shouldFindBymEquipoId() {
        Long equipoId = 1L;
        when(miembroEquipoRepository.findBymEquipoId(equipoId)).thenReturn(Optional.of(miembroPrueba));

        MiembroEquipo result = miembroEquipoService.findBymEquipoId(equipoId);

        assertThat(result.getMEquipoId()).isEqualTo(equipoId);
        verify(miembroEquipoRepository, times(1)).findBymEquipoId(equipoId);
    }

    @Test
    @DisplayName("Debe guardar un miembro de equipo válido")
    public void shouldSaveMiembroEquipo() {
        when(miembroEquipoRepository.save(miembroPrueba)).thenReturn(miembroPrueba);

        MiembroEquipo result = miembroEquipoService.save(miembroPrueba);

        assertThat(result).isNotNull();
        assertThat(result.getUsuarioId()).isEqualTo(10L);
        verify(miembroEquipoRepository, times(1)).save(miembroPrueba);
    }

    @Test
    @DisplayName("Debe actualizar un miembro de equipo existente")
    public void shouldUpdateMiembroEquipo() {
        Long miembroEquipoId = 1L;

        MiembroEquipo cambios = new MiembroEquipo();
        cambios.setMiembroId(miembroEquipoId);
        cambios.setRolDentroEquipo("Coach");
        cambios.setUsuarioId(15L);
        cambios.setMEquipoId(2L);

        when(miembroEquipoRepository.findByMiembroId(anyLong())).thenReturn(Optional.of(miembroPrueba));
        when(miembroEquipoRepository.save(any(MiembroEquipo.class))).thenAnswer(inv -> inv.getArgument(0));

        MiembroEquipo result = miembroEquipoService.update(miembroEquipoId, cambios);

        assertThat(result.getRolDentroEquipo()).isEqualTo("Coach");

        verify(miembroEquipoRepository, times(1)).findByMiembroId(miembroEquipoId);
        verify(miembroEquipoRepository, times(1)).save(any(MiembroEquipo.class));
    }

    @Test
    @DisplayName("Debe eliminar un miembro de equipo pasando la entidad")
    public void shouldDeleteByMiembroId() {
        doNothing().when(miembroEquipoRepository).delete(miembroPrueba);

        miembroEquipoService.deleteByMiembroId(miembroPrueba);

        verify(miembroEquipoRepository, times(1)).delete(miembroPrueba);
    }
}

