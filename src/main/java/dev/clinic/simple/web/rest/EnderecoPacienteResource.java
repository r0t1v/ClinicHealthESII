package dev.clinic.simple.web.rest;

import dev.clinic.simple.repository.EnderecoPacienteRepository;
import dev.clinic.simple.service.EnderecoPacienteQueryService;
import dev.clinic.simple.service.EnderecoPacienteService;
import dev.clinic.simple.service.criteria.EnderecoPacienteCriteria;
import dev.clinic.simple.service.dto.EnderecoPacienteDTO;
import dev.clinic.simple.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link dev.clinic.simple.domain.EnderecoPaciente}.
 */
@RestController
@RequestMapping("/api")
public class EnderecoPacienteResource {

    private final Logger log = LoggerFactory.getLogger(EnderecoPacienteResource.class);

    private static final String ENTITY_NAME = "enderecoPaciente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnderecoPacienteService enderecoPacienteService;

    private final EnderecoPacienteRepository enderecoPacienteRepository;

    private final EnderecoPacienteQueryService enderecoPacienteQueryService;

    public EnderecoPacienteResource(
        EnderecoPacienteService enderecoPacienteService,
        EnderecoPacienteRepository enderecoPacienteRepository,
        EnderecoPacienteQueryService enderecoPacienteQueryService
    ) {
        this.enderecoPacienteService = enderecoPacienteService;
        this.enderecoPacienteRepository = enderecoPacienteRepository;
        this.enderecoPacienteQueryService = enderecoPacienteQueryService;
    }

    /**
     * {@code POST  /endereco-pacientes} : Create a new enderecoPaciente.
     *
     * @param enderecoPacienteDTO the enderecoPacienteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enderecoPacienteDTO, or with status {@code 400 (Bad Request)} if the enderecoPaciente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/endereco-pacientes")
    public ResponseEntity<EnderecoPacienteDTO> createEnderecoPaciente(@RequestBody EnderecoPacienteDTO enderecoPacienteDTO)
        throws URISyntaxException {
        log.debug("REST request to save EnderecoPaciente : {}", enderecoPacienteDTO);
        if (enderecoPacienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new enderecoPaciente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnderecoPacienteDTO result = enderecoPacienteService.save(enderecoPacienteDTO);
        return ResponseEntity
            .created(new URI("/api/endereco-pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /endereco-pacientes/:id} : Updates an existing enderecoPaciente.
     *
     * @param id the id of the enderecoPacienteDTO to save.
     * @param enderecoPacienteDTO the enderecoPacienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enderecoPacienteDTO,
     * or with status {@code 400 (Bad Request)} if the enderecoPacienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enderecoPacienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/endereco-pacientes/{id}")
    public ResponseEntity<EnderecoPacienteDTO> updateEnderecoPaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EnderecoPacienteDTO enderecoPacienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EnderecoPaciente : {}, {}", id, enderecoPacienteDTO);
        if (enderecoPacienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enderecoPacienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enderecoPacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EnderecoPacienteDTO result = enderecoPacienteService.save(enderecoPacienteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enderecoPacienteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /endereco-pacientes/:id} : Partial updates given fields of an existing enderecoPaciente, field will ignore if it is null
     *
     * @param id the id of the enderecoPacienteDTO to save.
     * @param enderecoPacienteDTO the enderecoPacienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enderecoPacienteDTO,
     * or with status {@code 400 (Bad Request)} if the enderecoPacienteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enderecoPacienteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enderecoPacienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/endereco-pacientes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EnderecoPacienteDTO> partialUpdateEnderecoPaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EnderecoPacienteDTO enderecoPacienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EnderecoPaciente partially : {}, {}", id, enderecoPacienteDTO);
        if (enderecoPacienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enderecoPacienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enderecoPacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EnderecoPacienteDTO> result = enderecoPacienteService.partialUpdate(enderecoPacienteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enderecoPacienteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /endereco-pacientes} : get all the enderecoPacientes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enderecoPacientes in body.
     */
    @GetMapping("/endereco-pacientes")
    public ResponseEntity<List<EnderecoPacienteDTO>> getAllEnderecoPacientes(EnderecoPacienteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EnderecoPacientes by criteria: {}", criteria);
        Page<EnderecoPacienteDTO> page = enderecoPacienteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /endereco-pacientes/count} : count all the enderecoPacientes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/endereco-pacientes/count")
    public ResponseEntity<Long> countEnderecoPacientes(EnderecoPacienteCriteria criteria) {
        log.debug("REST request to count EnderecoPacientes by criteria: {}", criteria);
        return ResponseEntity.ok().body(enderecoPacienteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /endereco-pacientes/:id} : get the "id" enderecoPaciente.
     *
     * @param id the id of the enderecoPacienteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enderecoPacienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/endereco-pacientes/{id}")
    public ResponseEntity<EnderecoPacienteDTO> getEnderecoPaciente(@PathVariable Long id) {
        log.debug("REST request to get EnderecoPaciente : {}", id);
        Optional<EnderecoPacienteDTO> enderecoPacienteDTO = enderecoPacienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enderecoPacienteDTO);
    }

    /**
     * {@code DELETE  /endereco-pacientes/:id} : delete the "id" enderecoPaciente.
     *
     * @param id the id of the enderecoPacienteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/endereco-pacientes/{id}")
    public ResponseEntity<Void> deleteEnderecoPaciente(@PathVariable Long id) {
        log.debug("REST request to delete EnderecoPaciente : {}", id);
        enderecoPacienteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
