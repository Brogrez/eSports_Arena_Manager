package eSports_Arena_Manager.result_service.repositories;

import eSports_Arena_Manager.result_service.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

   Optional<Result> findByPartidaId(Long partidaId);
   List<Result> findByEstado(String estado);
   boolean existsByPartidaId(Long partidaId);
}