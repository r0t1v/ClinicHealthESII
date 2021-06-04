package dev.clinic.simple.service;

import dev.clinic.simple.service.dto.TipoConvenioDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link dev.clinic.simple.domain.TipoConvenio}.
 */
public interface TipoConvenioService {
    /**
     * Save a tipoConvenio.
     *
     * @param tipoConvenioDTO the entity to save.
     * @return the persisted entity.
     */
    TipoConvenioDTO save(TipoConvenioDTO tipoConvenioDTO);

    /**
     * Partially updates a tipoConvenio.
     *
     * @param tipoConvenioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoConvenioDTO> partialUpdate(TipoConvenioDTO tipoConvenioDTO);

    /**
     * Get all the tipoConvenios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TipoConvenioDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tipoConvenio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoConvenioDTO> findOne(Long id);

    /**
     * Delete the "id" tipoConvenio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
