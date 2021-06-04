package dev.clinic.simple.repository;

import dev.clinic.simple.domain.ContatoPaciente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContatoPaciente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContatoPacienteRepository extends JpaRepository<ContatoPaciente, Long>, JpaSpecificationExecutor<ContatoPaciente> {}
