package eSports_Arena_Manager.game_service.repositories;

import eSports_Arena_Manager.game_service.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByGenero(String genero);
    Optional<Game> findByModalidad(String modalidad);
    Optional<Game> findByName(String name);
    Optional<Game> findByEstado(String estado);
    boolean existsByName(String name);
}
