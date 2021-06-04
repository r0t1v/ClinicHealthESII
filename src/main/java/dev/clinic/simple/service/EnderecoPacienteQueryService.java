package dev.clinic.simple.service;

import dev.clinic.simple.domain.*; // for static metamodels
import dev.clinic.simple.domain.EnderecoPaciente;
import dev.clinic.simple.repository.EnderecoPacienteRepository;
import dev.clinic.simple.service.criteria.EnderecoPacienteCriteria;
import dev.clinic.simple.service.dto.EnderecoPacienteDTO;
import dev.clinic.simple.service.mapper.EnderecoPacienteMapper;
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
 * Service for executing complex queries for {@link EnderecoPaciente} entities in the database.
 * The main input is a {@link EnderecoPacienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EnderecoPacienteDTO} or a {@link Page} of {@link EnderecoPacienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EnderecoPacienteQueryService extends QueryService<EnderecoPaciente> {

    private final Logger log = LoggerFactory.getLogger(EnderecoPacienteQueryService.class);

    private final EnderecoPacienteRepository enderecoPacienteRepository;

    private final EnderecoPacienteMapper enderecoPacienteMapper;

    public EnderecoPacienteQueryService(
        EnderecoPacienteRepository enderecoPacienteRepository,
        EnderecoPacienteMapper enderecoPacienteMapper
    ) {
        this.enderecoPacienteRepository = enderecoPacienteRepository;
        this.enderecoPacienteMapper = enderecoPacienteMapper;
    }

    /**
     * Return a {@link List} of {@link EnderecoPacienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EnderecoPacienteDTO> findByCriteria(EnderecoPacienteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EnderecoPaciente> specification = createSpecification(criteria);
        return enderecoPacienteMapper.toDto(enderecoPacienteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EnderecoPacienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EnderecoPacienteDTO> findByCriteria(EnderecoPacienteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EnderecoPaciente> specification = createSpecification(criteria);
        return enderecoPacienteRepository.findAll(specification, page).map(enderecoPacienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EnderecoPacienteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EnderecoPaciente> specification = createSpecification(criteria);
        return enderecoPacienteRepository.count(specification);
    }

    /**
     * Function to convert {@link EnderecoPacienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EnderecoPaciente> createSpecification(EnderecoPacienteCriteria criteria) {
        Specification<EnderecoPaciente> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EnderecoPaciente_.id));
            }
            if (criteria.getCidade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCidade(), EnderecoPaciente_.cidade));
            }
            if (criteria.getLogradouro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogradouro(), EnderecoPaciente_.logradouro));
            }
            if (criteria.getBairro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBairro(), EnderecoPaciente_.bairro));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumero(), EnderecoPaciente_.numero));
            }
            if (criteria.getReferencia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReferencia(), EnderecoPaciente_.referencia));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), EnderecoPaciente_.cep));
            }
            if (criteria.getPacienteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPacienteId(),
                            root -> root.join(EnderecoPaciente_.paciente, JoinType.LEFT).get(Paciente_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
