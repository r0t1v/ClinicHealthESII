package dev.clinic.simple.web.rest;

import dev.clinic.simple.repository.TipoConvenioRepository;
import dev.clinic.simple.service.TipoConvenioQueryService;
import dev.clinic.simple.service.TipoConvenioService;
import dev.clinic.simple.service.criteria.TipoConvenioCriteria;
import dev.clinic.simple.service.dto.TipoConvenioDTO;
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
 * REST controller for managing {@link dev.clinic.simple.domain.TipoConvenio}.
 */
@RestController
@RequestMapping("/api")
public class TipoConvenioResource {

    private final Logger log = LoggerFactory.getLogger(TipoConvenioResource.class);

    private static final String ENTITY_NAME = "tipoConvenio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoConvenioService tipoConvenioService;

    private final TipoConvenioRepository tipoConvenioRepository;

    private final TipoConvenioQueryService tipoConvenioQueryService;

    public TipoConvenioResource(
        TipoConvenioService tipoConvenioService,
        TipoConvenioRepository tipoConvenioRepository,
        TipoConvenioQueryService tipoConvenioQueryService
    ) {
        this.tipoConvenioService = tipoConvenioService;
        this.tipoConvenioRepository = tipoConvenioRepository;
        this.tipoConvenioQueryService = tipoConvenioQueryService;
    }

    /**
     * {@code POST  /tipo-convenios} : Create a new tipoConvenio.
     *
     * @param tipoConvenioDTO the tipoConvenioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoConvenioDTO, or with status {@code 400 (Bad Request)} if the tipoConvenio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-convenios")
    public ResponseEntity<TipoConvenioDTO> createTipoConvenio(@RequestBody TipoConvenioDTO tipoConvenioDTO) throws URISyntaxException {
        log.debug("REST request to save TipoConvenio : {}", tipoConvenioDTO);
        if (tipoConvenioDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoConvenio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoConvenioDTO result = tipoConvenioService.save(tipoConvenioDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-convenios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-convenios/:id} : Updates an existing tipoConvenio.
     *
     * @param id the id of the tipoConvenioDTO to save.
     * @param tipoConvenioDTO the tipoConvenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoConvenioDTO,
     * or with status {@code 400 (Bad Request)} if the tipoConvenioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoConvenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-convenios/{id}")
    public ResponseEntity<TipoConvenioDTO> updateTipoConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoConvenioDTO tipoConvenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoConvenio : {}, {}", id, tipoConvenioDTO);
        if (tipoConvenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoConvenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoConvenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoConvenioDTO result = tipoConvenioService.save(tipoConvenioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoConvenioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-convenios/:id} : Partial updates given fields of an existing tipoConvenio, field will ignore if it is null
     *
     * @param id the id of the tipoConvenioDTO to save.
     * @param tipoConvenioDTO the tipoConvenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoConvenioDTO,
     * or with status {@code 400 (Bad Request)} if the tipoConvenioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoConvenioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoConvenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-convenios/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TipoConvenioDTO> partialUpdateTipoConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoConvenioDTO tipoConvenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoConvenio partially : {}, {}", id, tipoConvenioDTO);
        if (tipoConvenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoConvenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoConvenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoConvenioDTO> result = tipoConvenioService.partialUpdate(tipoConvenioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoConvenioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-convenios} : get all the tipoConvenios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoConvenios in body.
     */
    @GetMapping("/tipo-convenios")
    public ResponseEntity<List<TipoConvenioDTO>> getAllTipoConvenios(TipoConvenioCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TipoConvenios by criteria: {}", criteria);
        Page<TipoConvenioDTO> page = tipoConvenioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-convenios/count} : count all the tipoConvenios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tipo-convenios/count")
    public ResponseEntity<Long> countTipoConvenios(TipoConvenioCriteria criteria) {
        log.debug("REST request to count TipoConvenios by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoConvenioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-convenios/:id} : get the "id" tipoConvenio.
     *
     * @param id the id of the tipoConvenioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoConvenioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-convenios/{id}")
    public ResponseEntity<TipoConvenioDTO> getTipoConvenio(@PathVariable Long id) {
        log.debug("REST request to get TipoConvenio : {}", id);
        Optional<TipoConvenioDTO> tipoConvenioDTO = tipoConvenioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoConvenioDTO);
    }

    /**
     * {@code DELETE  /tipo-convenios/:id} : delete the "id" tipoConvenio.
     *
     * @param id the id of the tipoConvenioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-convenios/{id}")
    public ResponseEntity<Void> deleteTipoConvenio(@PathVariable Long id) {
        log.debug("REST request to delete TipoConvenio : {}", id);
        tipoConvenioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
