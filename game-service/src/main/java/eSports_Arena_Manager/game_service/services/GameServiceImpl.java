package eSports_Arena_Manager.game_service.services;

import eSports_Arena_Manager.game_service.exceptions.GameException;
import eSports_Arena_Manager.game_service.models.Game;
import eSports_Arena_Manager.game_service.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Game> findAll() {
        return this.gameRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Game findById(Long id) {
        return this.gameRepository.findById(id).orElseThrow(
                () -> new GameException("game no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Game findByGenero(String genero) {
        return this.gameRepository.findByGenero(genero).orElseThrow(
                () -> new GameException("genero no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Game findByModalidad(String modalidad) {
        return this.gameRepository.findByModalidad(modalidad).orElseThrow(
                () -> new GameException("modalidad no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Game findByName(String name) {
        return this.gameRepository.findByName(name).orElseThrow(
                () -> new GameException("name no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Game findByEstado(String estado) {
        return this.gameRepository.findByEstado(estado).orElseThrow(
                () -> new GameException("estados no encontrado")
        );
    }

    @Transactional
    @Override
    public Game save(Game game) {
        if(this.gameRepository.existsByName(game.getName())){
            throw new GameException("ya existe este juego");
        }
        if(game.getJugadoresPorEquipo() < 0){
            throw new GameException("la cantidad de jugadores debe ser mayor a 0");
        }
        game.setEstado("ACTIVO");
        return this.gameRepository.save(game);
    }

    @Override
    public void deleteByid(Long id) {
        this.gameRepository.deleteById(id);
    }

    @Override
    public Game updateById(Long id, Game game) {
        return this.gameRepository.findById(id).map(e -> {
            e.setName(game.getName());
            e.setGenero(game.getGenero());
            e.setModalidad(game.getModalidad());
            e.setJugadoresPorEquipo(game.getJugadoresPorEquipo());
            e.setEstado(game.getEstado());
            return this.gameRepository.save(e);
        }).orElseThrow(
                () -> new GameException("game no encontrado")
        );
    }
}
