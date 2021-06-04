package dev.clinic.simple.repository;

import dev.clinic.simple.domain.ResultadoExame;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ResultadoExame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultadoExameRepository extends JpaRepository<ResultadoExame, Long>, JpaSpecificationExecutor<ResultadoExame> {}
