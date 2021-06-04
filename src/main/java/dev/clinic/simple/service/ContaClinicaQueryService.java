package dev.clinic.simple.service;

import dev.clinic.simple.domain.*; // for static metamodels
import dev.clinic.simple.domain.ContaClinica;
import dev.clinic.simple.repository.ContaClinicaRepository;
import dev.clinic.simple.service.criteria.ContaClinicaCriteria;
import dev.clinic.simple.service.dto.ContaClinicaDTO;
import dev.clinic.simple.service.mapper.ContaClinicaMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ContaClinica} entities in the database.
 * The main input is a {@link ContaClinicaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContaClinicaDTO} or a {@link Page} of {@link ContaClinicaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContaClinicaQueryService extends QueryService<ContaClinica> {

    private final Logger log = LoggerFactory.getLogger(ContaClinicaQueryService.class);

    private final ContaClinicaRepository contaClinicaRepository;

    private final ContaClinicaMapper contaClinicaMapper;

    public ContaClinicaQueryService(ContaClinicaRepository contaClinicaRepository, ContaClinicaMapper contaClinicaMapper) {
        this.contaClinicaRepository = contaClinicaRepository;
        this.contaClinicaMapper = contaClinicaMapper;
    }

    /**
     * Return a {@link List} of {@link ContaClinicaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContaClinicaDTO> findByCriteria(ContaClinicaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContaClinica> specification = createSpecification(criteria);
        return contaClinicaMapper.toDto(contaClinicaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContaClinicaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContaClinicaDTO> findByCriteria(ContaClinicaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContaClinica> specification = createSpecification(criteria);
        return contaClinicaRepository.findAll(specification, page).map(contaClinicaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContaClinicaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContaClinica> specification = createSpecification(criteria);
        return contaClinicaRepository.count(specification);
    }

    /**
     * Function to convert {@link ContaClinicaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContaClinica> createSpecification(ContaClinicaCriteria criteria) {
        Specification<ContaClinica> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContaClinica_.id));
            }
            if (criteria.getCpf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpf(), ContaClinica_.cpf));
            }
            if (criteria.getSenha() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSenha(), ContaClinica_.senha));
            }
            if (criteria.getCpfId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCpfId(), root -> root.join(ContaClinica_.cpf, JoinType.LEFT).get(Paciente_.id))
                    );
            }
            if (criteria.getCpfId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCpfId(), root -> root.join(ContaClinica_.cpfs, JoinType.LEFT).get(TipoConvenio_.id))
                    );
            }
            if (criteria.getCpfId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCpfId(), root -> root.join(ContaClinica_.cpfs, JoinType.LEFT).get(Exame_.id))
                    );
            }
            if (criteria.getCpfId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCpfId(), root -> root.join(ContaClinica_.cpf, JoinType.LEFT).get(Paciente_.id))
                    );
            }
        }
        return specification;
    }
}
