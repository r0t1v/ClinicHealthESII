package dev.clinic.simple.repository;

import dev.clinic.simple.domain.Exame;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Exame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExameRepository extends JpaRepository<Exame, Long>, JpaSpecificationExecutor<Exame> {}
