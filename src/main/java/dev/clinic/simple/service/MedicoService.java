package dev.clinic.simple.service;

import dev.clinic.simple.service.dto.MedicoDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link dev.clinic.simple.domain.Medico}.
 */
public interface MedicoService {
    /**
     * Save a medico.
     *
     * @param medicoDTO the entity to save.
     * @return the persisted entity.
     */
    MedicoDTO save(MedicoDTO medicoDTO);

    /**
     * Partially updates a medico.
     *
     * @param medicoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicoDTO> partialUpdate(MedicoDTO medicoDTO);

    /**
     * Get all the medicos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicoDTO> findAll(Pageable pageable);
    /**
     * Get all the MedicoDTO where Nome is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<MedicoDTO> findAllWhereNomeIsNull();

    /**
     * Get the "id" medico.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicoDTO> findOne(Long id);

    /**
     * Delete the "id" medico.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
