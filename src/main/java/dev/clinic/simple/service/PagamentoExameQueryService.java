package dev.clinic.simple.service;

import dev.clinic.simple.domain.*; // for static metamodels
import dev.clinic.simple.domain.PagamentoExame;
import dev.clinic.simple.repository.PagamentoExameRepository;
import dev.clinic.simple.service.criteria.PagamentoExameCriteria;
import dev.clinic.simple.service.dto.PagamentoExameDTO;
import dev.clinic.simple.service.mapper.PagamentoExameMapper;
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
 * Service for executing complex queries for {@link PagamentoExame} entities in the database.
 * The main input is a {@link PagamentoExameCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PagamentoExameDTO} or a {@link Page} of {@link PagamentoExameDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PagamentoExameQueryService extends QueryService<PagamentoExame> {

    private final Logger log = LoggerFactory.getLogger(PagamentoExameQueryService.class);

    private final PagamentoExameRepository pagamentoExameRepository;

    private final PagamentoExameMapper pagamentoExameMapper;

    public PagamentoExameQueryService(PagamentoExameRepository pagamentoExameRepository, PagamentoExameMapper pagamentoExameMapper) {
        this.pagamentoExameRepository = pagamentoExameRepository;
        this.pagamentoExameMapper = pagamentoExameMapper;
    }

    /**
     * Return a {@link List} of {@link PagamentoExameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PagamentoExameDTO> findByCriteria(PagamentoExameCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PagamentoExame> specification = createSpecification(criteria);
        return pagamentoExameMapper.toDto(pagamentoExameRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PagamentoExameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PagamentoExameDTO> findByCriteria(PagamentoExameCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PagamentoExame> specification = createSpecification(criteria);
        return pagamentoExameRepository.findAll(specification, page).map(pagamentoExameMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PagamentoExameCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PagamentoExame> specification = createSpecification(criteria);
        return pagamentoExameRepository.count(specification);
    }

    /**
     * Function to convert {@link PagamentoExameCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PagamentoExame> createSpecification(PagamentoExameCriteria criteria) {
        Specification<PagamentoExame> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PagamentoExame_.id));
            }
            if (criteria.getFormapagamento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFormapagamento(), PagamentoExame_.formapagamento));
            }
            if (criteria.getConteudo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConteudo(), PagamentoExame_.conteudo));
            }
            if (criteria.getSeliquidado() != null) {
                specification = specification.and(buildSpecification(criteria.getSeliquidado(), PagamentoExame_.seliquidado));
            }
            if (criteria.getExameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getExameId(), root -> root.join(PagamentoExame_.exame, JoinType.LEFT).get(Exame_.id))
                    );
            }
        }
        return specification;
    }
}
