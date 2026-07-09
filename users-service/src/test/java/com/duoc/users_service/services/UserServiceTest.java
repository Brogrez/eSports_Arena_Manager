package com.duoc.users_service.services;

import com.duoc.users_service.exceptions.UserException;
import com.duoc.users_service.models.User;
import com.duoc.users_service.repositories.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User userPrueba;
    private List<User> userList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker(Locale.of("es", "CL"));

        this.userPrueba = new User();
        this.userPrueba.setUserId(1L);
        this.userPrueba.setName("Lee Sang-hyeok");
        this.userPrueba.setNickname("Faker");
        this.userPrueba.setEmail("faker@t1.gg");
        this.userPrueba.setRol("PLAYER");
        this.userPrueba.setEstado("ACTIVO");
        this.userPrueba.setFechaRegistro(LocalDate.now().minusDays(10));

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUserId((long) (i + 2));
            user.setName(faker.name().fullName());
            user.setNickname(faker.esports().player());
            user.setEmail(faker.internet().emailAddress());
            user.setRol(i % 2 == 0 ? "PLAYER" : "ADMIN");
            user.setEstado("ACTIVO");
            userList.add(user);
        }
    }

    // ==========================================
    // TESTS DE BÚSQUEDA (READ)
    // ==========================================

    @Test
    @DisplayName("Debe listar todos los usuarios")
    public void shouldFindAll() {
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.findAll();

        assertThat(result).hasSize(5);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar un usuario por su ID")
    public void shouldFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userPrueba));

        User result = userService.findById(1L);

        assertThat(result.getUserId()).isEqualTo(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar ID inexistente")
    public void shouldThrowWhenIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User no encontrado");
    }

    @Test
    @DisplayName("Debe buscar un usuario por su Nickname")
    public void shouldFindByNickname() {
        String nickname = "Faker";
        when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(userPrueba));

        User result = userService.findByNickname(nickname);

        assertThat(result.getNickname()).isEqualTo(nickname);
        verify(userRepository, times(1)).findByNickname(nickname);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar Nickname inexistente")
    public void shouldThrowWhenNicknameNotFound() {
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByNickname("Ghost"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User no encontrado");
    }

    @Test
    @DisplayName("Debe buscar un usuario por su Email")
    public void shouldFindByEmail() {
        String email = "faker@t1.gg";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userPrueba));

        User result = userService.findByEmail(email);

        assertThat(result.getEmail()).isEqualTo(email);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar Email inexistente")
    public void shouldThrowWhenEmailNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail("fake@mail.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User no encontrado");
    }

    // ==========================================
    // TESTS DE CREACIÓN (SAVE)
    // ==========================================

    @Test
    @DisplayName("Debe guardar un usuario exitosamente")
    public void shouldSaveUserSuccessfully() {
        when(userRepository.findByEmail(userPrueba.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(userPrueba.getNickname())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        userPrueba.setEstado(null);
        userPrueba.setFechaRegistro(null);

        User result = userService.save(userPrueba);

        assertThat(result.getEstado()).isEqualTo("ACTIVO");
        assertThat(result.getFechaRegistro()).isEqualTo(LocalDate.now());
        verify(userRepository, times(1)).save(userPrueba);
    }

    @Test
    @DisplayName("Debe lanzar excepcion si el Email ya existe")
    public void shouldThrowWhenSavingExistingEmail() {
        when(userRepository.findByEmail(userPrueba.getEmail())).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.save(userPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User ya existente");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion si el Nickname ya existe")
    public void shouldThrowWhenSavingExistingNickname() {
        when(userRepository.findByEmail(userPrueba.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(userPrueba.getNickname())).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.save(userPrueba))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User ya existente"); // Actualizado a tu nueva corrección

        verify(userRepository, never()).save(any(User.class));
    }

    // ==========================================
    // TESTS DE ACTUALIZACIÓN Y BORRADO
    // ==========================================

    @Test
    @DisplayName("Debe actualizar un usuario existente")
    public void shouldUpdateUser() {
        Long id = 1L;
        User cambios = new User();
        cambios.setEmail("newemail@t1.gg");
        cambios.setName("Lee Sang");
        cambios.setRol("COACH");
        cambios.setEstado("INACTIVO");

        when(userRepository.findById(id)).thenReturn(Optional.of(userPrueba));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateById(id, cambios);

        assertThat(result.getEmail()).isEqualTo("newemail@t1.gg");
        assertThat(result.getRol()).isEqualTo("COACH");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion al actualizar ID inexistente")
    public void shouldThrowWhenUpdateIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateById(99L, new User()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User no encontrado"); // Actualizado a tu corrección
    }

    @Test
    @DisplayName("Debe eliminar un usuario por su ID")
    public void shouldDeleteById() {
        userService.deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}