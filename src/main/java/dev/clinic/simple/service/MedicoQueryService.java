package dev.clinic.simple.service;

import dev.clinic.simple.domain.*; // for static metamodels
import dev.clinic.simple.domain.Medico;
import dev.clinic.simple.repository.MedicoRepository;
import dev.clinic.simple.service.criteria.MedicoCriteria;
import dev.clinic.simple.service.dto.MedicoDTO;
import dev.clinic.simple.service.mapper.MedicoMapper;
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
 * Service for executing complex queries for {@link Medico} entities in the database.
 * The main input is a {@link MedicoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MedicoDTO} or a {@link Page} of {@link MedicoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicoQueryService extends QueryService<Medico> {

    private final Logger log = LoggerFactory.getLogger(MedicoQueryService.class);

    private final MedicoRepository medicoRepository;

    private final MedicoMapper medicoMapper;

    public MedicoQueryService(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    /**
     * Return a {@link List} of {@link MedicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MedicoDTO> findByCriteria(MedicoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Medico> specification = createSpecification(criteria);
        return medicoMapper.toDto(medicoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MedicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicoDTO> findByCriteria(MedicoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medico> specification = createSpecification(criteria);
        return medicoRepository.findAll(specification, page).map(medicoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Medico> specification = createSpecification(criteria);
        return medicoRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medico> createSpecification(MedicoCriteria criteria) {
        Specification<Medico> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Medico_.id));
            }
            if (criteria.getCrm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCrm(), Medico_.crm));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNome(), Medico_.nome));
            }
            if (criteria.getGraduacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGraduacao(), Medico_.graduacao));
            }
            if (criteria.getAtuacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAtuacao(), Medico_.atuacao));
            }
            if (criteria.getNomeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNomeId(), root -> root.join(Medico_.nome, JoinType.LEFT).get(Exame_.id))
                    );
            }
        }
        return specification;
    }
}
