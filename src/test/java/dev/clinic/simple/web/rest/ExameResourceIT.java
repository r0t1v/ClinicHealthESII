package dev.clinic.simple.web.rest;

import static dev.clinic.simple.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.clinic.simple.IntegrationTest;
import dev.clinic.simple.domain.ContaClinica;
import dev.clinic.simple.domain.Exame;
import dev.clinic.simple.domain.Medico;
import dev.clinic.simple.domain.PagamentoExame;
import dev.clinic.simple.domain.ResultadoExame;
import dev.clinic.simple.repository.ExameRepository;
import dev.clinic.simple.service.criteria.ExameCriteria;
import dev.clinic.simple.service.dto.ExameDTO;
import dev.clinic.simple.service.mapper.ExameMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ExameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExameResourceIT {

    private static final String DEFAULT_TIPOEXAME = "AAAAAAAAAA";
    private static final String UPDATED_TIPOEXAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALOREXAME = "AAAAAAAAAA";
    private static final String UPDATED_VALOREXAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCONTOCONVENIO = "AAAAAAAAAA";
    private static final String UPDATED_DESCONTOCONVENIO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMEMMEDICO = "AAAAAAAAAA";
    private static final String UPDATED_NOMEMMEDICO = "BBBBBBBBBB";

    private static final String DEFAULT_PREREQUISITO = "AAAAAAAAAA";
    private static final String UPDATED_PREREQUISITO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATASOLICITACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATASOLICITACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATASOLICITACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATARESULTADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATARESULTADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATARESULTADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/exames";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private ExameMapper exameMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExameMockMvc;

    private Exame exame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exame createEntity(EntityManager em) {
        Exame exame = new Exame()
            .tipoexame(DEFAULT_TIPOEXAME)
            .valorexame(DEFAULT_VALOREXAME)
            .descontoconvenio(DEFAULT_DESCONTOCONVENIO)
            .nomemmedico(DEFAULT_NOMEMMEDICO)
            .prerequisito(DEFAULT_PREREQUISITO)
            .datasolicitacao(DEFAULT_DATASOLICITACAO)
            .dataresultado(DEFAULT_DATARESULTADO);
        return exame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exame createUpdatedEntity(EntityManager em) {
        Exame exame = new Exame()
            .tipoexame(UPDATED_TIPOEXAME)
            .valorexame(UPDATED_VALOREXAME)
            .descontoconvenio(UPDATED_DESCONTOCONVENIO)
            .nomemmedico(UPDATED_NOMEMMEDICO)
            .prerequisito(UPDATED_PREREQUISITO)
            .datasolicitacao(UPDATED_DATASOLICITACAO)
            .dataresultado(UPDATED_DATARESULTADO);
        return exame;
    }

    @BeforeEach
    public void initTest() {
        exame = createEntity(em);
    }

    @Test
    @Transactional
    void createExame() throws Exception {
        int databaseSizeBeforeCreate = exameRepository.findAll().size();
        // Create the Exame
        ExameDTO exameDTO = exameMapper.toDto(exame);
        restExameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exameDTO)))
            .andExpect(status().isCreated());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeCreate + 1);
        Exame testExame = exameList.get(exameList.size() - 1);
        assertThat(testExame.getTipoexame()).isEqualTo(DEFAULT_TIPOEXAME);
        assertThat(testExame.getValorexame()).isEqualTo(DEFAULT_VALOREXAME);
        assertThat(testExame.getDescontoconvenio()).isEqualTo(DEFAULT_DESCONTOCONVENIO);
        assertThat(testExame.getNomemmedico()).isEqualTo(DEFAULT_NOMEMMEDICO);
        assertThat(testExame.getPrerequisito()).isEqualTo(DEFAULT_PREREQUISITO);
        assertThat(testExame.getDatasolicitacao()).isEqualTo(DEFAULT_DATASOLICITACAO);
        assertThat(testExame.getDataresultado()).isEqualTo(DEFAULT_DATARESULTADO);
    }

    @Test
    @Transactional
    void createExameWithExistingId() throws Exception {
        // Create the Exame with an existing ID
        exame.setId(1L);
        ExameDTO exameDTO = exameMapper.toDto(exame);

        int databaseSizeBeforeCreate = exameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exameDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExames() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList
        restExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exame.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoexame").value(hasItem(DEFAULT_TIPOEXAME)))
            .andExpect(jsonPath("$.[*].valorexame").value(hasItem(DEFAULT_VALOREXAME)))
            .andExpect(jsonPath("$.[*].descontoconvenio").value(hasItem(DEFAULT_DESCONTOCONVENIO)))
            .andExpect(jsonPath("$.[*].nomemmedico").value(hasItem(DEFAULT_NOMEMMEDICO)))
            .andExpect(jsonPath("$.[*].prerequisito").value(hasItem(DEFAULT_PREREQUISITO.toString())))
            .andExpect(jsonPath("$.[*].datasolicitacao").value(hasItem(sameInstant(DEFAULT_DATASOLICITACAO))))
            .andExpect(jsonPath("$.[*].dataresultado").value(hasItem(sameInstant(DEFAULT_DATARESULTADO))));
    }

    @Test
    @Transactional
    void getExame() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get the exame
        restExameMockMvc
            .perform(get(ENTITY_API_URL_ID, exame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exame.getId().intValue()))
            .andExpect(jsonPath("$.tipoexame").value(DEFAULT_TIPOEXAME))
            .andExpect(jsonPath("$.valorexame").value(DEFAULT_VALOREXAME))
            .andExpect(jsonPath("$.descontoconvenio").value(DEFAULT_DESCONTOCONVENIO))
            .andExpect(jsonPath("$.nomemmedico").value(DEFAULT_NOMEMMEDICO))
            .andExpect(jsonPath("$.prerequisito").value(DEFAULT_PREREQUISITO.toString()))
            .andExpect(jsonPath("$.datasolicitacao").value(sameInstant(DEFAULT_DATASOLICITACAO)))
            .andExpect(jsonPath("$.dataresultado").value(sameInstant(DEFAULT_DATARESULTADO)));
    }

    @Test
    @Transactional
    void getExamesByIdFiltering() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        Long id = exame.getId();

        defaultExameShouldBeFound("id.equals=" + id);
        defaultExameShouldNotBeFound("id.notEquals=" + id);

        defaultExameShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExameShouldNotBeFound("id.greaterThan=" + id);

        defaultExameShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExameShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExamesByTipoexameIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where tipoexame equals to DEFAULT_TIPOEXAME
        defaultExameShouldBeFound("tipoexame.equals=" + DEFAULT_TIPOEXAME);

        // Get all the exameList where tipoexame equals to UPDATED_TIPOEXAME
        defaultExameShouldNotBeFound("tipoexame.equals=" + UPDATED_TIPOEXAME);
    }

    @Test
    @Transactional
    void getAllExamesByTipoexameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where tipoexame not equals to DEFAULT_TIPOEXAME
        defaultExameShouldNotBeFound("tipoexame.notEquals=" + DEFAULT_TIPOEXAME);

        // Get all the exameList where tipoexame not equals to UPDATED_TIPOEXAME
        defaultExameShouldBeFound("tipoexame.notEquals=" + UPDATED_TIPOEXAME);
    }

    @Test
    @Transactional
    void getAllExamesByTipoexameIsInShouldWork() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where tipoexame in DEFAULT_TIPOEXAME or UPDATED_TIPOEXAME
        defaultExameShouldBeFound("tipoexame.in=" + DEFAULT_TIPOEXAME + "," + UPDATED_TIPOEXAME);

        // Get all the exameList where tipoexame equals to UPDATED_TIPOEXAME
        defaultExameShouldNotBeFound("tipoexame.in=" + UPDATED_TIPOEXAME);
    }

    @Test
    @Transactional
    void getAllExamesByTipoexameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where tipoexame is not null
        defaultExameShouldBeFound("tipoexame.specified=true");

        // Get all the exameList where tipoexame is null
        defaultExameShouldNotBeFound("tipoexame.specified=false");
    }

    @Test
    @Transactional
    void getAllExamesByTipoexameContainsSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where tipoexame contains DEFAULT_TIPOEXAME
        defaultExameShouldBeFound("tipoexame.contains=" + DEFAULT_TIPOEXAME);

        // Get all the exameList where tipoexame contains UPDATED_TIPOEXAME
        defaultExameShouldNotBeFound("tipoexame.contains=" + UPDATED_TIPOEXAME);
    }

    @Test
    @Transactional
    void getAllExamesByTipoexameNotContainsSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where tipoexame does not contain DEFAULT_TIPOEXAME
        defaultExameShouldNotBeFound("tipoexame.doesNotContain=" + DEFAULT_TIPOEXAME);

        // Get all the exameList where tipoexame does not contain UPDATED_TIPOEXAME
        defaultExameShouldBeFound("tipoexame.doesNotContain=" + UPDATED_TIPOEXAME);
    }

    @Test
    @Transactional
    void getAllExamesByValorexameIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where valorexame equals to DEFAULT_VALOREXAME
        defaultExameShouldBeFound("valorexame.equals=" + DEFAULT_VALOREXAME);

        // Get all the exameList where valorexame equals to UPDATED_VALOREXAME
        defaultExameShouldNotBeFound("valorexame.equals=" + UPDATED_VALOREXAME);
    }

    @Test
    @Transactional
    void getAllExamesByValorexameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where valorexame not equals to DEFAULT_VALOREXAME
        defaultExameShouldNotBeFound("valorexame.notEquals=" + DEFAULT_VALOREXAME);

        // Get all the exameList where valorexame not equals to UPDATED_VALOREXAME
        defaultExameShouldBeFound("valorexame.notEquals=" + UPDATED_VALOREXAME);
    }

    @Test
    @Transactional
    void getAllExamesByValorexameIsInShouldWork() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where valorexame in DEFAULT_VALOREXAME or UPDATED_VALOREXAME
        defaultExameShouldBeFound("valorexame.in=" + DEFAULT_VALOREXAME + "," + UPDATED_VALOREXAME);

        // Get all the exameList where valorexame equals to UPDATED_VALOREXAME
        defaultExameShouldNotBeFound("valorexame.in=" + UPDATED_VALOREXAME);
    }

    @Test
    @Transactional
    void getAllExamesByValorexameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where valorexame is not null
        defaultExameShouldBeFound("valorexame.specified=true");

        // Get all the exameList where valorexame is null
        defaultExameShouldNotBeFound("valorexame.specified=false");
    }

    @Test
    @Transactional
    void getAllExamesByValorexameContainsSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where valorexame contains DEFAULT_VALOREXAME
        defaultExameShouldBeFound("valorexame.contains=" + DEFAULT_VALOREXAME);

        // Get all the exameList where valorexame contains UPDATED_VALOREXAME
        defaultExameShouldNotBeFound("valorexame.contains=" + UPDATED_VALOREXAME);
    }

    @Test
    @Transactional
    void getAllExamesByValorexameNotContainsSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where valorexame does not contain DEFAULT_VALOREXAME
        defaultExameShouldNotBeFound("valorexame.doesNotContain=" + DEFAULT_VALOREXAME);

        // Get all the exameList where valorexame does not contain UPDATED_VALOREXAME
        defaultExameShouldBeFound("valorexame.doesNotContain=" + UPDATED_VALOREXAME);
    }

    @Test
    @Transactional
    void getAllExamesByDescontoconvenioIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where descontoconvenio equals to DEFAULT_DESCONTOCONVENIO
        defaultExameShouldBeFound("descontoconvenio.equals=" + DEFAULT_DESCONTOCONVENIO);

        // Get all the exameList where descontoconvenio equals to UPDATED_DESCONTOCONVENIO
        defaultExameShouldNotBeFound("descontoconvenio.equals=" + UPDATED_DESCONTOCONVENIO);
    }

    @Test
    @Transactional
    void getAllExamesByDescontoconvenioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where descontoconvenio not equals to DEFAULT_DESCONTOCONVENIO
        defaultExameShouldNotBeFound("descontoconvenio.notEquals=" + DEFAULT_DESCONTOCONVENIO);

        // Get all the exameList where descontoconvenio not equals to UPDATED_DESCONTOCONVENIO
        defaultExameShouldBeFound("descontoconvenio.notEquals=" + UPDATED_DESCONTOCONVENIO);
    }

    @Test
    @Transactional
    void getAllExamesByDescontoconvenioIsInShouldWork() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where descontoconvenio in DEFAULT_DESCONTOCONVENIO or UPDATED_DESCONTOCONVENIO
        defaultExameShouldBeFound("descontoconvenio.in=" + DEFAULT_DESCONTOCONVENIO + "," + UPDATED_DESCONTOCONVENIO);

        // Get all the exameList where descontoconvenio equals to UPDATED_DESCONTOCONVENIO
        defaultExameShouldNotBeFound("descontoconvenio.in=" + UPDATED_DESCONTOCONVENIO);
    }

    @Test
    @Transactional
    void getAllExamesByDescontoconvenioIsNullOrNotNull() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where descontoconvenio is not null
        defaultExameShouldBeFound("descontoconvenio.specified=true");

        // Get all the exameList where descontoconvenio is null
        defaultExameShouldNotBeFound("descontoconvenio.specified=false");
    }

    @Test
    @Transactional
    void getAllExamesByDescontoconvenioContainsSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where descontoconvenio contains DEFAULT_DESCONTOCONVENIO
        defaultExameShouldBeFound("descontoconvenio.contains=" + DEFAULT_DESCONTOCONVENIO);

        // Get all the exameList where descontoconvenio contains UPDATED_DESCONTOCONVENIO
        defaultExameShouldNotBeFound("descontoconvenio.contains=" + UPDATED_DESCONTOCONVENIO);
    }

    @Test
    @Transactional
    void getAllExamesByDescontoconvenioNotContainsSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where descontoconvenio does not contain DEFAULT_DESCONTOCONVENIO
        defaultExameShouldNotBeFound("descontoconvenio.doesNotContain=" + DEFAULT_DESCONTOCONVENIO);

        // Get all the exameList where descontoconvenio does not contain UPDATED_DESCONTOCONVENIO
        defaultExameShouldBeFound("descontoconvenio.doesNotContain=" + UPDATED_DESCONTOCONVENIO);
    }

    @Test
    @Transactional
    void getAllExamesByNomemmedicoIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where nomemmedico equals to DEFAULT_NOMEMMEDICO
        defaultExameShouldBeFound("nomemmedico.equals=" + DEFAULT_NOMEMMEDICO);

        // Get all the exameList where nomemmedico equals to UPDATED_NOMEMMEDICO
        defaultExameShouldNotBeFound("nomemmedico.equals=" + UPDATED_NOMEMMEDICO);
    }

    @Test
    @Transactional
    void getAllExamesByNomemmedicoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where nomemmedico not equals to DEFAULT_NOMEMMEDICO
        defaultExameShouldNotBeFound("nomemmedico.notEquals=" + DEFAULT_NOMEMMEDICO);

        // Get all the exameList where nomemmedico not equals to UPDATED_NOMEMMEDICO
        defaultExameShouldBeFound("nomemmedico.notEquals=" + UPDATED_NOMEMMEDICO);
    }

    @Test
    @Transactional
    void getAllExamesByNomemmedicoIsInShouldWork() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where nomemmedico in DEFAULT_NOMEMMEDICO or UPDATED_NOMEMMEDICO
        defaultExameShouldBeFound("nomemmedico.in=" + DEFAULT_NOMEMMEDICO + "," + UPDATED_NOMEMMEDICO);

        // Get all the exameList where nomemmedico equals to UPDATED_NOMEMMEDICO
        defaultExameShouldNotBeFound("nomemmedico.in=" + UPDATED_NOMEMMEDICO);
    }

    @Test
    @Transactional
    void getAllExamesByNomemmedicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where nomemmedico is not null
        defaultExameShouldBeFound("nomemmedico.specified=true");

        // Get all the exameList where nomemmedico is null
        defaultExameShouldNotBeFound("nomemmedico.specified=false");
    }

    @Test
    @Transactional
    void getAllExamesByNomemmedicoContainsSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where nomemmedico contains DEFAULT_NOMEMMEDICO
        defaultExameShouldBeFound("nomemmedico.contains=" + DEFAULT_NOMEMMEDICO);

        // Get all the exameList where nomemmedico contains UPDATED_NOMEMMEDICO
        defaultExameShouldNotBeFound("nomemmedico.contains=" + UPDATED_NOMEMMEDICO);
    }

    @Test
    @Transactional
    void getAllExamesByNomemmedicoNotContainsSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where nomemmedico does not contain DEFAULT_NOMEMMEDICO
        defaultExameShouldNotBeFound("nomemmedico.doesNotContain=" + DEFAULT_NOMEMMEDICO);

        // Get all the exameList where nomemmedico does not contain UPDATED_NOMEMMEDICO
        defaultExameShouldBeFound("nomemmedico.doesNotContain=" + UPDATED_NOMEMMEDICO);
    }

    @Test
    @Transactional
    void getAllExamesByDatasolicitacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where datasolicitacao equals to DEFAULT_DATASOLICITACAO
        defaultExameShouldBeFound("datasolicitacao.equals=" + DEFAULT_DATASOLICITACAO);

        // Get all the exameList where datasolicitacao equals to UPDATED_DATASOLICITACAO
        defaultExameShouldNotBeFound("datasolicitacao.equals=" + UPDATED_DATASOLICITACAO);
    }

    @Test
    @Transactional
    void getAllExamesByDatasolicitacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where datasolicitacao not equals to DEFAULT_DATASOLICITACAO
        defaultExameShouldNotBeFound("datasolicitacao.notEquals=" + DEFAULT_DATASOLICITACAO);

        // Get all the exameList where datasolicitacao not equals to UPDATED_DATASOLICITACAO
        defaultExameShouldBeFound("datasolicitacao.notEquals=" + UPDATED_DATASOLICITACAO);
    }

    @Test
    @Transactional
    void getAllExamesByDatasolicitacaoIsInShouldWork() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where datasolicitacao in DEFAULT_DATASOLICITACAO or UPDATED_DATASOLICITACAO
        defaultExameShouldBeFound("datasolicitacao.in=" + DEFAULT_DATASOLICITACAO + "," + UPDATED_DATASOLICITACAO);

        // Get all the exameList where datasolicitacao equals to UPDATED_DATASOLICITACAO
        defaultExameShouldNotBeFound("datasolicitacao.in=" + UPDATED_DATASOLICITACAO);
    }

    @Test
    @Transactional
    void getAllExamesByDatasolicitacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where datasolicitacao is not null
        defaultExameShouldBeFound("datasolicitacao.specified=true");

        // Get all the exameList where datasolicitacao is null
        defaultExameShouldNotBeFound("datasolicitacao.specified=false");
    }

    @Test
    @Transactional
    void getAllExamesByDatasolicitacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where datasolicitacao is greater than or equal to DEFAULT_DATASOLICITACAO
        defaultExameShouldBeFound("datasolicitacao.greaterThanOrEqual=" + DEFAULT_DATASOLICITACAO);

        // Get all the exameList where datasolicitacao is greater than or equal to UPDATED_DATASOLICITACAO
        defaultExameShouldNotBeFound("datasolicitacao.greaterThanOrEqual=" + UPDATED_DATASOLICITACAO);
    }

    @Test
    @Transactional
    void getAllExamesByDatasolicitacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where datasolicitacao is less than or equal to DEFAULT_DATASOLICITACAO
        defaultExameShouldBeFound("datasolicitacao.lessThanOrEqual=" + DEFAULT_DATASOLICITACAO);

        // Get all the exameList where datasolicitacao is less than or equal to SMALLER_DATASOLICITACAO
        defaultExameShouldNotBeFound("datasolicitacao.lessThanOrEqual=" + SMALLER_DATASOLICITACAO);
    }

    @Test
    @Transactional
    void getAllExamesByDatasolicitacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where datasolicitacao is less than DEFAULT_DATASOLICITACAO
        defaultExameShouldNotBeFound("datasolicitacao.lessThan=" + DEFAULT_DATASOLICITACAO);

        // Get all the exameList where datasolicitacao is less than UPDATED_DATASOLICITACAO
        defaultExameShouldBeFound("datasolicitacao.lessThan=" + UPDATED_DATASOLICITACAO);
    }

    @Test
    @Transactional
    void getAllExamesByDatasolicitacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where datasolicitacao is greater than DEFAULT_DATASOLICITACAO
        defaultExameShouldNotBeFound("datasolicitacao.greaterThan=" + DEFAULT_DATASOLICITACAO);

        // Get all the exameList where datasolicitacao is greater than SMALLER_DATASOLICITACAO
        defaultExameShouldBeFound("datasolicitacao.greaterThan=" + SMALLER_DATASOLICITACAO);
    }

    @Test
    @Transactional
    void getAllExamesByDataresultadoIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where dataresultado equals to DEFAULT_DATARESULTADO
        defaultExameShouldBeFound("dataresultado.equals=" + DEFAULT_DATARESULTADO);

        // Get all the exameList where dataresultado equals to UPDATED_DATARESULTADO
        defaultExameShouldNotBeFound("dataresultado.equals=" + UPDATED_DATARESULTADO);
    }

    @Test
    @Transactional
    void getAllExamesByDataresultadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where dataresultado not equals to DEFAULT_DATARESULTADO
        defaultExameShouldNotBeFound("dataresultado.notEquals=" + DEFAULT_DATARESULTADO);

        // Get all the exameList where dataresultado not equals to UPDATED_DATARESULTADO
        defaultExameShouldBeFound("dataresultado.notEquals=" + UPDATED_DATARESULTADO);
    }

    @Test
    @Transactional
    void getAllExamesByDataresultadoIsInShouldWork() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where dataresultado in DEFAULT_DATARESULTADO or UPDATED_DATARESULTADO
        defaultExameShouldBeFound("dataresultado.in=" + DEFAULT_DATARESULTADO + "," + UPDATED_DATARESULTADO);

        // Get all the exameList where dataresultado equals to UPDATED_DATARESULTADO
        defaultExameShouldNotBeFound("dataresultado.in=" + UPDATED_DATARESULTADO);
    }

    @Test
    @Transactional
    void getAllExamesByDataresultadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where dataresultado is not null
        defaultExameShouldBeFound("dataresultado.specified=true");

        // Get all the exameList where dataresultado is null
        defaultExameShouldNotBeFound("dataresultado.specified=false");
    }

    @Test
    @Transactional
    void getAllExamesByDataresultadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where dataresultado is greater than or equal to DEFAULT_DATARESULTADO
        defaultExameShouldBeFound("dataresultado.greaterThanOrEqual=" + DEFAULT_DATARESULTADO);

        // Get all the exameList where dataresultado is greater than or equal to UPDATED_DATARESULTADO
        defaultExameShouldNotBeFound("dataresultado.greaterThanOrEqual=" + UPDATED_DATARESULTADO);
    }

    @Test
    @Transactional
    void getAllExamesByDataresultadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where dataresultado is less than or equal to DEFAULT_DATARESULTADO
        defaultExameShouldBeFound("dataresultado.lessThanOrEqual=" + DEFAULT_DATARESULTADO);

        // Get all the exameList where dataresultado is less than or equal to SMALLER_DATARESULTADO
        defaultExameShouldNotBeFound("dataresultado.lessThanOrEqual=" + SMALLER_DATARESULTADO);
    }

    @Test
    @Transactional
    void getAllExamesByDataresultadoIsLessThanSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where dataresultado is less than DEFAULT_DATARESULTADO
        defaultExameShouldNotBeFound("dataresultado.lessThan=" + DEFAULT_DATARESULTADO);

        // Get all the exameList where dataresultado is less than UPDATED_DATARESULTADO
        defaultExameShouldBeFound("dataresultado.lessThan=" + UPDATED_DATARESULTADO);
    }

    @Test
    @Transactional
    void getAllExamesByDataresultadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        // Get all the exameList where dataresultado is greater than DEFAULT_DATARESULTADO
        defaultExameShouldNotBeFound("dataresultado.greaterThan=" + DEFAULT_DATARESULTADO);

        // Get all the exameList where dataresultado is greater than SMALLER_DATARESULTADO
        defaultExameShouldBeFound("dataresultado.greaterThan=" + SMALLER_DATARESULTADO);
    }

    @Test
    @Transactional
    void getAllExamesByNomemedicoIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);
        Medico nomemedico = MedicoResourceIT.createEntity(em);
        em.persist(nomemedico);
        em.flush();
        exame.setNomemedico(nomemedico);
        exameRepository.saveAndFlush(exame);
        Long nomemedicoId = nomemedico.getId();

        // Get all the exameList where nomemedico equals to nomemedicoId
        defaultExameShouldBeFound("nomemedicoId.equals=" + nomemedicoId);

        // Get all the exameList where nomemedico equals to (nomemedicoId + 1)
        defaultExameShouldNotBeFound("nomemedicoId.equals=" + (nomemedicoId + 1));
    }

    @Test
    @Transactional
    void getAllExamesByTipoexameIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);
        ResultadoExame tipoexame = ResultadoExameResourceIT.createEntity(em);
        em.persist(tipoexame);
        em.flush();
        exame.setTipoexame(tipoexame);
        exameRepository.saveAndFlush(exame);
        Long tipoexameId = tipoexame.getId();

        // Get all the exameList where tipoexame equals to tipoexameId
        defaultExameShouldBeFound("tipoexameId.equals=" + tipoexameId);

        // Get all the exameList where tipoexame equals to (tipoexameId + 1)
        defaultExameShouldNotBeFound("tipoexameId.equals=" + (tipoexameId + 1));
    }

    @Test
    @Transactional
    void getAllExamesByValorexameIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);
        PagamentoExame valorexame = PagamentoExameResourceIT.createEntity(em);
        em.persist(valorexame);
        em.flush();
        exame.addValorexame(valorexame);
        exameRepository.saveAndFlush(exame);
        Long valorexameId = valorexame.getId();

        // Get all the exameList where valorexame equals to valorexameId
        defaultExameShouldBeFound("valorexameId.equals=" + valorexameId);

        // Get all the exameList where valorexame equals to (valorexameId + 1)
        defaultExameShouldNotBeFound("valorexameId.equals=" + (valorexameId + 1));
    }

    @Test
    @Transactional
    void getAllExamesByContaClinicaIsEqualToSomething() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);
        ContaClinica contaClinica = ContaClinicaResourceIT.createEntity(em);
        em.persist(contaClinica);
        em.flush();
        exame.setContaClinica(contaClinica);
        exameRepository.saveAndFlush(exame);
        Long contaClinicaId = contaClinica.getId();

        // Get all the exameList where contaClinica equals to contaClinicaId
        defaultExameShouldBeFound("contaClinicaId.equals=" + contaClinicaId);

        // Get all the exameList where contaClinica equals to (contaClinicaId + 1)
        defaultExameShouldNotBeFound("contaClinicaId.equals=" + (contaClinicaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExameShouldBeFound(String filter) throws Exception {
        restExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exame.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoexame").value(hasItem(DEFAULT_TIPOEXAME)))
            .andExpect(jsonPath("$.[*].valorexame").value(hasItem(DEFAULT_VALOREXAME)))
            .andExpect(jsonPath("$.[*].descontoconvenio").value(hasItem(DEFAULT_DESCONTOCONVENIO)))
            .andExpect(jsonPath("$.[*].nomemmedico").value(hasItem(DEFAULT_NOMEMMEDICO)))
            .andExpect(jsonPath("$.[*].prerequisito").value(hasItem(DEFAULT_PREREQUISITO.toString())))
            .andExpect(jsonPath("$.[*].datasolicitacao").value(hasItem(sameInstant(DEFAULT_DATASOLICITACAO))))
            .andExpect(jsonPath("$.[*].dataresultado").value(hasItem(sameInstant(DEFAULT_DATARESULTADO))));

        // Check, that the count call also returns 1
        restExameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExameShouldNotBeFound(String filter) throws Exception {
        restExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExame() throws Exception {
        // Get the exame
        restExameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExame() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        int databaseSizeBeforeUpdate = exameRepository.findAll().size();

        // Update the exame
        Exame updatedExame = exameRepository.findById(exame.getId()).get();
        // Disconnect from session so that the updates on updatedExame are not directly saved in db
        em.detach(updatedExame);
        updatedExame
            .tipoexame(UPDATED_TIPOEXAME)
            .valorexame(UPDATED_VALOREXAME)
            .descontoconvenio(UPDATED_DESCONTOCONVENIO)
            .nomemmedico(UPDATED_NOMEMMEDICO)
            .prerequisito(UPDATED_PREREQUISITO)
            .datasolicitacao(UPDATED_DATASOLICITACAO)
            .dataresultado(UPDATED_DATARESULTADO);
        ExameDTO exameDTO = exameMapper.toDto(updatedExame);

        restExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exameDTO))
            )
            .andExpect(status().isOk());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
        Exame testExame = exameList.get(exameList.size() - 1);
        assertThat(testExame.getTipoexame()).isEqualTo(UPDATED_TIPOEXAME);
        assertThat(testExame.getValorexame()).isEqualTo(UPDATED_VALOREXAME);
        assertThat(testExame.getDescontoconvenio()).isEqualTo(UPDATED_DESCONTOCONVENIO);
        assertThat(testExame.getNomemmedico()).isEqualTo(UPDATED_NOMEMMEDICO);
        assertThat(testExame.getPrerequisito()).isEqualTo(UPDATED_PREREQUISITO);
        assertThat(testExame.getDatasolicitacao()).isEqualTo(UPDATED_DATASOLICITACAO);
        assertThat(testExame.getDataresultado()).isEqualTo(UPDATED_DATARESULTADO);
    }

    @Test
    @Transactional
    void putNonExistingExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // Create the Exame
        ExameDTO exameDTO = exameMapper.toDto(exame);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // Create the Exame
        ExameDTO exameDTO = exameMapper.toDto(exame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // Create the Exame
        ExameDTO exameDTO = exameMapper.toDto(exame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExameWithPatch() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        int databaseSizeBeforeUpdate = exameRepository.findAll().size();

        // Update the exame using partial update
        Exame partialUpdatedExame = new Exame();
        partialUpdatedExame.setId(exame.getId());

        partialUpdatedExame
            .tipoexame(UPDATED_TIPOEXAME)
            .descontoconvenio(UPDATED_DESCONTOCONVENIO)
            .prerequisito(UPDATED_PREREQUISITO)
            .datasolicitacao(UPDATED_DATASOLICITACAO);

        restExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExame))
            )
            .andExpect(status().isOk());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
        Exame testExame = exameList.get(exameList.size() - 1);
        assertThat(testExame.getTipoexame()).isEqualTo(UPDATED_TIPOEXAME);
        assertThat(testExame.getValorexame()).isEqualTo(DEFAULT_VALOREXAME);
        assertThat(testExame.getDescontoconvenio()).isEqualTo(UPDATED_DESCONTOCONVENIO);
        assertThat(testExame.getNomemmedico()).isEqualTo(DEFAULT_NOMEMMEDICO);
        assertThat(testExame.getPrerequisito()).isEqualTo(UPDATED_PREREQUISITO);
        assertThat(testExame.getDatasolicitacao()).isEqualTo(UPDATED_DATASOLICITACAO);
        assertThat(testExame.getDataresultado()).isEqualTo(DEFAULT_DATARESULTADO);
    }

    @Test
    @Transactional
    void fullUpdateExameWithPatch() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        int databaseSizeBeforeUpdate = exameRepository.findAll().size();

        // Update the exame using partial update
        Exame partialUpdatedExame = new Exame();
        partialUpdatedExame.setId(exame.getId());

        partialUpdatedExame
            .tipoexame(UPDATED_TIPOEXAME)
            .valorexame(UPDATED_VALOREXAME)
            .descontoconvenio(UPDATED_DESCONTOCONVENIO)
            .nomemmedico(UPDATED_NOMEMMEDICO)
            .prerequisito(UPDATED_PREREQUISITO)
            .datasolicitacao(UPDATED_DATASOLICITACAO)
            .dataresultado(UPDATED_DATARESULTADO);

        restExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExame))
            )
            .andExpect(status().isOk());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
        Exame testExame = exameList.get(exameList.size() - 1);
        assertThat(testExame.getTipoexame()).isEqualTo(UPDATED_TIPOEXAME);
        assertThat(testExame.getValorexame()).isEqualTo(UPDATED_VALOREXAME);
        assertThat(testExame.getDescontoconvenio()).isEqualTo(UPDATED_DESCONTOCONVENIO);
        assertThat(testExame.getNomemmedico()).isEqualTo(UPDATED_NOMEMMEDICO);
        assertThat(testExame.getPrerequisito()).isEqualTo(UPDATED_PREREQUISITO);
        assertThat(testExame.getDatasolicitacao()).isEqualTo(UPDATED_DATASOLICITACAO);
        assertThat(testExame.getDataresultado()).isEqualTo(UPDATED_DATARESULTADO);
    }

    @Test
    @Transactional
    void patchNonExistingExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // Create the Exame
        ExameDTO exameDTO = exameMapper.toDto(exame);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exameDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // Create the Exame
        ExameDTO exameDTO = exameMapper.toDto(exame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExame() throws Exception {
        int databaseSizeBeforeUpdate = exameRepository.findAll().size();
        exame.setId(count.incrementAndGet());

        // Create the Exame
        ExameDTO exameDTO = exameMapper.toDto(exame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExameMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exame in the database
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExame() throws Exception {
        // Initialize the database
        exameRepository.saveAndFlush(exame);

        int databaseSizeBeforeDelete = exameRepository.findAll().size();

        // Delete the exame
        restExameMockMvc
            .perform(delete(ENTITY_API_URL_ID, exame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exame> exameList = exameRepository.findAll();
        assertThat(exameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
