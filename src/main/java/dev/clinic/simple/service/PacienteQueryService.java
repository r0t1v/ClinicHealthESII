package dev.clinic.simple.service;

import dev.clinic.simple.domain.*; // for static metamodels
import dev.clinic.simple.domain.Paciente;
import dev.clinic.simple.repository.PacienteRepository;
import dev.clinic.simple.service.criteria.PacienteCriteria;
import dev.clinic.simple.service.dto.PacienteDTO;
import dev.clinic.simple.service.mapper.PacienteMapper;
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
 * Service for executing complex queries for {@link Paciente} entities in the database.
 * The main input is a {@link PacienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PacienteDTO} or a {@link Page} of {@link PacienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PacienteQueryService extends QueryService<Paciente> {

    private final Logger log = LoggerFactory.getLogger(PacienteQueryService.class);

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    public PacienteQueryService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    /**
     * Return a {@link List} of {@link PacienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> findByCriteria(PacienteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteMapper.toDto(pacienteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PacienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PacienteDTO> findByCriteria(PacienteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteRepository.findAll(specification, page).map(pacienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PacienteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteRepository.count(specification);
    }

    /**
     * Function to convert {@link PacienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Paciente> createSpecification(PacienteCriteria criteria) {
        Specification<Paciente> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Paciente_.id));
            }
            if (criteria.getCpf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpf(), Paciente_.cpf));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Paciente_.nome));
            }
            if (criteria.getIdade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdade(), Paciente_.idade));
            }
            if (criteria.getCpfId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCpfId(), root -> root.join(Paciente_.cpf, JoinType.LEFT).get(ContaClinica_.id))
                    );
            }
            if (criteria.getCpfId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCpfId(), root -> root.join(Paciente_.cpfs, JoinType.LEFT).get(EnderecoPaciente_.id))
                    );
            }
            if (criteria.getCpfId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCpfId(), root -> root.join(Paciente_.cpfs, JoinType.LEFT).get(ContatoPaciente_.id))
                    );
            }
            if (criteria.getCpfId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCpfId(), root -> root.join(Paciente_.cpf, JoinType.LEFT).get(ContaClinica_.id))
                    );
            }
        }
        return specification;
    }
}
