package dev.clinic.simple.web.rest;

import dev.clinic.simple.repository.ResultadoExameRepository;
import dev.clinic.simple.service.ResultadoExameQueryService;
import dev.clinic.simple.service.ResultadoExameService;
import dev.clinic.simple.service.criteria.ResultadoExameCriteria;
import dev.clinic.simple.service.dto.ResultadoExameDTO;
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
 * REST controller for managing {@link dev.clinic.simple.domain.ResultadoExame}.
 */
@RestController
@RequestMapping("/api")
public class ResultadoExameResource {

    private final Logger log = LoggerFactory.getLogger(ResultadoExameResource.class);

    private static final String ENTITY_NAME = "resultadoExame";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResultadoExameService resultadoExameService;

    private final ResultadoExameRepository resultadoExameRepository;

    private final ResultadoExameQueryService resultadoExameQueryService;

    public ResultadoExameResource(
        ResultadoExameService resultadoExameService,
        ResultadoExameRepository resultadoExameRepository,
        ResultadoExameQueryService resultadoExameQueryService
    ) {
        this.resultadoExameService = resultadoExameService;
        this.resultadoExameRepository = resultadoExameRepository;
        this.resultadoExameQueryService = resultadoExameQueryService;
    }

    /**
     * {@code POST  /resultado-exames} : Create a new resultadoExame.
     *
     * @param resultadoExameDTO the resultadoExameDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resultadoExameDTO, or with status {@code 400 (Bad Request)} if the resultadoExame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resultado-exames")
    public ResponseEntity<ResultadoExameDTO> createResultadoExame(@RequestBody ResultadoExameDTO resultadoExameDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResultadoExame : {}", resultadoExameDTO);
        if (resultadoExameDTO.getId() != null) {
            throw new BadRequestAlertException("A new resultadoExame cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResultadoExameDTO result = resultadoExameService.save(resultadoExameDTO);
        return ResponseEntity
            .created(new URI("/api/resultado-exames/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resultado-exames/:id} : Updates an existing resultadoExame.
     *
     * @param id the id of the resultadoExameDTO to save.
     * @param resultadoExameDTO the resultadoExameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultadoExameDTO,
     * or with status {@code 400 (Bad Request)} if the resultadoExameDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resultadoExameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resultado-exames/{id}")
    public ResponseEntity<ResultadoExameDTO> updateResultadoExame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResultadoExameDTO resultadoExameDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResultadoExame : {}, {}", id, resultadoExameDTO);
        if (resultadoExameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resultadoExameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resultadoExameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResultadoExameDTO result = resultadoExameService.save(resultadoExameDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resultadoExameDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resultado-exames/:id} : Partial updates given fields of an existing resultadoExame, field will ignore if it is null
     *
     * @param id the id of the resultadoExameDTO to save.
     * @param resultadoExameDTO the resultadoExameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultadoExameDTO,
     * or with status {@code 400 (Bad Request)} if the resultadoExameDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resultadoExameDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resultadoExameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resultado-exames/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ResultadoExameDTO> partialUpdateResultadoExame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResultadoExameDTO resultadoExameDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResultadoExame partially : {}, {}", id, resultadoExameDTO);
        if (resultadoExameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resultadoExameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resultadoExameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResultadoExameDTO> result = resultadoExameService.partialUpdate(resultadoExameDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resultadoExameDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resultado-exames} : get all the resultadoExames.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resultadoExames in body.
     */
    @GetMapping("/resultado-exames")
    public ResponseEntity<List<ResultadoExameDTO>> getAllResultadoExames(ResultadoExameCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ResultadoExames by criteria: {}", criteria);
        Page<ResultadoExameDTO> page = resultadoExameQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resultado-exames/count} : count all the resultadoExames.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/resultado-exames/count")
    public ResponseEntity<Long> countResultadoExames(ResultadoExameCriteria criteria) {
        log.debug("REST request to count ResultadoExames by criteria: {}", criteria);
        return ResponseEntity.ok().body(resultadoExameQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /resultado-exames/:id} : get the "id" resultadoExame.
     *
     * @param id the id of the resultadoExameDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resultadoExameDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resultado-exames/{id}")
    public ResponseEntity<ResultadoExameDTO> getResultadoExame(@PathVariable Long id) {
        log.debug("REST request to get ResultadoExame : {}", id);
        Optional<ResultadoExameDTO> resultadoExameDTO = resultadoExameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resultadoExameDTO);
    }

    /**
     * {@code DELETE  /resultado-exames/:id} : delete the "id" resultadoExame.
     *
     * @param id the id of the resultadoExameDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resultado-exames/{id}")
    public ResponseEntity<Void> deleteResultadoExame(@PathVariable Long id) {
        log.debug("REST request to delete ResultadoExame : {}", id);
        resultadoExameService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
