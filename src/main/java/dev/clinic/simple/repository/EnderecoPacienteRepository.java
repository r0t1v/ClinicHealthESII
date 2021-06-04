package dev.clinic.simple.repository;

import dev.clinic.simple.domain.EnderecoPaciente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EnderecoPaciente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnderecoPacienteRepository extends JpaRepository<EnderecoPaciente, Long>, JpaSpecificationExecutor<EnderecoPaciente> {}
