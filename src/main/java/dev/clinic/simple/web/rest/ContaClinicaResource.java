package dev.clinic.simple.web.rest;

import dev.clinic.simple.repository.ContaClinicaRepository;
import dev.clinic.simple.service.ContaClinicaQueryService;
import dev.clinic.simple.service.ContaClinicaService;
import dev.clinic.simple.service.criteria.ContaClinicaCriteria;
import dev.clinic.simple.service.dto.ContaClinicaDTO;
import dev.clinic.simple.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link dev.clinic.simple.domain.ContaClinica}.
 */
@RestController
@RequestMapping("/api")
public class ContaClinicaResource {

    private final Logger log = LoggerFactory.getLogger(ContaClinicaResource.class);

    private static final String ENTITY_NAME = "contaClinica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContaClinicaService contaClinicaService;

    private final ContaClinicaRepository contaClinicaRepository;

    private final ContaClinicaQueryService contaClinicaQueryService;

    public ContaClinicaResource(
        ContaClinicaService contaClinicaService,
        ContaClinicaRepository contaClinicaRepository,
        ContaClinicaQueryService contaClinicaQueryService
    ) {
        this.contaClinicaService = contaClinicaService;
        this.contaClinicaRepository = contaClinicaRepository;
        this.contaClinicaQueryService = contaClinicaQueryService;
    }

    /**
     * {@code POST  /conta-clinicas} : Create a new contaClinica.
     *
     * @param contaClinicaDTO the contaClinicaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contaClinicaDTO, or with status {@code 400 (Bad Request)} if the contaClinica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conta-clinicas")
    public ResponseEntity<ContaClinicaDTO> createContaClinica(@Valid @RequestBody ContaClinicaDTO contaClinicaDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContaClinica : {}", contaClinicaDTO);
        if (contaClinicaDTO.getId() != null) {
            throw new BadRequestAlertException("A new contaClinica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContaClinicaDTO result = contaClinicaService.save(contaClinicaDTO);
        return ResponseEntity
            .created(new URI("/api/conta-clinicas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conta-clinicas/:id} : Updates an existing contaClinica.
     *
     * @param id the id of the contaClinicaDTO to save.
     * @param contaClinicaDTO the contaClinicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaClinicaDTO,
     * or with status {@code 400 (Bad Request)} if the contaClinicaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contaClinicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conta-clinicas/{id}")
    public ResponseEntity<ContaClinicaDTO> updateContaClinica(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContaClinicaDTO contaClinicaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContaClinica : {}, {}", id, contaClinicaDTO);
        if (contaClinicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaClinicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaClinicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContaClinicaDTO result = contaClinicaService.save(contaClinicaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contaClinicaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conta-clinicas/:id} : Partial updates given fields of an existing contaClinica, field will ignore if it is null
     *
     * @param id the id of the contaClinicaDTO to save.
     * @param contaClinicaDTO the contaClinicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaClinicaDTO,
     * or with status {@code 400 (Bad Request)} if the contaClinicaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contaClinicaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contaClinicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conta-clinicas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContaClinicaDTO> partialUpdateContaClinica(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContaClinicaDTO contaClinicaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContaClinica partially : {}, {}", id, contaClinicaDTO);
        if (contaClinicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaClinicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaClinicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContaClinicaDTO> result = contaClinicaService.partialUpdate(contaClinicaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contaClinicaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conta-clinicas} : get all the contaClinicas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contaClinicas in body.
     */
    @GetMapping("/conta-clinicas")
    public ResponseEntity<List<ContaClinicaDTO>> getAllContaClinicas(ContaClinicaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContaClinicas by criteria: {}", criteria);
        Page<ContaClinicaDTO> page = contaClinicaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conta-clinicas/count} : count all the contaClinicas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/conta-clinicas/count")
    public ResponseEntity<Long> countContaClinicas(ContaClinicaCriteria criteria) {
        log.debug("REST request to count ContaClinicas by criteria: {}", criteria);
        return ResponseEntity.ok().body(contaClinicaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /conta-clinicas/:id} : get the "id" contaClinica.
     *
     * @param id the id of the contaClinicaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contaClinicaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conta-clinicas/{id}")
    public ResponseEntity<ContaClinicaDTO> getContaClinica(@PathVariable Long id) {
        log.debug("REST request to get ContaClinica : {}", id);
        Optional<ContaClinicaDTO> contaClinicaDTO = contaClinicaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contaClinicaDTO);
    }

    /**
     * {@code DELETE  /conta-clinicas/:id} : delete the "id" contaClinica.
     *
     * @param id the id of the contaClinicaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conta-clinicas/{id}")
    public ResponseEntity<Void> deleteContaClinica(@PathVariable Long id) {
        log.debug("REST request to delete ContaClinica : {}", id);
        contaClinicaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
