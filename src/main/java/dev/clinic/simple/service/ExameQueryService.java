package dev.clinic.simple.service;

import dev.clinic.simple.domain.*; // for static metamodels
import dev.clinic.simple.domain.Exame;
import dev.clinic.simple.repository.ExameRepository;
import dev.clinic.simple.service.criteria.ExameCriteria;
import dev.clinic.simple.service.dto.ExameDTO;
import dev.clinic.simple.service.mapper.ExameMapper;
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
 * Service for executing complex queries for {@link Exame} entities in the database.
 * The main input is a {@link ExameCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExameDTO} or a {@link Page} of {@link ExameDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExameQueryService extends QueryService<Exame> {

    private final Logger log = LoggerFactory.getLogger(ExameQueryService.class);

    private final ExameRepository exameRepository;

    private final ExameMapper exameMapper;

    public ExameQueryService(ExameRepository exameRepository, ExameMapper exameMapper) {
        this.exameRepository = exameRepository;
        this.exameMapper = exameMapper;
    }

    /**
     * Return a {@link List} of {@link ExameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExameDTO> findByCriteria(ExameCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Exame> specification = createSpecification(criteria);
        return exameMapper.toDto(exameRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExameDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExameDTO> findByCriteria(ExameCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Exame> specification = createSpecification(criteria);
        return exameRepository.findAll(specification, page).map(exameMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExameCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Exame> specification = createSpecification(criteria);
        return exameRepository.count(specification);
    }

    /**
     * Function to convert {@link ExameCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Exame> createSpecification(ExameCriteria criteria) {
        Specification<Exame> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Exame_.id));
            }
            if (criteria.getTipoexame() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipoexame(), Exame_.tipoexame));
            }
            if (criteria.getValorexame() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValorexame(), Exame_.valorexame));
            }
            if (criteria.getDescontoconvenio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescontoconvenio(), Exame_.descontoconvenio));
            }
            if (criteria.getNomemmedico() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomemmedico(), Exame_.nomemmedico));
            }
            if (criteria.getDatasolicitacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatasolicitacao(), Exame_.datasolicitacao));
            }
            if (criteria.getDataresultado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataresultado(), Exame_.dataresultado));
            }
            if (criteria.getNomemedicoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNomemedicoId(), root -> root.join(Exame_.nomemedico, JoinType.LEFT).get(Medico_.id))
                    );
            }
            if (criteria.getTipoexameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTipoexameId(),
                            root -> root.join(Exame_.tipoexame, JoinType.LEFT).get(ResultadoExame_.id)
                        )
                    );
            }
            if (criteria.getValorexameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getValorexameId(),
                            root -> root.join(Exame_.valorexames, JoinType.LEFT).get(PagamentoExame_.id)
                        )
                    );
            }
            if (criteria.getContaClinicaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContaClinicaId(),
                            root -> root.join(Exame_.contaClinica, JoinType.LEFT).get(ContaClinica_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
