package dev.clinic.simple.service;

import dev.clinic.simple.service.dto.EnderecoPacienteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link dev.clinic.simple.domain.EnderecoPaciente}.
 */
public interface EnderecoPacienteService {
    /**
     * Save a enderecoPaciente.
     *
     * @param enderecoPacienteDTO the entity to save.
     * @return the persisted entity.
     */
    EnderecoPacienteDTO save(EnderecoPacienteDTO enderecoPacienteDTO);

    /**
     * Partially updates a enderecoPaciente.
     *
     * @param enderecoPacienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnderecoPacienteDTO> partialUpdate(EnderecoPacienteDTO enderecoPacienteDTO);

    /**
     * Get all the enderecoPacientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnderecoPacienteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" enderecoPaciente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnderecoPacienteDTO> findOne(Long id);

    /**
     * Delete the "id" enderecoPaciente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
