package eSports_Arena_Manager.sanction_service.repositories;

import eSports_Arena_Manager.sanction_service.models.Sanction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Long> {
    // Listar por usuario
    List<Sanction> findByUsuarioId(Long usuarioId);

    // Listar por equipo
    List<Sanction> findByEquipoId(Long equipoId);

    // Listar por estado
    List<Sanction> findByEstado(String estado);

    // Verificar si un usuario tiene sanción activa
    boolean existsByUsuarioIdAndEstado(Long usuarioId, String estado);

    // Verificar si un equipo tiene sanción activa
    boolean existsByEquipoIdAndEstado(Long equipoId, String estado);
}

