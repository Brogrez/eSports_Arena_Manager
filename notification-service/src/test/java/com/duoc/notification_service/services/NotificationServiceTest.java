package com.duoc.notification_service.services;

import com.duoc.notification_service.exceptions.NotificacionException;
import com.duoc.notification_service.models.Notificacion;
import com.duoc.notification_service.repositories.NotificacionRepository;
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
public class NotificationServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    private Notificacion notificacionPrueba;
    private List<Notificacion> notificacionList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker(Locale.of("es", "CL"));


        this.notificacionPrueba = new Notificacion();

        this.notificacionPrueba.setNotificacionId(1L);
        this.notificacionPrueba.setTipo("INVITACION_EQUIPO");
        this.notificacionPrueba.setEquipoId(10L);
        this.notificacionPrueba.setUsuarioId(5L);
        this.notificacionPrueba.setMensaje("Has sido invitado a unirte a Leviatan");
        this.notificacionPrueba.setLeido(false);

        String[] tipos = {"ALERTA_TORNEO", "INVITACION_EQUIPO", "RESULTADO_PARTIDA", "SANCION"};
        for (int i = 0; i < 15; i++) {
            Notificacion notif = new Notificacion();
            notif.setNotificacionId((long) (i + 2));
            notif.setTipo(tipos[i % tipos.length]);
            notif.setEquipoId(faker.number().numberBetween(1L, 20L));
            notif.setUsuarioId(faker.number().numberBetween(1L, 100L));
            notif.setMensaje(faker.lorem().sentence());
            notif.setLeido(i % 2 == 0);
            notificacionList.add(notif);
        }
    }


    @Test
    @DisplayName("Debe listar todas las notificaciones")
    public void shouldFindAll() {
        when(notificacionRepository.findAll()).thenReturn(notificacionList);

        List<Notificacion> result = notificacionService.findAll();

        assertThat(result).hasSize(15);
        verify(notificacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar una notificacion por su ID")
    public void shouldFindBynotificacionId() {
        Long id = 1L;

        when(notificacionRepository.findByNotificacionId(id)).thenReturn(Optional.of(notificacionPrueba));

        Notificacion result = notificacionService.findBynotificacionId(id);

        assertThat(result.getMensaje()).isEqualTo("Has sido invitado a unirte a Leviatan");
        verify(notificacionRepository, times(1)).findByNotificacionId(id);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar ID inexistente")
    public void shouldThrowWhenIdNotFound() {
        when(notificacionRepository.findByNotificacionId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.findBynotificacionId(99L))
                .isInstanceOf(NotificacionException.class)
                .hasMessage("Notificacion no encontrado");
    }

    @Test
    @DisplayName("Debe buscar una notificacion por ID de usuario")
    public void shouldFindByUsuarioId() {
        Long usuarioId = 5L;
        when(notificacionRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(notificacionPrueba));

        Notificacion result = notificacionService.findByUsuarioId(usuarioId);

        assertThat(result.getUsuarioId()).isEqualTo(usuarioId);
        verify(notificacionRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar usuario inexistente")
    public void shouldThrowWhenUsuarioIdNotFound() {
        when(notificacionRepository.findByUsuarioId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.findByUsuarioId(99L))
                .isInstanceOf(NotificacionException.class)
                .hasMessage("Usuario no encontrado");
    }

    @Test
    @DisplayName("Debe buscar una notificacion por ID de equipo")
    public void shouldFindByEquipoId() {
        Long equipoId = 10L;
        when(notificacionRepository.findByEquipoId(equipoId)).thenReturn(Optional.of(notificacionPrueba));

        Notificacion result = notificacionService.findByEquipoId(equipoId);

        assertThat(result.getEquipoId()).isEqualTo(equipoId);
        verify(notificacionRepository, times(1)).findByEquipoId(equipoId);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar equipo inexistente")
    public void shouldThrowWhenEquipoIdNotFound() {
        when(notificacionRepository.findByEquipoId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.findByEquipoId(99L))
                .isInstanceOf(NotificacionException.class)
                .hasMessage("Equipo no encontrado");
    }


    @Test
    @DisplayName("Debe guardar una notificacion")
    public void shouldSaveNotificacion() {
        when(notificacionRepository.save(notificacionPrueba)).thenReturn(notificacionPrueba);

        Notificacion result = notificacionService.save(notificacionPrueba);

        assertThat(result.getMensaje()).isEqualTo("Has sido invitado a unirte a Leviatan");
        verify(notificacionRepository, times(1)).save(notificacionPrueba);
    }

    @Test
    @DisplayName("Debe eliminar una notificacion por su ID")
    public void shouldDeleteBynotificacionId() {
        Long id = 1L;
        notificacionService.deleteBynotificacionId(id);
        verify(notificacionRepository, times(1)).deleteById(id);
    }



    @Test
    @DisplayName("Debe actualizar una notificacion existente")
    public void shouldUpdateNotificacion() {
        Long id = 1L;
        Notificacion cambios = new Notificacion();
        cambios.setTipo("RESULTADO_PARTIDA");
        cambios.setEquipoId(12L);
        cambios.setMensaje("Tu equipo ha ganado el encuentro");
        cambios.setUsuarioId(5L);
        cambios.setLeido(true);

        when(notificacionRepository.findByNotificacionId(anyLong())).thenReturn(Optional.of(notificacionPrueba));
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(inv -> inv.getArgument(0));

        Notificacion result = notificacionService.update(id, cambios);

        assertThat(result.getTipo()).isEqualTo("RESULTADO_PARTIDA");
        assertThat(result.getMensaje()).isEqualTo("Tu equipo ha ganado el encuentro");
        assertThat(result.isLeido()).isTrue();

        verify(notificacionRepository, times(1)).findByNotificacionId(id);
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al actualizar notificacion inexistente")
    public void shouldThrowWhenUpdatingNonExistingNotif() {
        when(notificacionRepository.findByNotificacionId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.update(99L, new Notificacion()))
                .isInstanceOf(NotificacionException.class)
                .hasMessage("notificacion no encontrada");

        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    @DisplayName("Debe marcar una notificacion como leida")
    public void shouldMarcarComoLeido() {
        Long id = 1L;
        // Verificamos que arranca en false
        assertThat(notificacionPrueba.isLeido()).isFalse();

        when(notificacionRepository.findByNotificacionId(anyLong())).thenReturn(Optional.of(notificacionPrueba));
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(inv -> inv.getArgument(0));

        Notificacion result = notificacionService.marcarComoLeido(id);

        assertThat(result.isLeido()).isTrue();
        verify(notificacionRepository, times(1)).findByNotificacionId(id);
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al marcar como leida una notif inexistente")
    public void shouldThrowWhenMarcarLeidoNonExisting() {
        when(notificacionRepository.findByNotificacionId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.marcarComoLeido(99L))
                .isInstanceOf(NotificacionException.class)
                .hasMessage("Notificación no encontrada");

        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

}