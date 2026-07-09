package eSports_Arena_Manager.game_service.services;

import eSports_Arena_Manager.game_service.exceptions.GameException;
import eSports_Arena_Manager.game_service.models.Game;
import eSports_Arena_Manager.game_service.repositories.GameRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    private Game gamePrueba;
    private List<Game> gameList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker(Locale.of("es", "CL"));


        this.gamePrueba = new Game();
        this.gamePrueba.setGameId(1L);
        this.gamePrueba.setName("Valorant");
        this.gamePrueba.setGenero("Tactical Shooter");
        this.gamePrueba.setModalidad("5v5");
        this.gamePrueba.setJugadoresPorEquipo(5);
        this.gamePrueba.setEstado("ACTIVO");

        String[] generos = {"MOBA", "Hero Shooter", "Battle Royale", "Fighting", "RTS"};
        for (int i = 0; i < 15; i++) {
            Game game = new Game();
            game.setGameId((long) (i + 2));
            game.setName(faker.esports().game());
            game.setGenero(generos[i % generos.length]);
            game.setModalidad("Team-based");
            game.setJugadoresPorEquipo(faker.number().numberBetween(1, 6));
            game.setEstado(i % 2 == 0 ? "ACTIVO" : "INACTIVO");
            gameList.add(game);
        }
    }


    @Test
    @DisplayName("Debe listar todos los juegos")
    public void shouldFindAll() {
        when(gameRepository.findAll()).thenReturn(gameList);

        List<Game> result = gameService.findAll();

        assertThat(result).hasSize(15);
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar un juego por su ID")
    public void shouldFindById() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(gamePrueba));

        Game result = gameService.findById(1L);

        assertThat(result.getName()).isEqualTo("Valorant");
        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar ID inexistente")
    public void shouldThrowWhenIdNotFound() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.findById(99L))
                .isInstanceOf(GameException.class)
                .hasMessage("game no encontrado");
    }

    @Test
    @DisplayName("Debe buscar un juego por su género")
    public void shouldFindByGenero() {
        String genero = "Tactical Shooter";
        when(gameRepository.findByGenero(genero)).thenReturn(Optional.of(gamePrueba));

        Game result = gameService.findByGenero(genero);

        assertThat(result.getGenero()).isEqualTo(genero);
        verify(gameRepository, times(1)).findByGenero(genero);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar género inexistente")
    public void shouldThrowWhenGeneroNotFound() {
        when(gameRepository.findByGenero(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.findByGenero("Inexistente"))
                .isInstanceOf(GameException.class)
                .hasMessage("genero no encontrado");
    }

    @Test
    @DisplayName("Debe buscar un juego por su modalidad")
    public void shouldFindByModalidad() {
        String modalidad = "5v5";
        when(gameRepository.findByModalidad(modalidad)).thenReturn(Optional.of(gamePrueba));

        Game result = gameService.findByModalidad(modalidad);

        assertThat(result.getModalidad()).isEqualTo(modalidad);
        verify(gameRepository, times(1)).findByModalidad(modalidad);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar modalidad inexistente")
    public void shouldThrowWhenModalidadNotFound() {
        when(gameRepository.findByModalidad(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.findByModalidad("Random"))
                .isInstanceOf(GameException.class)
                .hasMessage("modalidad no encontrado");
    }

    @Test
    @DisplayName("Debe buscar un juego por su nombre")
    public void shouldFindByName() {
        String name = "Valorant";
        when(gameRepository.findByName(name)).thenReturn(Optional.of(gamePrueba));

        Game result = gameService.findByName(name);

        assertThat(result.getName()).isEqualTo(name);
        verify(gameRepository, times(1)).findByName(name);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar nombre inexistente")
    public void shouldThrowWhenNameNotFound() {
        when(gameRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.findByName("Juego Fake"))
                .isInstanceOf(GameException.class)
                .hasMessage("name no encontrado");
    }

    @Test
    @DisplayName("Debe buscar un juego por su estado")
    public void shouldFindByEstado() {
        String estado = "ACTIVO";
        when(gameRepository.findByEstado(estado)).thenReturn(Optional.of(gamePrueba));

        Game result = gameService.findByEstado(estado);

        assertThat(result.getEstado()).isEqualTo(estado);
        verify(gameRepository, times(1)).findByEstado(estado);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al buscar estado inexistente")
    public void shouldThrowWhenEstadoNotFound() {
        when(gameRepository.findByEstado(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.findByEstado("PENDIENTE"))
                .isInstanceOf(GameException.class)
                .hasMessage("estados no encontrado");
    }


    @Test
    @DisplayName("Debe guardar un juego pasando todas las validaciones")
    public void shouldSaveGame() {

        when(gameRepository.existsByName(gamePrueba.getName())).thenReturn(false);
        when(gameRepository.save(any(Game.class))).thenAnswer(inv -> inv.getArgument(0));


        gamePrueba.setEstado(null);

        Game result = gameService.save(gamePrueba);

        assertThat(result.getEstado()).isEqualTo("ACTIVO");
        verify(gameRepository, times(1)).save(gamePrueba);
    }

    @Test
    @DisplayName("Debe lanzar excepcion si el nombre del juego ya existe")
    public void shouldThrowWhenSavingExistingName() {
        when(gameRepository.existsByName(gamePrueba.getName())).thenReturn(true);

        assertThatThrownBy(() -> gameService.save(gamePrueba))
                .isInstanceOf(GameException.class)
                .hasMessage("ya existe este juego");

        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion si jugadores por equipo es negativo")
    public void shouldThrowWhenJugadoresNegativos() {
        when(gameRepository.existsByName(gamePrueba.getName())).thenReturn(false);

        gamePrueba.setJugadoresPorEquipo(-1);

        assertThatThrownBy(() -> gameService.save(gamePrueba))
                .isInstanceOf(GameException.class)
                .hasMessage("la cantidad de jugadores debe ser mayor a 0");

        verify(gameRepository, never()).save(any(Game.class));
    }



    @Test
    @DisplayName("Debe actualizar un juego existente")
    public void shouldUpdateGame() {
        Long id = 1L;
        Game cambios = new Game();
        cambios.setName("Counter Strike 2");
        cambios.setGenero("FPS");
        cambios.setModalidad("5v5");
        cambios.setJugadoresPorEquipo(5);
        cambios.setEstado("INACTIVO");

        when(gameRepository.findById(id)).thenReturn(Optional.of(gamePrueba));
        when(gameRepository.save(any(Game.class))).thenAnswer(inv -> inv.getArgument(0));

        Game result = gameService.updateById(id, cambios);

        assertThat(result.getName()).isEqualTo("Counter Strike 2");
        assertThat(result.getEstado()).isEqualTo("INACTIVO");
        verify(gameRepository, times(1)).findById(id);
        verify(gameRepository, times(1)).save(gamePrueba);
    }

    @Test
    @DisplayName("Debe lanzar excepcion al actualizar juego inexistente")
    public void shouldThrowWhenUpdatingNonExistingGame() {
        when(gameRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.updateById(99L, new Game()))
                .isInstanceOf(GameException.class)
                .hasMessage("game no encontrado");

        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    @DisplayName("Debe eliminar un juego por su ID")
    public void shouldDeleteById() {
        Long id = 1L;

        gameService.deleteByid(id);
        verify(gameRepository, times(1)).deleteById(id);
    }
}