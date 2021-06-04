package dev.clinic.simple.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.clinic.simple.IntegrationTest;
import dev.clinic.simple.domain.ContaClinica;
import dev.clinic.simple.domain.TipoConvenio;
import dev.clinic.simple.repository.TipoConvenioRepository;
import dev.clinic.simple.service.criteria.TipoConvenioCriteria;
import dev.clinic.simple.service.dto.TipoConvenioDTO;
import dev.clinic.simple.service.mapper.TipoConvenioMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TipoConvenioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoConvenioResourceIT {

    private static final String DEFAULT_CONVENIO = "AAAAAAAAAA";
    private static final String UPDATED_CONVENIO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODIGOCONVENIO = 1;
    private static final Integer UPDATED_CODIGOCONVENIO = 2;
    private static final Integer SMALLER_CODIGOCONVENIO = 1 - 1;

    private static final String DEFAULT_NOMECONVENIO = "AAAAAAAAAA";
    private static final String UPDATED_NOMECONVENIO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTATO = "AAAAAAAAAA";
    private static final String UPDATED_CONTATO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-convenios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoConvenioRepository tipoConvenioRepository;

    @Autowired
    private TipoConvenioMapper tipoConvenioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoConvenioMockMvc;

    private TipoConvenio tipoConvenio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoConvenio createEntity(EntityManager em) {
        TipoConvenio tipoConvenio = new TipoConvenio()
            .convenio(DEFAULT_CONVENIO)
            .codigoconvenio(DEFAULT_CODIGOCONVENIO)
            .nomeconvenio(DEFAULT_NOMECONVENIO)
            .contato(DEFAULT_CONTATO);
        return tipoConvenio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoConvenio createUpdatedEntity(EntityManager em) {
        TipoConvenio tipoConvenio = new TipoConvenio()
            .convenio(UPDATED_CONVENIO)
            .codigoconvenio(UPDATED_CODIGOCONVENIO)
            .nomeconvenio(UPDATED_NOMECONVENIO)
            .contato(UPDATED_CONTATO);
        return tipoConvenio;
    }

    @BeforeEach
    public void initTest() {
        tipoConvenio = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoConvenio() throws Exception {
        int databaseSizeBeforeCreate = tipoConvenioRepository.findAll().size();
        // Create the TipoConvenio
        TipoConvenioDTO tipoConvenioDTO = tipoConvenioMapper.toDto(tipoConvenio);
        restTipoConvenioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoConvenioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeCreate + 1);
        TipoConvenio testTipoConvenio = tipoConvenioList.get(tipoConvenioList.size() - 1);
        assertThat(testTipoConvenio.getConvenio()).isEqualTo(DEFAULT_CONVENIO);
        assertThat(testTipoConvenio.getCodigoconvenio()).isEqualTo(DEFAULT_CODIGOCONVENIO);
        assertThat(testTipoConvenio.getNomeconvenio()).isEqualTo(DEFAULT_NOMECONVENIO);
        assertThat(testTipoConvenio.getContato()).isEqualTo(DEFAULT_CONTATO);
    }

    @Test
    @Transactional
    void createTipoConvenioWithExistingId() throws Exception {
        // Create the TipoConvenio with an existing ID
        tipoConvenio.setId(1L);
        TipoConvenioDTO tipoConvenioDTO = tipoConvenioMapper.toDto(tipoConvenio);

        int databaseSizeBeforeCreate = tipoConvenioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoConvenioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoConvenios() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList
        restTipoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoConvenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].convenio").value(hasItem(DEFAULT_CONVENIO)))
            .andExpect(jsonPath("$.[*].codigoconvenio").value(hasItem(DEFAULT_CODIGOCONVENIO)))
            .andExpect(jsonPath("$.[*].nomeconvenio").value(hasItem(DEFAULT_NOMECONVENIO)))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO)));
    }

    @Test
    @Transactional
    void getTipoConvenio() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get the tipoConvenio
        restTipoConvenioMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoConvenio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoConvenio.getId().intValue()))
            .andExpect(jsonPath("$.convenio").value(DEFAULT_CONVENIO))
            .andExpect(jsonPath("$.codigoconvenio").value(DEFAULT_CODIGOCONVENIO))
            .andExpect(jsonPath("$.nomeconvenio").value(DEFAULT_NOMECONVENIO))
            .andExpect(jsonPath("$.contato").value(DEFAULT_CONTATO));
    }

    @Test
    @Transactional
    void getTipoConveniosByIdFiltering() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        Long id = tipoConvenio.getId();

        defaultTipoConvenioShouldBeFound("id.equals=" + id);
        defaultTipoConvenioShouldNotBeFound("id.notEquals=" + id);

        defaultTipoConvenioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTipoConvenioShouldNotBeFound("id.greaterThan=" + id);

        defaultTipoConvenioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTipoConvenioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByConvenioIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where convenio equals to DEFAULT_CONVENIO
        defaultTipoConvenioShouldBeFound("convenio.equals=" + DEFAULT_CONVENIO);

        // Get all the tipoConvenioList where convenio equals to UPDATED_CONVENIO
        defaultTipoConvenioShouldNotBeFound("convenio.equals=" + UPDATED_CONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByConvenioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where convenio not equals to DEFAULT_CONVENIO
        defaultTipoConvenioShouldNotBeFound("convenio.notEquals=" + DEFAULT_CONVENIO);

        // Get all the tipoConvenioList where convenio not equals to UPDATED_CONVENIO
        defaultTipoConvenioShouldBeFound("convenio.notEquals=" + UPDATED_CONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByConvenioIsInShouldWork() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where convenio in DEFAULT_CONVENIO or UPDATED_CONVENIO
        defaultTipoConvenioShouldBeFound("convenio.in=" + DEFAULT_CONVENIO + "," + UPDATED_CONVENIO);

        // Get all the tipoConvenioList where convenio equals to UPDATED_CONVENIO
        defaultTipoConvenioShouldNotBeFound("convenio.in=" + UPDATED_CONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByConvenioIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where convenio is not null
        defaultTipoConvenioShouldBeFound("convenio.specified=true");

        // Get all the tipoConvenioList where convenio is null
        defaultTipoConvenioShouldNotBeFound("convenio.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoConveniosByConvenioContainsSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where convenio contains DEFAULT_CONVENIO
        defaultTipoConvenioShouldBeFound("convenio.contains=" + DEFAULT_CONVENIO);

        // Get all the tipoConvenioList where convenio contains UPDATED_CONVENIO
        defaultTipoConvenioShouldNotBeFound("convenio.contains=" + UPDATED_CONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByConvenioNotContainsSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where convenio does not contain DEFAULT_CONVENIO
        defaultTipoConvenioShouldNotBeFound("convenio.doesNotContain=" + DEFAULT_CONVENIO);

        // Get all the tipoConvenioList where convenio does not contain UPDATED_CONVENIO
        defaultTipoConvenioShouldBeFound("convenio.doesNotContain=" + UPDATED_CONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByCodigoconvenioIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where codigoconvenio equals to DEFAULT_CODIGOCONVENIO
        defaultTipoConvenioShouldBeFound("codigoconvenio.equals=" + DEFAULT_CODIGOCONVENIO);

        // Get all the tipoConvenioList where codigoconvenio equals to UPDATED_CODIGOCONVENIO
        defaultTipoConvenioShouldNotBeFound("codigoconvenio.equals=" + UPDATED_CODIGOCONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByCodigoconvenioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where codigoconvenio not equals to DEFAULT_CODIGOCONVENIO
        defaultTipoConvenioShouldNotBeFound("codigoconvenio.notEquals=" + DEFAULT_CODIGOCONVENIO);

        // Get all the tipoConvenioList where codigoconvenio not equals to UPDATED_CODIGOCONVENIO
        defaultTipoConvenioShouldBeFound("codigoconvenio.notEquals=" + UPDATED_CODIGOCONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByCodigoconvenioIsInShouldWork() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where codigoconvenio in DEFAULT_CODIGOCONVENIO or UPDATED_CODIGOCONVENIO
        defaultTipoConvenioShouldBeFound("codigoconvenio.in=" + DEFAULT_CODIGOCONVENIO + "," + UPDATED_CODIGOCONVENIO);

        // Get all the tipoConvenioList where codigoconvenio equals to UPDATED_CODIGOCONVENIO
        defaultTipoConvenioShouldNotBeFound("codigoconvenio.in=" + UPDATED_CODIGOCONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByCodigoconvenioIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where codigoconvenio is not null
        defaultTipoConvenioShouldBeFound("codigoconvenio.specified=true");

        // Get all the tipoConvenioList where codigoconvenio is null
        defaultTipoConvenioShouldNotBeFound("codigoconvenio.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoConveniosByCodigoconvenioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where codigoconvenio is greater than or equal to DEFAULT_CODIGOCONVENIO
        defaultTipoConvenioShouldBeFound("codigoconvenio.greaterThanOrEqual=" + DEFAULT_CODIGOCONVENIO);

        // Get all the tipoConvenioList where codigoconvenio is greater than or equal to UPDATED_CODIGOCONVENIO
        defaultTipoConvenioShouldNotBeFound("codigoconvenio.greaterThanOrEqual=" + UPDATED_CODIGOCONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByCodigoconvenioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where codigoconvenio is less than or equal to DEFAULT_CODIGOCONVENIO
        defaultTipoConvenioShouldBeFound("codigoconvenio.lessThanOrEqual=" + DEFAULT_CODIGOCONVENIO);

        // Get all the tipoConvenioList where codigoconvenio is less than or equal to SMALLER_CODIGOCONVENIO
        defaultTipoConvenioShouldNotBeFound("codigoconvenio.lessThanOrEqual=" + SMALLER_CODIGOCONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByCodigoconvenioIsLessThanSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where codigoconvenio is less than DEFAULT_CODIGOCONVENIO
        defaultTipoConvenioShouldNotBeFound("codigoconvenio.lessThan=" + DEFAULT_CODIGOCONVENIO);

        // Get all the tipoConvenioList where codigoconvenio is less than UPDATED_CODIGOCONVENIO
        defaultTipoConvenioShouldBeFound("codigoconvenio.lessThan=" + UPDATED_CODIGOCONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByCodigoconvenioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where codigoconvenio is greater than DEFAULT_CODIGOCONVENIO
        defaultTipoConvenioShouldNotBeFound("codigoconvenio.greaterThan=" + DEFAULT_CODIGOCONVENIO);

        // Get all the tipoConvenioList where codigoconvenio is greater than SMALLER_CODIGOCONVENIO
        defaultTipoConvenioShouldBeFound("codigoconvenio.greaterThan=" + SMALLER_CODIGOCONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByNomeconvenioIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where nomeconvenio equals to DEFAULT_NOMECONVENIO
        defaultTipoConvenioShouldBeFound("nomeconvenio.equals=" + DEFAULT_NOMECONVENIO);

        // Get all the tipoConvenioList where nomeconvenio equals to UPDATED_NOMECONVENIO
        defaultTipoConvenioShouldNotBeFound("nomeconvenio.equals=" + UPDATED_NOMECONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByNomeconvenioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where nomeconvenio not equals to DEFAULT_NOMECONVENIO
        defaultTipoConvenioShouldNotBeFound("nomeconvenio.notEquals=" + DEFAULT_NOMECONVENIO);

        // Get all the tipoConvenioList where nomeconvenio not equals to UPDATED_NOMECONVENIO
        defaultTipoConvenioShouldBeFound("nomeconvenio.notEquals=" + UPDATED_NOMECONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByNomeconvenioIsInShouldWork() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where nomeconvenio in DEFAULT_NOMECONVENIO or UPDATED_NOMECONVENIO
        defaultTipoConvenioShouldBeFound("nomeconvenio.in=" + DEFAULT_NOMECONVENIO + "," + UPDATED_NOMECONVENIO);

        // Get all the tipoConvenioList where nomeconvenio equals to UPDATED_NOMECONVENIO
        defaultTipoConvenioShouldNotBeFound("nomeconvenio.in=" + UPDATED_NOMECONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByNomeconvenioIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where nomeconvenio is not null
        defaultTipoConvenioShouldBeFound("nomeconvenio.specified=true");

        // Get all the tipoConvenioList where nomeconvenio is null
        defaultTipoConvenioShouldNotBeFound("nomeconvenio.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoConveniosByNomeconvenioContainsSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where nomeconvenio contains DEFAULT_NOMECONVENIO
        defaultTipoConvenioShouldBeFound("nomeconvenio.contains=" + DEFAULT_NOMECONVENIO);

        // Get all the tipoConvenioList where nomeconvenio contains UPDATED_NOMECONVENIO
        defaultTipoConvenioShouldNotBeFound("nomeconvenio.contains=" + UPDATED_NOMECONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByNomeconvenioNotContainsSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where nomeconvenio does not contain DEFAULT_NOMECONVENIO
        defaultTipoConvenioShouldNotBeFound("nomeconvenio.doesNotContain=" + DEFAULT_NOMECONVENIO);

        // Get all the tipoConvenioList where nomeconvenio does not contain UPDATED_NOMECONVENIO
        defaultTipoConvenioShouldBeFound("nomeconvenio.doesNotContain=" + UPDATED_NOMECONVENIO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByContatoIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where contato equals to DEFAULT_CONTATO
        defaultTipoConvenioShouldBeFound("contato.equals=" + DEFAULT_CONTATO);

        // Get all the tipoConvenioList where contato equals to UPDATED_CONTATO
        defaultTipoConvenioShouldNotBeFound("contato.equals=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByContatoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where contato not equals to DEFAULT_CONTATO
        defaultTipoConvenioShouldNotBeFound("contato.notEquals=" + DEFAULT_CONTATO);

        // Get all the tipoConvenioList where contato not equals to UPDATED_CONTATO
        defaultTipoConvenioShouldBeFound("contato.notEquals=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByContatoIsInShouldWork() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where contato in DEFAULT_CONTATO or UPDATED_CONTATO
        defaultTipoConvenioShouldBeFound("contato.in=" + DEFAULT_CONTATO + "," + UPDATED_CONTATO);

        // Get all the tipoConvenioList where contato equals to UPDATED_CONTATO
        defaultTipoConvenioShouldNotBeFound("contato.in=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByContatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where contato is not null
        defaultTipoConvenioShouldBeFound("contato.specified=true");

        // Get all the tipoConvenioList where contato is null
        defaultTipoConvenioShouldNotBeFound("contato.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoConveniosByContatoContainsSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where contato contains DEFAULT_CONTATO
        defaultTipoConvenioShouldBeFound("contato.contains=" + DEFAULT_CONTATO);

        // Get all the tipoConvenioList where contato contains UPDATED_CONTATO
        defaultTipoConvenioShouldNotBeFound("contato.contains=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByContatoNotContainsSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        // Get all the tipoConvenioList where contato does not contain DEFAULT_CONTATO
        defaultTipoConvenioShouldNotBeFound("contato.doesNotContain=" + DEFAULT_CONTATO);

        // Get all the tipoConvenioList where contato does not contain UPDATED_CONTATO
        defaultTipoConvenioShouldBeFound("contato.doesNotContain=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void getAllTipoConveniosByContaClinicaIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);
        ContaClinica contaClinica = ContaClinicaResourceIT.createEntity(em);
        em.persist(contaClinica);
        em.flush();
        tipoConvenio.setContaClinica(contaClinica);
        tipoConvenioRepository.saveAndFlush(tipoConvenio);
        Long contaClinicaId = contaClinica.getId();

        // Get all the tipoConvenioList where contaClinica equals to contaClinicaId
        defaultTipoConvenioShouldBeFound("contaClinicaId.equals=" + contaClinicaId);

        // Get all the tipoConvenioList where contaClinica equals to (contaClinicaId + 1)
        defaultTipoConvenioShouldNotBeFound("contaClinicaId.equals=" + (contaClinicaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoConvenioShouldBeFound(String filter) throws Exception {
        restTipoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoConvenio.getId().intValue())))
            .andExpect(jsonPath("$.[*].convenio").value(hasItem(DEFAULT_CONVENIO)))
            .andExpect(jsonPath("$.[*].codigoconvenio").value(hasItem(DEFAULT_CODIGOCONVENIO)))
            .andExpect(jsonPath("$.[*].nomeconvenio").value(hasItem(DEFAULT_NOMECONVENIO)))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO)));

        // Check, that the count call also returns 1
        restTipoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoConvenioShouldNotBeFound(String filter) throws Exception {
        restTipoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoConvenioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoConvenio() throws Exception {
        // Get the tipoConvenio
        restTipoConvenioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoConvenio() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        int databaseSizeBeforeUpdate = tipoConvenioRepository.findAll().size();

        // Update the tipoConvenio
        TipoConvenio updatedTipoConvenio = tipoConvenioRepository.findById(tipoConvenio.getId()).get();
        // Disconnect from session so that the updates on updatedTipoConvenio are not directly saved in db
        em.detach(updatedTipoConvenio);
        updatedTipoConvenio
            .convenio(UPDATED_CONVENIO)
            .codigoconvenio(UPDATED_CODIGOCONVENIO)
            .nomeconvenio(UPDATED_NOMECONVENIO)
            .contato(UPDATED_CONTATO);
        TipoConvenioDTO tipoConvenioDTO = tipoConvenioMapper.toDto(updatedTipoConvenio);

        restTipoConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoConvenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoConvenioDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeUpdate);
        TipoConvenio testTipoConvenio = tipoConvenioList.get(tipoConvenioList.size() - 1);
        assertThat(testTipoConvenio.getConvenio()).isEqualTo(UPDATED_CONVENIO);
        assertThat(testTipoConvenio.getCodigoconvenio()).isEqualTo(UPDATED_CODIGOCONVENIO);
        assertThat(testTipoConvenio.getNomeconvenio()).isEqualTo(UPDATED_NOMECONVENIO);
        assertThat(testTipoConvenio.getContato()).isEqualTo(UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void putNonExistingTipoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = tipoConvenioRepository.findAll().size();
        tipoConvenio.setId(count.incrementAndGet());

        // Create the TipoConvenio
        TipoConvenioDTO tipoConvenioDTO = tipoConvenioMapper.toDto(tipoConvenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoConvenioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = tipoConvenioRepository.findAll().size();
        tipoConvenio.setId(count.incrementAndGet());

        // Create the TipoConvenio
        TipoConvenioDTO tipoConvenioDTO = tipoConvenioMapper.toDto(tipoConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoConvenioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = tipoConvenioRepository.findAll().size();
        tipoConvenio.setId(count.incrementAndGet());

        // Create the TipoConvenio
        TipoConvenioDTO tipoConvenioDTO = tipoConvenioMapper.toDto(tipoConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoConvenioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoConvenioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoConvenioWithPatch() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        int databaseSizeBeforeUpdate = tipoConvenioRepository.findAll().size();

        // Update the tipoConvenio using partial update
        TipoConvenio partialUpdatedTipoConvenio = new TipoConvenio();
        partialUpdatedTipoConvenio.setId(tipoConvenio.getId());

        partialUpdatedTipoConvenio.nomeconvenio(UPDATED_NOMECONVENIO);

        restTipoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoConvenio))
            )
            .andExpect(status().isOk());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeUpdate);
        TipoConvenio testTipoConvenio = tipoConvenioList.get(tipoConvenioList.size() - 1);
        assertThat(testTipoConvenio.getConvenio()).isEqualTo(DEFAULT_CONVENIO);
        assertThat(testTipoConvenio.getCodigoconvenio()).isEqualTo(DEFAULT_CODIGOCONVENIO);
        assertThat(testTipoConvenio.getNomeconvenio()).isEqualTo(UPDATED_NOMECONVENIO);
        assertThat(testTipoConvenio.getContato()).isEqualTo(DEFAULT_CONTATO);
    }

    @Test
    @Transactional
    void fullUpdateTipoConvenioWithPatch() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        int databaseSizeBeforeUpdate = tipoConvenioRepository.findAll().size();

        // Update the tipoConvenio using partial update
        TipoConvenio partialUpdatedTipoConvenio = new TipoConvenio();
        partialUpdatedTipoConvenio.setId(tipoConvenio.getId());

        partialUpdatedTipoConvenio
            .convenio(UPDATED_CONVENIO)
            .codigoconvenio(UPDATED_CODIGOCONVENIO)
            .nomeconvenio(UPDATED_NOMECONVENIO)
            .contato(UPDATED_CONTATO);

        restTipoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoConvenio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoConvenio))
            )
            .andExpect(status().isOk());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeUpdate);
        TipoConvenio testTipoConvenio = tipoConvenioList.get(tipoConvenioList.size() - 1);
        assertThat(testTipoConvenio.getConvenio()).isEqualTo(UPDATED_CONVENIO);
        assertThat(testTipoConvenio.getCodigoconvenio()).isEqualTo(UPDATED_CODIGOCONVENIO);
        assertThat(testTipoConvenio.getNomeconvenio()).isEqualTo(UPDATED_NOMECONVENIO);
        assertThat(testTipoConvenio.getContato()).isEqualTo(UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = tipoConvenioRepository.findAll().size();
        tipoConvenio.setId(count.incrementAndGet());

        // Create the TipoConvenio
        TipoConvenioDTO tipoConvenioDTO = tipoConvenioMapper.toDto(tipoConvenio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoConvenioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = tipoConvenioRepository.findAll().size();
        tipoConvenio.setId(count.incrementAndGet());

        // Create the TipoConvenio
        TipoConvenioDTO tipoConvenioDTO = tipoConvenioMapper.toDto(tipoConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoConvenioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoConvenio() throws Exception {
        int databaseSizeBeforeUpdate = tipoConvenioRepository.findAll().size();
        tipoConvenio.setId(count.incrementAndGet());

        // Create the TipoConvenio
        TipoConvenioDTO tipoConvenioDTO = tipoConvenioMapper.toDto(tipoConvenio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoConvenioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoConvenioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoConvenio in the database
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoConvenio() throws Exception {
        // Initialize the database
        tipoConvenioRepository.saveAndFlush(tipoConvenio);

        int databaseSizeBeforeDelete = tipoConvenioRepository.findAll().size();

        // Delete the tipoConvenio
        restTipoConvenioMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoConvenio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoConvenio> tipoConvenioList = tipoConvenioRepository.findAll();
        assertThat(tipoConvenioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
