package dev.clinic.simple.repository;

import dev.clinic.simple.domain.ContaClinica;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContaClinica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContaClinicaRepository extends JpaRepository<ContaClinica, Long>, JpaSpecificationExecutor<ContaClinica> {}
