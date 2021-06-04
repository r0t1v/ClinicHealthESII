package dev.clinic.simple.service;

import dev.clinic.simple.service.dto.ExameDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link dev.clinic.simple.domain.Exame}.
 */
public interface ExameService {
    /**
     * Save a exame.
     *
     * @param exameDTO the entity to save.
     * @return the persisted entity.
     */
    ExameDTO save(ExameDTO exameDTO);

    /**
     * Partially updates a exame.
     *
     * @param exameDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExameDTO> partialUpdate(ExameDTO exameDTO);

    /**
     * Get all the exames.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExameDTO> findAll(Pageable pageable);

    /**
     * Get the "id" exame.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExameDTO> findOne(Long id);

    /**
     * Delete the "id" exame.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
