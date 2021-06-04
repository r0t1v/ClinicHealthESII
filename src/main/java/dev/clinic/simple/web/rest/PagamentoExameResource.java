package dev.clinic.simple.web.rest;

import dev.clinic.simple.repository.PagamentoExameRepository;
import dev.clinic.simple.service.PagamentoExameQueryService;
import dev.clinic.simple.service.PagamentoExameService;
import dev.clinic.simple.service.criteria.PagamentoExameCriteria;
import dev.clinic.simple.service.dto.PagamentoExameDTO;
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
 * REST controller for managing {@link dev.clinic.simple.domain.PagamentoExame}.
 */
@RestController
@RequestMapping("/api")
public class PagamentoExameResource {

    private final Logger log = LoggerFactory.getLogger(PagamentoExameResource.class);

    private static final String ENTITY_NAME = "pagamentoExame";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PagamentoExameService pagamentoExameService;

    private final PagamentoExameRepository pagamentoExameRepository;

    private final PagamentoExameQueryService pagamentoExameQueryService;

    public PagamentoExameResource(
        PagamentoExameService pagamentoExameService,
        PagamentoExameRepository pagamentoExameRepository,
        PagamentoExameQueryService pagamentoExameQueryService
    ) {
        this.pagamentoExameService = pagamentoExameService;
        this.pagamentoExameRepository = pagamentoExameRepository;
        this.pagamentoExameQueryService = pagamentoExameQueryService;
    }

    /**
     * {@code POST  /pagamento-exames} : Create a new pagamentoExame.
     *
     * @param pagamentoExameDTO the pagamentoExameDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pagamentoExameDTO, or with status {@code 400 (Bad Request)} if the pagamentoExame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pagamento-exames")
    public ResponseEntity<PagamentoExameDTO> createPagamentoExame(@RequestBody PagamentoExameDTO pagamentoExameDTO)
        throws URISyntaxException {
        log.debug("REST request to save PagamentoExame : {}", pagamentoExameDTO);
        if (pagamentoExameDTO.getId() != null) {
            throw new BadRequestAlertException("A new pagamentoExame cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PagamentoExameDTO result = pagamentoExameService.save(pagamentoExameDTO);
        return ResponseEntity
            .created(new URI("/api/pagamento-exames/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pagamento-exames/:id} : Updates an existing pagamentoExame.
     *
     * @param id the id of the pagamentoExameDTO to save.
     * @param pagamentoExameDTO the pagamentoExameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagamentoExameDTO,
     * or with status {@code 400 (Bad Request)} if the pagamentoExameDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pagamentoExameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pagamento-exames/{id}")
    public ResponseEntity<PagamentoExameDTO> updatePagamentoExame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PagamentoExameDTO pagamentoExameDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PagamentoExame : {}, {}", id, pagamentoExameDTO);
        if (pagamentoExameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagamentoExameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pagamentoExameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PagamentoExameDTO result = pagamentoExameService.save(pagamentoExameDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pagamentoExameDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pagamento-exames/:id} : Partial updates given fields of an existing pagamentoExame, field will ignore if it is null
     *
     * @param id the id of the pagamentoExameDTO to save.
     * @param pagamentoExameDTO the pagamentoExameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagamentoExameDTO,
     * or with status {@code 400 (Bad Request)} if the pagamentoExameDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pagamentoExameDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pagamentoExameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pagamento-exames/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PagamentoExameDTO> partialUpdatePagamentoExame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PagamentoExameDTO pagamentoExameDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PagamentoExame partially : {}, {}", id, pagamentoExameDTO);
        if (pagamentoExameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagamentoExameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pagamentoExameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PagamentoExameDTO> result = pagamentoExameService.partialUpdate(pagamentoExameDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pagamentoExameDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pagamento-exames} : get all the pagamentoExames.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pagamentoExames in body.
     */
    @GetMapping("/pagamento-exames")
    public ResponseEntity<List<PagamentoExameDTO>> getAllPagamentoExames(PagamentoExameCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PagamentoExames by criteria: {}", criteria);
        Page<PagamentoExameDTO> page = pagamentoExameQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pagamento-exames/count} : count all the pagamentoExames.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pagamento-exames/count")
    public ResponseEntity<Long> countPagamentoExames(PagamentoExameCriteria criteria) {
        log.debug("REST request to count PagamentoExames by criteria: {}", criteria);
        return ResponseEntity.ok().body(pagamentoExameQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pagamento-exames/:id} : get the "id" pagamentoExame.
     *
     * @param id the id of the pagamentoExameDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pagamentoExameDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pagamento-exames/{id}")
    public ResponseEntity<PagamentoExameDTO> getPagamentoExame(@PathVariable Long id) {
        log.debug("REST request to get PagamentoExame : {}", id);
        Optional<PagamentoExameDTO> pagamentoExameDTO = pagamentoExameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pagamentoExameDTO);
    }

    /**
     * {@code DELETE  /pagamento-exames/:id} : delete the "id" pagamentoExame.
     *
     * @param id the id of the pagamentoExameDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pagamento-exames/{id}")
    public ResponseEntity<Void> deletePagamentoExame(@PathVariable Long id) {
        log.debug("REST request to delete PagamentoExame : {}", id);
        pagamentoExameService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
