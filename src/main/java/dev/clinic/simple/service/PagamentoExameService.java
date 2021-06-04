package dev.clinic.simple.service;

import dev.clinic.simple.service.dto.PagamentoExameDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link dev.clinic.simple.domain.PagamentoExame}.
 */
public interface PagamentoExameService {
    /**
     * Save a pagamentoExame.
     *
     * @param pagamentoExameDTO the entity to save.
     * @return the persisted entity.
     */
    PagamentoExameDTO save(PagamentoExameDTO pagamentoExameDTO);

    /**
     * Partially updates a pagamentoExame.
     *
     * @param pagamentoExameDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PagamentoExameDTO> partialUpdate(PagamentoExameDTO pagamentoExameDTO);

    /**
     * Get all the pagamentoExames.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PagamentoExameDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pagamentoExame.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PagamentoExameDTO> findOne(Long id);

    /**
     * Delete the "id" pagamentoExame.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
