package dev.clinic.simple.service;

import dev.clinic.simple.service.dto.PacienteDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link dev.clinic.simple.domain.Paciente}.
 */
public interface PacienteService {
    /**
     * Save a paciente.
     *
     * @param pacienteDTO the entity to save.
     * @return the persisted entity.
     */
    PacienteDTO save(PacienteDTO pacienteDTO);

    /**
     * Partially updates a paciente.
     *
     * @param pacienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PacienteDTO> partialUpdate(PacienteDTO pacienteDTO);

    /**
     * Get all the pacientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PacienteDTO> findAll(Pageable pageable);
    /**
     * Get all the PacienteDTO where Cpf is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PacienteDTO> findAllWhereCpfIsNull();

    /**
     * Get the "id" paciente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PacienteDTO> findOne(Long id);

    /**
     * Delete the "id" paciente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
