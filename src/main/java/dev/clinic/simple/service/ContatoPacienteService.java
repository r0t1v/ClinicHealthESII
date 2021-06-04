package dev.clinic.simple.service;

import dev.clinic.simple.service.dto.ContatoPacienteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link dev.clinic.simple.domain.ContatoPaciente}.
 */
public interface ContatoPacienteService {
    /**
     * Save a contatoPaciente.
     *
     * @param contatoPacienteDTO the entity to save.
     * @return the persisted entity.
     */
    ContatoPacienteDTO save(ContatoPacienteDTO contatoPacienteDTO);

    /**
     * Partially updates a contatoPaciente.
     *
     * @param contatoPacienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContatoPacienteDTO> partialUpdate(ContatoPacienteDTO contatoPacienteDTO);

    /**
     * Get all the contatoPacientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContatoPacienteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contatoPaciente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContatoPacienteDTO> findOne(Long id);

    /**
     * Delete the "id" contatoPaciente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
