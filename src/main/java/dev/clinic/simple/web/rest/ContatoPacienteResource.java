package dev.clinic.simple.web.rest;

import dev.clinic.simple.repository.ContatoPacienteRepository;
import dev.clinic.simple.service.ContatoPacienteQueryService;
import dev.clinic.simple.service.ContatoPacienteService;
import dev.clinic.simple.service.criteria.ContatoPacienteCriteria;
import dev.clinic.simple.service.dto.ContatoPacienteDTO;
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
 * REST controller for managing {@link dev.clinic.simple.domain.ContatoPaciente}.
 */
@RestController
@RequestMapping("/api")
public class ContatoPacienteResource {

    private final Logger log = LoggerFactory.getLogger(ContatoPacienteResource.class);

    private static final String ENTITY_NAME = "contatoPaciente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContatoPacienteService contatoPacienteService;

    private final ContatoPacienteRepository contatoPacienteRepository;

    private final ContatoPacienteQueryService contatoPacienteQueryService;

    public ContatoPacienteResource(
        ContatoPacienteService contatoPacienteService,
        ContatoPacienteRepository contatoPacienteRepository,
        ContatoPacienteQueryService contatoPacienteQueryService
    ) {
        this.contatoPacienteService = contatoPacienteService;
        this.contatoPacienteRepository = contatoPacienteRepository;
        this.contatoPacienteQueryService = contatoPacienteQueryService;
    }

    /**
     * {@code POST  /contato-pacientes} : Create a new contatoPaciente.
     *
     * @param contatoPacienteDTO the contatoPacienteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contatoPacienteDTO, or with status {@code 400 (Bad Request)} if the contatoPaciente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contato-pacientes")
    public ResponseEntity<ContatoPacienteDTO> createContatoPaciente(@RequestBody ContatoPacienteDTO contatoPacienteDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContatoPaciente : {}", contatoPacienteDTO);
        if (contatoPacienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new contatoPaciente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContatoPacienteDTO result = contatoPacienteService.save(contatoPacienteDTO);
        return ResponseEntity
            .created(new URI("/api/contato-pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contato-pacientes/:id} : Updates an existing contatoPaciente.
     *
     * @param id the id of the contatoPacienteDTO to save.
     * @param contatoPacienteDTO the contatoPacienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contatoPacienteDTO,
     * or with status {@code 400 (Bad Request)} if the contatoPacienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contatoPacienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contato-pacientes/{id}")
    public ResponseEntity<ContatoPacienteDTO> updateContatoPaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContatoPacienteDTO contatoPacienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContatoPaciente : {}, {}", id, contatoPacienteDTO);
        if (contatoPacienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contatoPacienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contatoPacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContatoPacienteDTO result = contatoPacienteService.save(contatoPacienteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contatoPacienteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contato-pacientes/:id} : Partial updates given fields of an existing contatoPaciente, field will ignore if it is null
     *
     * @param id the id of the contatoPacienteDTO to save.
     * @param contatoPacienteDTO the contatoPacienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contatoPacienteDTO,
     * or with status {@code 400 (Bad Request)} if the contatoPacienteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contatoPacienteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contatoPacienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contato-pacientes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContatoPacienteDTO> partialUpdateContatoPaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContatoPacienteDTO contatoPacienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContatoPaciente partially : {}, {}", id, contatoPacienteDTO);
        if (contatoPacienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contatoPacienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contatoPacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContatoPacienteDTO> result = contatoPacienteService.partialUpdate(contatoPacienteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contatoPacienteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contato-pacientes} : get all the contatoPacientes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contatoPacientes in body.
     */
    @GetMapping("/contato-pacientes")
    public ResponseEntity<List<ContatoPacienteDTO>> getAllContatoPacientes(ContatoPacienteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContatoPacientes by criteria: {}", criteria);
        Page<ContatoPacienteDTO> page = contatoPacienteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contato-pacientes/count} : count all the contatoPacientes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contato-pacientes/count")
    public ResponseEntity<Long> countContatoPacientes(ContatoPacienteCriteria criteria) {
        log.debug("REST request to count ContatoPacientes by criteria: {}", criteria);
        return ResponseEntity.ok().body(contatoPacienteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contato-pacientes/:id} : get the "id" contatoPaciente.
     *
     * @param id the id of the contatoPacienteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contatoPacienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contato-pacientes/{id}")
    public ResponseEntity<ContatoPacienteDTO> getContatoPaciente(@PathVariable Long id) {
        log.debug("REST request to get ContatoPaciente : {}", id);
        Optional<ContatoPacienteDTO> contatoPacienteDTO = contatoPacienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contatoPacienteDTO);
    }

    /**
     * {@code DELETE  /contato-pacientes/:id} : delete the "id" contatoPaciente.
     *
     * @param id the id of the contatoPacienteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contato-pacientes/{id}")
    public ResponseEntity<Void> deleteContatoPaciente(@PathVariable Long id) {
        log.debug("REST request to delete ContatoPaciente : {}", id);
        contatoPacienteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
