package eSports_Arena_Manager.sanction_service.services;

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

    @Transactional(readOnly = true)
    @Override
    public List<Sanction> findAll() {
        return this.sanctionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Sanction findById(Long id) {
        return this.sanctionRepository.findById(id).orElseThrow(
                () -> new SanctionException("Sanción no encontrada")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Sanction> findByUsuarioId(Long usuarioId) {
        // Importante: Tu Repository debe tener el método: Optional<List<Sanction>> findByUsuarioId(Long usuarioId);
        return this.sanctionRepository.findByUsuarioId(usuarioId).orElseThrow(
                () -> new SanctionException("No se encontraron sanciones para el usuario con ID: " + usuarioId)
        );
    }

    @Transactional
    @Override
    public Sanction save(Sanction sanction) {
        // VALIDACIÓN DE NEGOCIO (REQUERIMIENTO EVALUACIÓN PARCIAL 2)
        if (sanction.getFechaFin().isBefore(sanction.getFechaInicio())) {
            throw new SanctionException("La fecha de término no puede ser anterior a la fecha de inicio");
        }

        sanction.setEstado("ACTIVA");

        return this.sanctionRepository.save(sanction);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!this.sanctionRepository.existsById(id)) {
            throw new SanctionException("No se puede eliminar: Sanción no encontrada");
        }
        this.sanctionRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Sanction updateById(Long id, Sanction sanction) {
        return this.sanctionRepository.findById(id).map(s -> {
            // Validamos fechas también en el update
            if (sanction.getFechaFin().isBefore(sanction.getFechaInicio())) {
                throw new SanctionException("La fecha de término no puede ser anterior a la fecha de inicio");
            }

            s.setMotivo(sanction.getMotivo());
            s.setEstado(sanction.getEstado());
            s.setFechaInicio(sanction.getFechaInicio());
            s.setFechaFin(sanction.getFechaFin());
            s.setUsuarioId(sanction.getUsuarioId());

            return this.sanctionRepository.save(s);
        }).orElseThrow(
                () -> new SanctionException("Sanción no encontrada para actualizar")
        );
    }
}