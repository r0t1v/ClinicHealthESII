package dev.clinic.simple.service;

import dev.clinic.simple.service.dto.ContaClinicaDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link dev.clinic.simple.domain.ContaClinica}.
 */
public interface ContaClinicaService {
    /**
     * Save a contaClinica.
     *
     * @param contaClinicaDTO the entity to save.
     * @return the persisted entity.
     */
    ContaClinicaDTO save(ContaClinicaDTO contaClinicaDTO);

    /**
     * Partially updates a contaClinica.
     *
     * @param contaClinicaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContaClinicaDTO> partialUpdate(ContaClinicaDTO contaClinicaDTO);

    /**
     * Get all the contaClinicas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContaClinicaDTO> findAll(Pageable pageable);
    /**
     * Get all the ContaClinicaDTO where Cpf is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ContaClinicaDTO> findAllWhereCpfIsNull();

    /**
     * Get the "id" contaClinica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContaClinicaDTO> findOne(Long id);

    /**
     * Delete the "id" contaClinica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
