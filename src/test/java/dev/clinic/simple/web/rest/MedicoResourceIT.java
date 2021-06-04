package dev.clinic.simple.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.clinic.simple.IntegrationTest;
import dev.clinic.simple.domain.Exame;
import dev.clinic.simple.domain.Medico;
import dev.clinic.simple.repository.MedicoRepository;
import dev.clinic.simple.service.criteria.MedicoCriteria;
import dev.clinic.simple.service.dto.MedicoDTO;
import dev.clinic.simple.service.mapper.MedicoMapper;
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
 * Integration tests for the {@link MedicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicoResourceIT {

    private static final Integer DEFAULT_CRM = 1;
    private static final Integer UPDATED_CRM = 2;
    private static final Integer SMALLER_CRM = 1 - 1;

    private static final Integer DEFAULT_NOME = 1;
    private static final Integer UPDATED_NOME = 2;
    private static final Integer SMALLER_NOME = 1 - 1;

    private static final String DEFAULT_GRADUACAO = "AAAAAAAAAA";
    private static final String UPDATED_GRADUACAO = "BBBBBBBBBB";

    private static final String DEFAULT_ATUACAO = "AAAAAAAAAA";
    private static final String UPDATED_ATUACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private MedicoMapper medicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicoMockMvc;

    private Medico medico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createEntity(EntityManager em) {
        Medico medico = new Medico().crm(DEFAULT_CRM).nome(DEFAULT_NOME).graduacao(DEFAULT_GRADUACAO).atuacao(DEFAULT_ATUACAO);
        return medico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createUpdatedEntity(EntityManager em) {
        Medico medico = new Medico().crm(UPDATED_CRM).nome(UPDATED_NOME).graduacao(UPDATED_GRADUACAO).atuacao(UPDATED_ATUACAO);
        return medico;
    }

    @BeforeEach
    public void initTest() {
        medico = createEntity(em);
    }

    @Test
    @Transactional
    void createMedico() throws Exception {
        int databaseSizeBeforeCreate = medicoRepository.findAll().size();
        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);
        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicoDTO)))
            .andExpect(status().isCreated());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeCreate + 1);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getCrm()).isEqualTo(DEFAULT_CRM);
        assertThat(testMedico.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMedico.getGraduacao()).isEqualTo(DEFAULT_GRADUACAO);
        assertThat(testMedico.getAtuacao()).isEqualTo(DEFAULT_ATUACAO);
    }

    @Test
    @Transactional
    void createMedicoWithExistingId() throws Exception {
        // Create the Medico with an existing ID
        medico.setId(1L);
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        int databaseSizeBeforeCreate = medicoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMedicos() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medico.getId().intValue())))
            .andExpect(jsonPath("$.[*].crm").value(hasItem(DEFAULT_CRM)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].graduacao").value(hasItem(DEFAULT_GRADUACAO)))
            .andExpect(jsonPath("$.[*].atuacao").value(hasItem(DEFAULT_ATUACAO)));
    }

    @Test
    @Transactional
    void getMedico() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get the medico
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL_ID, medico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medico.getId().intValue()))
            .andExpect(jsonPath("$.crm").value(DEFAULT_CRM))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.graduacao").value(DEFAULT_GRADUACAO))
            .andExpect(jsonPath("$.atuacao").value(DEFAULT_ATUACAO));
    }

    @Test
    @Transactional
    void getMedicosByIdFiltering() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        Long id = medico.getId();

        defaultMedicoShouldBeFound("id.equals=" + id);
        defaultMedicoShouldNotBeFound("id.notEquals=" + id);

        defaultMedicoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMedicoShouldNotBeFound("id.greaterThan=" + id);

        defaultMedicoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMedicoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicosByCrmIsEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where crm equals to DEFAULT_CRM
        defaultMedicoShouldBeFound("crm.equals=" + DEFAULT_CRM);

        // Get all the medicoList where crm equals to UPDATED_CRM
        defaultMedicoShouldNotBeFound("crm.equals=" + UPDATED_CRM);
    }

    @Test
    @Transactional
    void getAllMedicosByCrmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where crm not equals to DEFAULT_CRM
        defaultMedicoShouldNotBeFound("crm.notEquals=" + DEFAULT_CRM);

        // Get all the medicoList where crm not equals to UPDATED_CRM
        defaultMedicoShouldBeFound("crm.notEquals=" + UPDATED_CRM);
    }

    @Test
    @Transactional
    void getAllMedicosByCrmIsInShouldWork() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where crm in DEFAULT_CRM or UPDATED_CRM
        defaultMedicoShouldBeFound("crm.in=" + DEFAULT_CRM + "," + UPDATED_CRM);

        // Get all the medicoList where crm equals to UPDATED_CRM
        defaultMedicoShouldNotBeFound("crm.in=" + UPDATED_CRM);
    }

    @Test
    @Transactional
    void getAllMedicosByCrmIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where crm is not null
        defaultMedicoShouldBeFound("crm.specified=true");

        // Get all the medicoList where crm is null
        defaultMedicoShouldNotBeFound("crm.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByCrmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where crm is greater than or equal to DEFAULT_CRM
        defaultMedicoShouldBeFound("crm.greaterThanOrEqual=" + DEFAULT_CRM);

        // Get all the medicoList where crm is greater than or equal to UPDATED_CRM
        defaultMedicoShouldNotBeFound("crm.greaterThanOrEqual=" + UPDATED_CRM);
    }

    @Test
    @Transactional
    void getAllMedicosByCrmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where crm is less than or equal to DEFAULT_CRM
        defaultMedicoShouldBeFound("crm.lessThanOrEqual=" + DEFAULT_CRM);

        // Get all the medicoList where crm is less than or equal to SMALLER_CRM
        defaultMedicoShouldNotBeFound("crm.lessThanOrEqual=" + SMALLER_CRM);
    }

    @Test
    @Transactional
    void getAllMedicosByCrmIsLessThanSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where crm is less than DEFAULT_CRM
        defaultMedicoShouldNotBeFound("crm.lessThan=" + DEFAULT_CRM);

        // Get all the medicoList where crm is less than UPDATED_CRM
        defaultMedicoShouldBeFound("crm.lessThan=" + UPDATED_CRM);
    }

    @Test
    @Transactional
    void getAllMedicosByCrmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where crm is greater than DEFAULT_CRM
        defaultMedicoShouldNotBeFound("crm.greaterThan=" + DEFAULT_CRM);

        // Get all the medicoList where crm is greater than SMALLER_CRM
        defaultMedicoShouldBeFound("crm.greaterThan=" + SMALLER_CRM);
    }

    @Test
    @Transactional
    void getAllMedicosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where nome equals to DEFAULT_NOME
        defaultMedicoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the medicoList where nome equals to UPDATED_NOME
        defaultMedicoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllMedicosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where nome not equals to DEFAULT_NOME
        defaultMedicoShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the medicoList where nome not equals to UPDATED_NOME
        defaultMedicoShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllMedicosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultMedicoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the medicoList where nome equals to UPDATED_NOME
        defaultMedicoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllMedicosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where nome is not null
        defaultMedicoShouldBeFound("nome.specified=true");

        // Get all the medicoList where nome is null
        defaultMedicoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByNomeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where nome is greater than or equal to DEFAULT_NOME
        defaultMedicoShouldBeFound("nome.greaterThanOrEqual=" + DEFAULT_NOME);

        // Get all the medicoList where nome is greater than or equal to UPDATED_NOME
        defaultMedicoShouldNotBeFound("nome.greaterThanOrEqual=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllMedicosByNomeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where nome is less than or equal to DEFAULT_NOME
        defaultMedicoShouldBeFound("nome.lessThanOrEqual=" + DEFAULT_NOME);

        // Get all the medicoList where nome is less than or equal to SMALLER_NOME
        defaultMedicoShouldNotBeFound("nome.lessThanOrEqual=" + SMALLER_NOME);
    }

    @Test
    @Transactional
    void getAllMedicosByNomeIsLessThanSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where nome is less than DEFAULT_NOME
        defaultMedicoShouldNotBeFound("nome.lessThan=" + DEFAULT_NOME);

        // Get all the medicoList where nome is less than UPDATED_NOME
        defaultMedicoShouldBeFound("nome.lessThan=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllMedicosByNomeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where nome is greater than DEFAULT_NOME
        defaultMedicoShouldNotBeFound("nome.greaterThan=" + DEFAULT_NOME);

        // Get all the medicoList where nome is greater than SMALLER_NOME
        defaultMedicoShouldBeFound("nome.greaterThan=" + SMALLER_NOME);
    }

    @Test
    @Transactional
    void getAllMedicosByGraduacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where graduacao equals to DEFAULT_GRADUACAO
        defaultMedicoShouldBeFound("graduacao.equals=" + DEFAULT_GRADUACAO);

        // Get all the medicoList where graduacao equals to UPDATED_GRADUACAO
        defaultMedicoShouldNotBeFound("graduacao.equals=" + UPDATED_GRADUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByGraduacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where graduacao not equals to DEFAULT_GRADUACAO
        defaultMedicoShouldNotBeFound("graduacao.notEquals=" + DEFAULT_GRADUACAO);

        // Get all the medicoList where graduacao not equals to UPDATED_GRADUACAO
        defaultMedicoShouldBeFound("graduacao.notEquals=" + UPDATED_GRADUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByGraduacaoIsInShouldWork() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where graduacao in DEFAULT_GRADUACAO or UPDATED_GRADUACAO
        defaultMedicoShouldBeFound("graduacao.in=" + DEFAULT_GRADUACAO + "," + UPDATED_GRADUACAO);

        // Get all the medicoList where graduacao equals to UPDATED_GRADUACAO
        defaultMedicoShouldNotBeFound("graduacao.in=" + UPDATED_GRADUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByGraduacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where graduacao is not null
        defaultMedicoShouldBeFound("graduacao.specified=true");

        // Get all the medicoList where graduacao is null
        defaultMedicoShouldNotBeFound("graduacao.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByGraduacaoContainsSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where graduacao contains DEFAULT_GRADUACAO
        defaultMedicoShouldBeFound("graduacao.contains=" + DEFAULT_GRADUACAO);

        // Get all the medicoList where graduacao contains UPDATED_GRADUACAO
        defaultMedicoShouldNotBeFound("graduacao.contains=" + UPDATED_GRADUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByGraduacaoNotContainsSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where graduacao does not contain DEFAULT_GRADUACAO
        defaultMedicoShouldNotBeFound("graduacao.doesNotContain=" + DEFAULT_GRADUACAO);

        // Get all the medicoList where graduacao does not contain UPDATED_GRADUACAO
        defaultMedicoShouldBeFound("graduacao.doesNotContain=" + UPDATED_GRADUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByAtuacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where atuacao equals to DEFAULT_ATUACAO
        defaultMedicoShouldBeFound("atuacao.equals=" + DEFAULT_ATUACAO);

        // Get all the medicoList where atuacao equals to UPDATED_ATUACAO
        defaultMedicoShouldNotBeFound("atuacao.equals=" + UPDATED_ATUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByAtuacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where atuacao not equals to DEFAULT_ATUACAO
        defaultMedicoShouldNotBeFound("atuacao.notEquals=" + DEFAULT_ATUACAO);

        // Get all the medicoList where atuacao not equals to UPDATED_ATUACAO
        defaultMedicoShouldBeFound("atuacao.notEquals=" + UPDATED_ATUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByAtuacaoIsInShouldWork() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where atuacao in DEFAULT_ATUACAO or UPDATED_ATUACAO
        defaultMedicoShouldBeFound("atuacao.in=" + DEFAULT_ATUACAO + "," + UPDATED_ATUACAO);

        // Get all the medicoList where atuacao equals to UPDATED_ATUACAO
        defaultMedicoShouldNotBeFound("atuacao.in=" + UPDATED_ATUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByAtuacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where atuacao is not null
        defaultMedicoShouldBeFound("atuacao.specified=true");

        // Get all the medicoList where atuacao is null
        defaultMedicoShouldNotBeFound("atuacao.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByAtuacaoContainsSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where atuacao contains DEFAULT_ATUACAO
        defaultMedicoShouldBeFound("atuacao.contains=" + DEFAULT_ATUACAO);

        // Get all the medicoList where atuacao contains UPDATED_ATUACAO
        defaultMedicoShouldNotBeFound("atuacao.contains=" + UPDATED_ATUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByAtuacaoNotContainsSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where atuacao does not contain DEFAULT_ATUACAO
        defaultMedicoShouldNotBeFound("atuacao.doesNotContain=" + DEFAULT_ATUACAO);

        // Get all the medicoList where atuacao does not contain UPDATED_ATUACAO
        defaultMedicoShouldBeFound("atuacao.doesNotContain=" + UPDATED_ATUACAO);
    }

    @Test
    @Transactional
    void getAllMedicosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);
        Exame nome = ExameResourceIT.createEntity(em);
        em.persist(nome);
        em.flush();
        medico.setNome(nome);
        nome.setNomemedico(medico);
        medicoRepository.saveAndFlush(medico);
        Long nomeId = nome.getId();

        // Get all the medicoList where nome equals to nomeId
        defaultMedicoShouldBeFound("nomeId.equals=" + nomeId);

        // Get all the medicoList where nome equals to (nomeId + 1)
        defaultMedicoShouldNotBeFound("nomeId.equals=" + (nomeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicoShouldBeFound(String filter) throws Exception {
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medico.getId().intValue())))
            .andExpect(jsonPath("$.[*].crm").value(hasItem(DEFAULT_CRM)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].graduacao").value(hasItem(DEFAULT_GRADUACAO)))
            .andExpect(jsonPath("$.[*].atuacao").value(hasItem(DEFAULT_ATUACAO)));

        // Check, that the count call also returns 1
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicoShouldNotBeFound(String filter) throws Exception {
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedico() throws Exception {
        // Get the medico
        restMedicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedico() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();

        // Update the medico
        Medico updatedMedico = medicoRepository.findById(medico.getId()).get();
        // Disconnect from session so that the updates on updatedMedico are not directly saved in db
        em.detach(updatedMedico);
        updatedMedico.crm(UPDATED_CRM).nome(UPDATED_NOME).graduacao(UPDATED_GRADUACAO).atuacao(UPDATED_ATUACAO);
        MedicoDTO medicoDTO = medicoMapper.toDto(updatedMedico);

        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getCrm()).isEqualTo(UPDATED_CRM);
        assertThat(testMedico.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMedico.getGraduacao()).isEqualTo(UPDATED_GRADUACAO);
        assertThat(testMedico.getAtuacao()).isEqualTo(UPDATED_ATUACAO);
    }

    @Test
    @Transactional
    void putNonExistingMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico.graduacao(UPDATED_GRADUACAO);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getCrm()).isEqualTo(DEFAULT_CRM);
        assertThat(testMedico.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMedico.getGraduacao()).isEqualTo(UPDATED_GRADUACAO);
        assertThat(testMedico.getAtuacao()).isEqualTo(DEFAULT_ATUACAO);
    }

    @Test
    @Transactional
    void fullUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico.crm(UPDATED_CRM).nome(UPDATED_NOME).graduacao(UPDATED_GRADUACAO).atuacao(UPDATED_ATUACAO);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getCrm()).isEqualTo(UPDATED_CRM);
        assertThat(testMedico.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMedico.getGraduacao()).isEqualTo(UPDATED_GRADUACAO);
        assertThat(testMedico.getAtuacao()).isEqualTo(UPDATED_ATUACAO);
    }

    @Test
    @Transactional
    void patchNonExistingMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medicoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedico() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeDelete = medicoRepository.findAll().size();

        // Delete the medico
        restMedicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, medico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
