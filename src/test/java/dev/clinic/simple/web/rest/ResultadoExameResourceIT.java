package dev.clinic.simple.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.clinic.simple.IntegrationTest;
import dev.clinic.simple.domain.ResultadoExame;
import dev.clinic.simple.repository.ResultadoExameRepository;
import dev.clinic.simple.service.criteria.ResultadoExameCriteria;
import dev.clinic.simple.service.dto.ResultadoExameDTO;
import dev.clinic.simple.service.mapper.ResultadoExameMapper;
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
 * Integration tests for the {@link ResultadoExameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResultadoExameResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_PRESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_PRESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_INDICACAO = "AAAAAAAAAA";
    private static final String UPDATED_INDICACAO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRAINDICACAO = "AAAAAAAAAA";
    private static final String UPDATED_CONTRAINDICACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/resultado-exames";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResultadoExameRepository resultadoExameRepository;

    @Autowired
    private ResultadoExameMapper resultadoExameMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResultadoExameMockMvc;

    private ResultadoExame resultadoExame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultadoExame createEntity(EntityManager em) {
        ResultadoExame resultadoExame = new ResultadoExame()
            .descricao(DEFAULT_DESCRICAO)
            .prescricao(DEFAULT_PRESCRICAO)
            .indicacao(DEFAULT_INDICACAO)
            .contraindicacao(DEFAULT_CONTRAINDICACAO);
        return resultadoExame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultadoExame createUpdatedEntity(EntityManager em) {
        ResultadoExame resultadoExame = new ResultadoExame()
            .descricao(UPDATED_DESCRICAO)
            .prescricao(UPDATED_PRESCRICAO)
            .indicacao(UPDATED_INDICACAO)
            .contraindicacao(UPDATED_CONTRAINDICACAO);
        return resultadoExame;
    }

    @BeforeEach
    public void initTest() {
        resultadoExame = createEntity(em);
    }

    @Test
    @Transactional
    void createResultadoExame() throws Exception {
        int databaseSizeBeforeCreate = resultadoExameRepository.findAll().size();
        // Create the ResultadoExame
        ResultadoExameDTO resultadoExameDTO = resultadoExameMapper.toDto(resultadoExame);
        restResultadoExameMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultadoExameDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeCreate + 1);
        ResultadoExame testResultadoExame = resultadoExameList.get(resultadoExameList.size() - 1);
        assertThat(testResultadoExame.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testResultadoExame.getPrescricao()).isEqualTo(DEFAULT_PRESCRICAO);
        assertThat(testResultadoExame.getIndicacao()).isEqualTo(DEFAULT_INDICACAO);
        assertThat(testResultadoExame.getContraindicacao()).isEqualTo(DEFAULT_CONTRAINDICACAO);
    }

    @Test
    @Transactional
    void createResultadoExameWithExistingId() throws Exception {
        // Create the ResultadoExame with an existing ID
        resultadoExame.setId(1L);
        ResultadoExameDTO resultadoExameDTO = resultadoExameMapper.toDto(resultadoExame);

        int databaseSizeBeforeCreate = resultadoExameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultadoExameMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultadoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResultadoExames() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList
        restResultadoExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultadoExame.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].prescricao").value(hasItem(DEFAULT_PRESCRICAO)))
            .andExpect(jsonPath("$.[*].indicacao").value(hasItem(DEFAULT_INDICACAO)))
            .andExpect(jsonPath("$.[*].contraindicacao").value(hasItem(DEFAULT_CONTRAINDICACAO)));
    }

    @Test
    @Transactional
    void getResultadoExame() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get the resultadoExame
        restResultadoExameMockMvc
            .perform(get(ENTITY_API_URL_ID, resultadoExame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resultadoExame.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.prescricao").value(DEFAULT_PRESCRICAO))
            .andExpect(jsonPath("$.indicacao").value(DEFAULT_INDICACAO))
            .andExpect(jsonPath("$.contraindicacao").value(DEFAULT_CONTRAINDICACAO));
    }

    @Test
    @Transactional
    void getResultadoExamesByIdFiltering() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        Long id = resultadoExame.getId();

        defaultResultadoExameShouldBeFound("id.equals=" + id);
        defaultResultadoExameShouldNotBeFound("id.notEquals=" + id);

        defaultResultadoExameShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResultadoExameShouldNotBeFound("id.greaterThan=" + id);

        defaultResultadoExameShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResultadoExameShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where descricao equals to DEFAULT_DESCRICAO
        defaultResultadoExameShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the resultadoExameList where descricao equals to UPDATED_DESCRICAO
        defaultResultadoExameShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where descricao not equals to DEFAULT_DESCRICAO
        defaultResultadoExameShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the resultadoExameList where descricao not equals to UPDATED_DESCRICAO
        defaultResultadoExameShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultResultadoExameShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the resultadoExameList where descricao equals to UPDATED_DESCRICAO
        defaultResultadoExameShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where descricao is not null
        defaultResultadoExameShouldBeFound("descricao.specified=true");

        // Get all the resultadoExameList where descricao is null
        defaultResultadoExameShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllResultadoExamesByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where descricao contains DEFAULT_DESCRICAO
        defaultResultadoExameShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the resultadoExameList where descricao contains UPDATED_DESCRICAO
        defaultResultadoExameShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where descricao does not contain DEFAULT_DESCRICAO
        defaultResultadoExameShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the resultadoExameList where descricao does not contain UPDATED_DESCRICAO
        defaultResultadoExameShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByPrescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where prescricao equals to DEFAULT_PRESCRICAO
        defaultResultadoExameShouldBeFound("prescricao.equals=" + DEFAULT_PRESCRICAO);

        // Get all the resultadoExameList where prescricao equals to UPDATED_PRESCRICAO
        defaultResultadoExameShouldNotBeFound("prescricao.equals=" + UPDATED_PRESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByPrescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where prescricao not equals to DEFAULT_PRESCRICAO
        defaultResultadoExameShouldNotBeFound("prescricao.notEquals=" + DEFAULT_PRESCRICAO);

        // Get all the resultadoExameList where prescricao not equals to UPDATED_PRESCRICAO
        defaultResultadoExameShouldBeFound("prescricao.notEquals=" + UPDATED_PRESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByPrescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where prescricao in DEFAULT_PRESCRICAO or UPDATED_PRESCRICAO
        defaultResultadoExameShouldBeFound("prescricao.in=" + DEFAULT_PRESCRICAO + "," + UPDATED_PRESCRICAO);

        // Get all the resultadoExameList where prescricao equals to UPDATED_PRESCRICAO
        defaultResultadoExameShouldNotBeFound("prescricao.in=" + UPDATED_PRESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByPrescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where prescricao is not null
        defaultResultadoExameShouldBeFound("prescricao.specified=true");

        // Get all the resultadoExameList where prescricao is null
        defaultResultadoExameShouldNotBeFound("prescricao.specified=false");
    }

    @Test
    @Transactional
    void getAllResultadoExamesByPrescricaoContainsSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where prescricao contains DEFAULT_PRESCRICAO
        defaultResultadoExameShouldBeFound("prescricao.contains=" + DEFAULT_PRESCRICAO);

        // Get all the resultadoExameList where prescricao contains UPDATED_PRESCRICAO
        defaultResultadoExameShouldNotBeFound("prescricao.contains=" + UPDATED_PRESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByPrescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where prescricao does not contain DEFAULT_PRESCRICAO
        defaultResultadoExameShouldNotBeFound("prescricao.doesNotContain=" + DEFAULT_PRESCRICAO);

        // Get all the resultadoExameList where prescricao does not contain UPDATED_PRESCRICAO
        defaultResultadoExameShouldBeFound("prescricao.doesNotContain=" + UPDATED_PRESCRICAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByIndicacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where indicacao equals to DEFAULT_INDICACAO
        defaultResultadoExameShouldBeFound("indicacao.equals=" + DEFAULT_INDICACAO);

        // Get all the resultadoExameList where indicacao equals to UPDATED_INDICACAO
        defaultResultadoExameShouldNotBeFound("indicacao.equals=" + UPDATED_INDICACAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByIndicacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where indicacao not equals to DEFAULT_INDICACAO
        defaultResultadoExameShouldNotBeFound("indicacao.notEquals=" + DEFAULT_INDICACAO);

        // Get all the resultadoExameList where indicacao not equals to UPDATED_INDICACAO
        defaultResultadoExameShouldBeFound("indicacao.notEquals=" + UPDATED_INDICACAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByIndicacaoIsInShouldWork() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where indicacao in DEFAULT_INDICACAO or UPDATED_INDICACAO
        defaultResultadoExameShouldBeFound("indicacao.in=" + DEFAULT_INDICACAO + "," + UPDATED_INDICACAO);

        // Get all the resultadoExameList where indicacao equals to UPDATED_INDICACAO
        defaultResultadoExameShouldNotBeFound("indicacao.in=" + UPDATED_INDICACAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByIndicacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where indicacao is not null
        defaultResultadoExameShouldBeFound("indicacao.specified=true");

        // Get all the resultadoExameList where indicacao is null
        defaultResultadoExameShouldNotBeFound("indicacao.specified=false");
    }

    @Test
    @Transactional
    void getAllResultadoExamesByIndicacaoContainsSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where indicacao contains DEFAULT_INDICACAO
        defaultResultadoExameShouldBeFound("indicacao.contains=" + DEFAULT_INDICACAO);

        // Get all the resultadoExameList where indicacao contains UPDATED_INDICACAO
        defaultResultadoExameShouldNotBeFound("indicacao.contains=" + UPDATED_INDICACAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByIndicacaoNotContainsSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where indicacao does not contain DEFAULT_INDICACAO
        defaultResultadoExameShouldNotBeFound("indicacao.doesNotContain=" + DEFAULT_INDICACAO);

        // Get all the resultadoExameList where indicacao does not contain UPDATED_INDICACAO
        defaultResultadoExameShouldBeFound("indicacao.doesNotContain=" + UPDATED_INDICACAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByContraindicacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where contraindicacao equals to DEFAULT_CONTRAINDICACAO
        defaultResultadoExameShouldBeFound("contraindicacao.equals=" + DEFAULT_CONTRAINDICACAO);

        // Get all the resultadoExameList where contraindicacao equals to UPDATED_CONTRAINDICACAO
        defaultResultadoExameShouldNotBeFound("contraindicacao.equals=" + UPDATED_CONTRAINDICACAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByContraindicacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where contraindicacao not equals to DEFAULT_CONTRAINDICACAO
        defaultResultadoExameShouldNotBeFound("contraindicacao.notEquals=" + DEFAULT_CONTRAINDICACAO);

        // Get all the resultadoExameList where contraindicacao not equals to UPDATED_CONTRAINDICACAO
        defaultResultadoExameShouldBeFound("contraindicacao.notEquals=" + UPDATED_CONTRAINDICACAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByContraindicacaoIsInShouldWork() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where contraindicacao in DEFAULT_CONTRAINDICACAO or UPDATED_CONTRAINDICACAO
        defaultResultadoExameShouldBeFound("contraindicacao.in=" + DEFAULT_CONTRAINDICACAO + "," + UPDATED_CONTRAINDICACAO);

        // Get all the resultadoExameList where contraindicacao equals to UPDATED_CONTRAINDICACAO
        defaultResultadoExameShouldNotBeFound("contraindicacao.in=" + UPDATED_CONTRAINDICACAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByContraindicacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where contraindicacao is not null
        defaultResultadoExameShouldBeFound("contraindicacao.specified=true");

        // Get all the resultadoExameList where contraindicacao is null
        defaultResultadoExameShouldNotBeFound("contraindicacao.specified=false");
    }

    @Test
    @Transactional
    void getAllResultadoExamesByContraindicacaoContainsSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where contraindicacao contains DEFAULT_CONTRAINDICACAO
        defaultResultadoExameShouldBeFound("contraindicacao.contains=" + DEFAULT_CONTRAINDICACAO);

        // Get all the resultadoExameList where contraindicacao contains UPDATED_CONTRAINDICACAO
        defaultResultadoExameShouldNotBeFound("contraindicacao.contains=" + UPDATED_CONTRAINDICACAO);
    }

    @Test
    @Transactional
    void getAllResultadoExamesByContraindicacaoNotContainsSomething() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        // Get all the resultadoExameList where contraindicacao does not contain DEFAULT_CONTRAINDICACAO
        defaultResultadoExameShouldNotBeFound("contraindicacao.doesNotContain=" + DEFAULT_CONTRAINDICACAO);

        // Get all the resultadoExameList where contraindicacao does not contain UPDATED_CONTRAINDICACAO
        defaultResultadoExameShouldBeFound("contraindicacao.doesNotContain=" + UPDATED_CONTRAINDICACAO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResultadoExameShouldBeFound(String filter) throws Exception {
        restResultadoExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultadoExame.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].prescricao").value(hasItem(DEFAULT_PRESCRICAO)))
            .andExpect(jsonPath("$.[*].indicacao").value(hasItem(DEFAULT_INDICACAO)))
            .andExpect(jsonPath("$.[*].contraindicacao").value(hasItem(DEFAULT_CONTRAINDICACAO)));

        // Check, that the count call also returns 1
        restResultadoExameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResultadoExameShouldNotBeFound(String filter) throws Exception {
        restResultadoExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResultadoExameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingResultadoExame() throws Exception {
        // Get the resultadoExame
        restResultadoExameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResultadoExame() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        int databaseSizeBeforeUpdate = resultadoExameRepository.findAll().size();

        // Update the resultadoExame
        ResultadoExame updatedResultadoExame = resultadoExameRepository.findById(resultadoExame.getId()).get();
        // Disconnect from session so that the updates on updatedResultadoExame are not directly saved in db
        em.detach(updatedResultadoExame);
        updatedResultadoExame
            .descricao(UPDATED_DESCRICAO)
            .prescricao(UPDATED_PRESCRICAO)
            .indicacao(UPDATED_INDICACAO)
            .contraindicacao(UPDATED_CONTRAINDICACAO);
        ResultadoExameDTO resultadoExameDTO = resultadoExameMapper.toDto(updatedResultadoExame);

        restResultadoExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultadoExameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultadoExameDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeUpdate);
        ResultadoExame testResultadoExame = resultadoExameList.get(resultadoExameList.size() - 1);
        assertThat(testResultadoExame.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testResultadoExame.getPrescricao()).isEqualTo(UPDATED_PRESCRICAO);
        assertThat(testResultadoExame.getIndicacao()).isEqualTo(UPDATED_INDICACAO);
        assertThat(testResultadoExame.getContraindicacao()).isEqualTo(UPDATED_CONTRAINDICACAO);
    }

    @Test
    @Transactional
    void putNonExistingResultadoExame() throws Exception {
        int databaseSizeBeforeUpdate = resultadoExameRepository.findAll().size();
        resultadoExame.setId(count.incrementAndGet());

        // Create the ResultadoExame
        ResultadoExameDTO resultadoExameDTO = resultadoExameMapper.toDto(resultadoExame);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultadoExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultadoExameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultadoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResultadoExame() throws Exception {
        int databaseSizeBeforeUpdate = resultadoExameRepository.findAll().size();
        resultadoExame.setId(count.incrementAndGet());

        // Create the ResultadoExame
        ResultadoExameDTO resultadoExameDTO = resultadoExameMapper.toDto(resultadoExame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultadoExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultadoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResultadoExame() throws Exception {
        int databaseSizeBeforeUpdate = resultadoExameRepository.findAll().size();
        resultadoExame.setId(count.incrementAndGet());

        // Create the ResultadoExame
        ResultadoExameDTO resultadoExameDTO = resultadoExameMapper.toDto(resultadoExame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultadoExameMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultadoExameDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResultadoExameWithPatch() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        int databaseSizeBeforeUpdate = resultadoExameRepository.findAll().size();

        // Update the resultadoExame using partial update
        ResultadoExame partialUpdatedResultadoExame = new ResultadoExame();
        partialUpdatedResultadoExame.setId(resultadoExame.getId());

        partialUpdatedResultadoExame.descricao(UPDATED_DESCRICAO).indicacao(UPDATED_INDICACAO);

        restResultadoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultadoExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultadoExame))
            )
            .andExpect(status().isOk());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeUpdate);
        ResultadoExame testResultadoExame = resultadoExameList.get(resultadoExameList.size() - 1);
        assertThat(testResultadoExame.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testResultadoExame.getPrescricao()).isEqualTo(DEFAULT_PRESCRICAO);
        assertThat(testResultadoExame.getIndicacao()).isEqualTo(UPDATED_INDICACAO);
        assertThat(testResultadoExame.getContraindicacao()).isEqualTo(DEFAULT_CONTRAINDICACAO);
    }

    @Test
    @Transactional
    void fullUpdateResultadoExameWithPatch() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        int databaseSizeBeforeUpdate = resultadoExameRepository.findAll().size();

        // Update the resultadoExame using partial update
        ResultadoExame partialUpdatedResultadoExame = new ResultadoExame();
        partialUpdatedResultadoExame.setId(resultadoExame.getId());

        partialUpdatedResultadoExame
            .descricao(UPDATED_DESCRICAO)
            .prescricao(UPDATED_PRESCRICAO)
            .indicacao(UPDATED_INDICACAO)
            .contraindicacao(UPDATED_CONTRAINDICACAO);

        restResultadoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultadoExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultadoExame))
            )
            .andExpect(status().isOk());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeUpdate);
        ResultadoExame testResultadoExame = resultadoExameList.get(resultadoExameList.size() - 1);
        assertThat(testResultadoExame.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testResultadoExame.getPrescricao()).isEqualTo(UPDATED_PRESCRICAO);
        assertThat(testResultadoExame.getIndicacao()).isEqualTo(UPDATED_INDICACAO);
        assertThat(testResultadoExame.getContraindicacao()).isEqualTo(UPDATED_CONTRAINDICACAO);
    }

    @Test
    @Transactional
    void patchNonExistingResultadoExame() throws Exception {
        int databaseSizeBeforeUpdate = resultadoExameRepository.findAll().size();
        resultadoExame.setId(count.incrementAndGet());

        // Create the ResultadoExame
        ResultadoExameDTO resultadoExameDTO = resultadoExameMapper.toDto(resultadoExame);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultadoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resultadoExameDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultadoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResultadoExame() throws Exception {
        int databaseSizeBeforeUpdate = resultadoExameRepository.findAll().size();
        resultadoExame.setId(count.incrementAndGet());

        // Create the ResultadoExame
        ResultadoExameDTO resultadoExameDTO = resultadoExameMapper.toDto(resultadoExame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultadoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultadoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResultadoExame() throws Exception {
        int databaseSizeBeforeUpdate = resultadoExameRepository.findAll().size();
        resultadoExame.setId(count.incrementAndGet());

        // Create the ResultadoExame
        ResultadoExameDTO resultadoExameDTO = resultadoExameMapper.toDto(resultadoExame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultadoExameMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultadoExameDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultadoExame in the database
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResultadoExame() throws Exception {
        // Initialize the database
        resultadoExameRepository.saveAndFlush(resultadoExame);

        int databaseSizeBeforeDelete = resultadoExameRepository.findAll().size();

        // Delete the resultadoExame
        restResultadoExameMockMvc
            .perform(delete(ENTITY_API_URL_ID, resultadoExame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResultadoExame> resultadoExameList = resultadoExameRepository.findAll();
        assertThat(resultadoExameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
