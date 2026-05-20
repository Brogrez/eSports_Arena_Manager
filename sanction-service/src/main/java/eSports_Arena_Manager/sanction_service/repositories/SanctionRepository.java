package eSports_Arena_Manager.sanction_service.repositories;

import eSports_Arena_Manager.sanction_service.models.Sanction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Long> {
    Optional<Sanction> findByUsuarioId(Long usuarioId);
}
