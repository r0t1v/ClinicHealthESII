package dev.clinic.simple.service;

import dev.clinic.simple.domain.*; // for static metamodels
import dev.clinic.simple.domain.ResultadoExame;
import dev.clinic.simple.repository.ResultadoExameRepository;
import dev.clinic.simple.service.criteria.ResultadoExameCriteria;
import dev.clinic.simple.service.dto.ResultadoExameDTO;
import dev.clinic.simple.service.mapper.ResultadoExameMapper;
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
 * Service for executing complex queries for {@link ResultadoExame} entities in the database.
 * The main input is a {@link ResultadoExameCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ResultadoExameDTO} or a {@link Page} of {@link ResultadoExameDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ResultadoExameQueryService extends QueryService<ResultadoExame> {

    private final Logger log = LoggerFactory.getLogger(ResultadoExameQueryService.class);

    private final ResultadoExameRepository resultadoExameRepository;

    private final ResultadoExameMapper resultadoExameMapper;

    public ResultadoExameQueryService(ResultadoExameRepository resultadoExameRepository, ResultadoExameMapper resultadoExameMapper) {
        this.resultadoExameRepository = resultadoExameRepository;
        this.resultadoExameMapper = resultadoExameMapper;
    }

    /**
     * Return a {@link List} of {@link ResultadoExameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ResultadoExameDTO> findByCriteria(ResultadoExameCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ResultadoExame> specification = createSpecification(criteria);
        return resultadoExameMapper.toDto(resultadoExameRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ResultadoExameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ResultadoExameDTO> findByCriteria(ResultadoExameCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ResultadoExame> specification = createSpecification(criteria);
        return resultadoExameRepository.findAll(specification, page).map(resultadoExameMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ResultadoExameCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ResultadoExame> specification = createSpecification(criteria);
        return resultadoExameRepository.count(specification);
    }

    /**
     * Function to convert {@link ResultadoExameCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ResultadoExame> createSpecification(ResultadoExameCriteria criteria) {
        Specification<ResultadoExame> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ResultadoExame_.id));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), ResultadoExame_.descricao));
            }
            if (criteria.getPrescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrescricao(), ResultadoExame_.prescricao));
            }
            if (criteria.getIndicacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndicacao(), ResultadoExame_.indicacao));
            }
            if (criteria.getContraindicacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContraindicacao(), ResultadoExame_.contraindicacao));
            }
        }
        return specification;
    }
}
