package eSports_Arena_Manager.game_service.services;

import eSports_Arena_Manager.game_service.models.Game;

import java.util.List;

public interface GameService {
    List<Game> findAll();
    Game findById(Long id);
    Game findByGenero(String genero);
    Game findByModalidad(String modalidad);
    Game findByName(String name);
    Game findByEstado(String estado);
    Game save(Game game);
    void deleteByid(Long id);
    Game updateById(Long id, Game game);
}
