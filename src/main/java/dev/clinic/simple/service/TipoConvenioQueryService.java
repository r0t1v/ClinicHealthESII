package dev.clinic.simple.service;

import dev.clinic.simple.domain.*; // for static metamodels
import dev.clinic.simple.domain.TipoConvenio;
import dev.clinic.simple.repository.TipoConvenioRepository;
import dev.clinic.simple.service.criteria.TipoConvenioCriteria;
import dev.clinic.simple.service.dto.TipoConvenioDTO;
import dev.clinic.simple.service.mapper.TipoConvenioMapper;
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
 * Service for executing complex queries for {@link TipoConvenio} entities in the database.
 * The main input is a {@link TipoConvenioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoConvenioDTO} or a {@link Page} of {@link TipoConvenioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoConvenioQueryService extends QueryService<TipoConvenio> {

    private final Logger log = LoggerFactory.getLogger(TipoConvenioQueryService.class);

    private final TipoConvenioRepository tipoConvenioRepository;

    private final TipoConvenioMapper tipoConvenioMapper;

    public TipoConvenioQueryService(TipoConvenioRepository tipoConvenioRepository, TipoConvenioMapper tipoConvenioMapper) {
        this.tipoConvenioRepository = tipoConvenioRepository;
        this.tipoConvenioMapper = tipoConvenioMapper;
    }

    /**
     * Return a {@link List} of {@link TipoConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoConvenioDTO> findByCriteria(TipoConvenioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TipoConvenio> specification = createSpecification(criteria);
        return tipoConvenioMapper.toDto(tipoConvenioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TipoConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoConvenioDTO> findByCriteria(TipoConvenioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoConvenio> specification = createSpecification(criteria);
        return tipoConvenioRepository.findAll(specification, page).map(tipoConvenioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoConvenioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TipoConvenio> specification = createSpecification(criteria);
        return tipoConvenioRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoConvenioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoConvenio> createSpecification(TipoConvenioCriteria criteria) {
        Specification<TipoConvenio> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TipoConvenio_.id));
            }
            if (criteria.getConvenio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConvenio(), TipoConvenio_.convenio));
            }
            if (criteria.getCodigoconvenio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCodigoconvenio(), TipoConvenio_.codigoconvenio));
            }
            if (criteria.getNomeconvenio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeconvenio(), TipoConvenio_.nomeconvenio));
            }
            if (criteria.getContato() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContato(), TipoConvenio_.contato));
            }
            if (criteria.getContaClinicaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContaClinicaId(),
                            root -> root.join(TipoConvenio_.contaClinica, JoinType.LEFT).get(ContaClinica_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
