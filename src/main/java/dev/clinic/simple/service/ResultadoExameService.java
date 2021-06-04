package dev.clinic.simple.service;

import dev.clinic.simple.service.dto.ResultadoExameDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link dev.clinic.simple.domain.ResultadoExame}.
 */
public interface ResultadoExameService {
    /**
     * Save a resultadoExame.
     *
     * @param resultadoExameDTO the entity to save.
     * @return the persisted entity.
     */
    ResultadoExameDTO save(ResultadoExameDTO resultadoExameDTO);

    /**
     * Partially updates a resultadoExame.
     *
     * @param resultadoExameDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ResultadoExameDTO> partialUpdate(ResultadoExameDTO resultadoExameDTO);

    /**
     * Get all the resultadoExames.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ResultadoExameDTO> findAll(Pageable pageable);

    /**
     * Get the "id" resultadoExame.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ResultadoExameDTO> findOne(Long id);

    /**
     * Delete the "id" resultadoExame.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
