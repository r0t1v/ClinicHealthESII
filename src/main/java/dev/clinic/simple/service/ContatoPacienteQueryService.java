package dev.clinic.simple.service;

import dev.clinic.simple.domain.*; // for static metamodels
import dev.clinic.simple.domain.ContatoPaciente;
import dev.clinic.simple.repository.ContatoPacienteRepository;
import dev.clinic.simple.service.criteria.ContatoPacienteCriteria;
import dev.clinic.simple.service.dto.ContatoPacienteDTO;
import dev.clinic.simple.service.mapper.ContatoPacienteMapper;
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
 * Service for executing complex queries for {@link ContatoPaciente} entities in the database.
 * The main input is a {@link ContatoPacienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContatoPacienteDTO} or a {@link Page} of {@link ContatoPacienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContatoPacienteQueryService extends QueryService<ContatoPaciente> {

    private final Logger log = LoggerFactory.getLogger(ContatoPacienteQueryService.class);

    private final ContatoPacienteRepository contatoPacienteRepository;

    private final ContatoPacienteMapper contatoPacienteMapper;

    public ContatoPacienteQueryService(ContatoPacienteRepository contatoPacienteRepository, ContatoPacienteMapper contatoPacienteMapper) {
        this.contatoPacienteRepository = contatoPacienteRepository;
        this.contatoPacienteMapper = contatoPacienteMapper;
    }

    /**
     * Return a {@link List} of {@link ContatoPacienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContatoPacienteDTO> findByCriteria(ContatoPacienteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContatoPaciente> specification = createSpecification(criteria);
        return contatoPacienteMapper.toDto(contatoPacienteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContatoPacienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContatoPacienteDTO> findByCriteria(ContatoPacienteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContatoPaciente> specification = createSpecification(criteria);
        return contatoPacienteRepository.findAll(specification, page).map(contatoPacienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContatoPacienteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContatoPaciente> specification = createSpecification(criteria);
        return contatoPacienteRepository.count(specification);
    }

    /**
     * Function to convert {@link ContatoPacienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContatoPaciente> createSpecification(ContatoPacienteCriteria criteria) {
        Specification<ContatoPaciente> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContatoPaciente_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), ContatoPaciente_.tipo));
            }
            if (criteria.getConteudo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConteudo(), ContatoPaciente_.conteudo));
            }
            if (criteria.getPacienteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPacienteId(),
                            root -> root.join(ContatoPaciente_.paciente, JoinType.LEFT).get(Paciente_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
