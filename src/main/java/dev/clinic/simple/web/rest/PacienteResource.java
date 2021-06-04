package dev.clinic.simple.web.rest;

import dev.clinic.simple.repository.PacienteRepository;
import dev.clinic.simple.service.PacienteQueryService;
import dev.clinic.simple.service.PacienteService;
import dev.clinic.simple.service.criteria.PacienteCriteria;
import dev.clinic.simple.service.dto.PacienteDTO;
import dev.clinic.simple.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dev.clinic.simple.domain.Paciente}.
 */
@RestController
@RequestMapping("/api")
public class PacienteResource {

    private final Logger log = LoggerFactory.getLogger(PacienteResource.class);

    private static final String ENTITY_NAME = "paciente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacienteService pacienteService;

    private final PacienteRepository pacienteRepository;

    private final PacienteQueryService pacienteQueryService;

    public PacienteResource(
        PacienteService pacienteService,
        PacienteRepository pacienteRepository,
        PacienteQueryService pacienteQueryService
    ) {
        this.pacienteService = pacienteService;
        this.pacienteRepository = pacienteRepository;
        this.pacienteQueryService = pacienteQueryService;
    }

    /**
     * {@code POST  /pacientes} : Create a new paciente.
     *
     * @param pacienteDTO the pacienteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pacienteDTO, or with status {@code 400 (Bad Request)} if the paciente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pacientes")
    public ResponseEntity<PacienteDTO> createPaciente(@RequestBody PacienteDTO pacienteDTO) throws URISyntaxException {
        log.debug("REST request to save Paciente : {}", pacienteDTO);
        if (pacienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new paciente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PacienteDTO result = pacienteService.save(pacienteDTO);
        return ResponseEntity
            .created(new URI("/api/pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pacientes/:id} : Updates an existing paciente.
     *
     * @param id the id of the pacienteDTO to save.
     * @param pacienteDTO the pacienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacienteDTO,
     * or with status {@code 400 (Bad Request)} if the pacienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pacienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pacientes/{id}")
    public ResponseEntity<PacienteDTO> updatePaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PacienteDTO pacienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Paciente : {}, {}", id, pacienteDTO);
        if (pacienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PacienteDTO result = pacienteService.save(pacienteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pacienteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pacientes/:id} : Partial updates given fields of an existing paciente, field will ignore if it is null
     *
     * @param id the id of the pacienteDTO to save.
     * @param pacienteDTO the pacienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacienteDTO,
     * or with status {@code 400 (Bad Request)} if the pacienteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pacienteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pacienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pacientes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PacienteDTO> partialUpdatePaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PacienteDTO pacienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Paciente partially : {}, {}", id, pacienteDTO);
        if (pacienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PacienteDTO> result = pacienteService.partialUpdate(pacienteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pacienteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pacientes} : get all the pacientes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pacientes in body.
     */
    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDTO>> getAllPacientes(PacienteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pacientes by criteria: {}", criteria);
        Page<PacienteDTO> page = pacienteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pacientes/count} : count all the pacientes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pacientes/count")
    public ResponseEntity<Long> countPacientes(PacienteCriteria criteria) {
        log.debug("REST request to count Pacientes by criteria: {}", criteria);
        return ResponseEntity.ok().body(pacienteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pacientes/:id} : get the "id" paciente.
     *
     * @param id the id of the pacienteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pacienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pacientes/{id}")
    public ResponseEntity<PacienteDTO> getPaciente(@PathVariable Long id) {
        log.debug("REST request to get Paciente : {}", id);
        Optional<PacienteDTO> pacienteDTO = pacienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pacienteDTO);
    }

    /**
     * {@code DELETE  /pacientes/:id} : delete the "id" paciente.
     *
     * @param id the id of the pacienteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pacientes/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        log.debug("REST request to delete Paciente : {}", id);
        pacienteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
