package dev.clinic.simple.web.rest;

import dev.clinic.simple.repository.MedicoRepository;
import dev.clinic.simple.service.MedicoQueryService;
import dev.clinic.simple.service.MedicoService;
import dev.clinic.simple.service.criteria.MedicoCriteria;
import dev.clinic.simple.service.dto.MedicoDTO;
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
 * REST controller for managing {@link dev.clinic.simple.domain.Medico}.
 */
@RestController
@RequestMapping("/api")
public class MedicoResource {

    private final Logger log = LoggerFactory.getLogger(MedicoResource.class);

    private static final String ENTITY_NAME = "medico";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicoService medicoService;

    private final MedicoRepository medicoRepository;

    private final MedicoQueryService medicoQueryService;

    public MedicoResource(MedicoService medicoService, MedicoRepository medicoRepository, MedicoQueryService medicoQueryService) {
        this.medicoService = medicoService;
        this.medicoRepository = medicoRepository;
        this.medicoQueryService = medicoQueryService;
    }

    /**
     * {@code POST  /medicos} : Create a new medico.
     *
     * @param medicoDTO the medicoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicoDTO, or with status {@code 400 (Bad Request)} if the medico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medicos")
    public ResponseEntity<MedicoDTO> createMedico(@RequestBody MedicoDTO medicoDTO) throws URISyntaxException {
        log.debug("REST request to save Medico : {}", medicoDTO);
        if (medicoDTO.getId() != null) {
            throw new BadRequestAlertException("A new medico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedicoDTO result = medicoService.save(medicoDTO);
        return ResponseEntity
            .created(new URI("/api/medicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medicos/:id} : Updates an existing medico.
     *
     * @param id the id of the medicoDTO to save.
     * @param medicoDTO the medicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicoDTO,
     * or with status {@code 400 (Bad Request)} if the medicoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medicos/{id}")
    public ResponseEntity<MedicoDTO> updateMedico(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MedicoDTO medicoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Medico : {}, {}", id, medicoDTO);
        if (medicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MedicoDTO result = medicoService.save(medicoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medicos/:id} : Partial updates given fields of an existing medico, field will ignore if it is null
     *
     * @param id the id of the medicoDTO to save.
     * @param medicoDTO the medicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicoDTO,
     * or with status {@code 400 (Bad Request)} if the medicoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medicos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MedicoDTO> partialUpdateMedico(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MedicoDTO medicoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medico partially : {}, {}", id, medicoDTO);
        if (medicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicoDTO> result = medicoService.partialUpdate(medicoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medicos} : get all the medicos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicos in body.
     */
    @GetMapping("/medicos")
    public ResponseEntity<List<MedicoDTO>> getAllMedicos(MedicoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Medicos by criteria: {}", criteria);
        Page<MedicoDTO> page = medicoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medicos/count} : count all the medicos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/medicos/count")
    public ResponseEntity<Long> countMedicos(MedicoCriteria criteria) {
        log.debug("REST request to count Medicos by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medicos/:id} : get the "id" medico.
     *
     * @param id the id of the medicoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medicos/{id}")
    public ResponseEntity<MedicoDTO> getMedico(@PathVariable Long id) {
        log.debug("REST request to get Medico : {}", id);
        Optional<MedicoDTO> medicoDTO = medicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicoDTO);
    }

    /**
     * {@code DELETE  /medicos/:id} : delete the "id" medico.
     *
     * @param id the id of the medicoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medicos/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable Long id) {
        log.debug("REST request to delete Medico : {}", id);
        medicoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
