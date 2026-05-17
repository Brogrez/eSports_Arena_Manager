package eSports_Arena_Manager.sanction_service.services;

import eSports_Arena_Manager.sanction_service.Clients.UserClient;
import eSports_Arena_Manager.sanction_service.DTO.UserDTO;
import eSports_Arena_Manager.sanction_service.exceptions.SanctionException;
import eSports_Arena_Manager.sanction_service.models.Sanction;
import eSports_Arena_Manager.sanction_service.repositories.SanctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SanctionServiceImpl implements SanctionService {

    @Autowired
    private SanctionRepository sanctionRepository;

    @Autowired
    private UserClient userClient;

    @Transactional
    @Override
    public Sanction save(Sanction sanction) {
        if (sanction.getFechaFin().isBefore(sanction.getFechaInicio())) {
            throw new SanctionException("La fecha de término no puede ser anterior a la fecha de inicio.");
        }

        try {
            UserDTO user = userClient.getUserById(sanction.getUsuarioId());
            if (user == null) {
                throw new SanctionException("El usuario con ID " + sanction.getUsuarioId() + " no existe.");
            }
        } catch (Exception e) {
            throw new SanctionException("El servicio de usuarios no está disponible, pero la validación de fechas fue exitosa.");
        }

        sanction.setEstado("ACTIVA");
        return this.sanctionRepository.save(sanction);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Sanction> findAll() {
        return this.sanctionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Sanction findById(Long id) {
        return this.sanctionRepository.findById(id).orElseThrow(
                () -> new SanctionException("Sanción no encontrada.")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Sanction> findByUsuarioId(Long usuarioId) {
        return this.sanctionRepository.findByUsuarioId(usuarioId).orElseThrow(
                () -> new SanctionException("No hay sanciones para este usuario.")
        );
    }

    @Override
    public void deleteById(Long id) {
        this.sanctionRepository.deleteById(id);
    }

    @Override
    public Sanction updateById(Long id, Sanction sanction) {
        return this.sanctionRepository.findById(id).map(s -> {
            if (sanction.getFechaFin().isBefore(sanction.getFechaInicio())) {
                throw new SanctionException("La fecha de término no puede ser anterior a la de inicio.");
            }
            s.setMotivo(sanction.getMotivo());
            s.setEstado(sanction.getEstado());
            s.setFechaInicio(sanction.getFechaInicio());
            s.setFechaFin(sanction.getFechaFin());
            return this.sanctionRepository.save(s);
        }).orElseThrow(() -> new SanctionException("Sanción no encontrada."));
    }
}