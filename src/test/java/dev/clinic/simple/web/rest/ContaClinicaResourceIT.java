package dev.clinic.simple.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.clinic.simple.IntegrationTest;
import dev.clinic.simple.domain.ContaClinica;
import dev.clinic.simple.domain.Exame;
import dev.clinic.simple.domain.Paciente;
import dev.clinic.simple.domain.TipoConvenio;
import dev.clinic.simple.repository.ContaClinicaRepository;
import dev.clinic.simple.service.criteria.ContaClinicaCriteria;
import dev.clinic.simple.service.dto.ContaClinicaDTO;
import dev.clinic.simple.service.mapper.ContaClinicaMapper;
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
 * Integration tests for the {@link ContaClinicaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContaClinicaResourceIT {

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_SENHA = "AAAAAAAAAA";
    private static final String UPDATED_SENHA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conta-clinicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContaClinicaRepository contaClinicaRepository;

    @Autowired
    private ContaClinicaMapper contaClinicaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContaClinicaMockMvc;

    private ContaClinica contaClinica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaClinica createEntity(EntityManager em) {
        ContaClinica contaClinica = new ContaClinica().cpf(DEFAULT_CPF).senha(DEFAULT_SENHA);
        return contaClinica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaClinica createUpdatedEntity(EntityManager em) {
        ContaClinica contaClinica = new ContaClinica().cpf(UPDATED_CPF).senha(UPDATED_SENHA);
        return contaClinica;
    }

    @BeforeEach
    public void initTest() {
        contaClinica = createEntity(em);
    }

    @Test
    @Transactional
    void createContaClinica() throws Exception {
        int databaseSizeBeforeCreate = contaClinicaRepository.findAll().size();
        // Create the ContaClinica
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(contaClinica);
        restContaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeCreate + 1);
        ContaClinica testContaClinica = contaClinicaList.get(contaClinicaList.size() - 1);
        assertThat(testContaClinica.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testContaClinica.getSenha()).isEqualTo(DEFAULT_SENHA);
    }

    @Test
    @Transactional
    void createContaClinicaWithExistingId() throws Exception {
        // Create the ContaClinica with an existing ID
        contaClinica.setId(1L);
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(contaClinica);

        int databaseSizeBeforeCreate = contaClinicaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaClinicaRepository.findAll().size();
        // set the field null
        contaClinica.setCpf(null);

        // Create the ContaClinica, which fails.
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(contaClinica);

        restContaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContaClinicas() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList
        restContaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaClinica.getId().intValue())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA)));
    }

    @Test
    @Transactional
    void getContaClinica() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get the contaClinica
        restContaClinicaMockMvc
            .perform(get(ENTITY_API_URL_ID, contaClinica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contaClinica.getId().intValue()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.senha").value(DEFAULT_SENHA));
    }

    @Test
    @Transactional
    void getContaClinicasByIdFiltering() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        Long id = contaClinica.getId();

        defaultContaClinicaShouldBeFound("id.equals=" + id);
        defaultContaClinicaShouldNotBeFound("id.notEquals=" + id);

        defaultContaClinicaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContaClinicaShouldNotBeFound("id.greaterThan=" + id);

        defaultContaClinicaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContaClinicaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where cpf equals to DEFAULT_CPF
        defaultContaClinicaShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the contaClinicaList where cpf equals to UPDATED_CPF
        defaultContaClinicaShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where cpf not equals to DEFAULT_CPF
        defaultContaClinicaShouldNotBeFound("cpf.notEquals=" + DEFAULT_CPF);

        // Get all the contaClinicaList where cpf not equals to UPDATED_CPF
        defaultContaClinicaShouldBeFound("cpf.notEquals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultContaClinicaShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the contaClinicaList where cpf equals to UPDATED_CPF
        defaultContaClinicaShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where cpf is not null
        defaultContaClinicaShouldBeFound("cpf.specified=true");

        // Get all the contaClinicaList where cpf is null
        defaultContaClinicaShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfContainsSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where cpf contains DEFAULT_CPF
        defaultContaClinicaShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the contaClinicaList where cpf contains UPDATED_CPF
        defaultContaClinicaShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where cpf does not contain DEFAULT_CPF
        defaultContaClinicaShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the contaClinicaList where cpf does not contain UPDATED_CPF
        defaultContaClinicaShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllContaClinicasBySenhaIsEqualToSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where senha equals to DEFAULT_SENHA
        defaultContaClinicaShouldBeFound("senha.equals=" + DEFAULT_SENHA);

        // Get all the contaClinicaList where senha equals to UPDATED_SENHA
        defaultContaClinicaShouldNotBeFound("senha.equals=" + UPDATED_SENHA);
    }

    @Test
    @Transactional
    void getAllContaClinicasBySenhaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where senha not equals to DEFAULT_SENHA
        defaultContaClinicaShouldNotBeFound("senha.notEquals=" + DEFAULT_SENHA);

        // Get all the contaClinicaList where senha not equals to UPDATED_SENHA
        defaultContaClinicaShouldBeFound("senha.notEquals=" + UPDATED_SENHA);
    }

    @Test
    @Transactional
    void getAllContaClinicasBySenhaIsInShouldWork() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where senha in DEFAULT_SENHA or UPDATED_SENHA
        defaultContaClinicaShouldBeFound("senha.in=" + DEFAULT_SENHA + "," + UPDATED_SENHA);

        // Get all the contaClinicaList where senha equals to UPDATED_SENHA
        defaultContaClinicaShouldNotBeFound("senha.in=" + UPDATED_SENHA);
    }

    @Test
    @Transactional
    void getAllContaClinicasBySenhaIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where senha is not null
        defaultContaClinicaShouldBeFound("senha.specified=true");

        // Get all the contaClinicaList where senha is null
        defaultContaClinicaShouldNotBeFound("senha.specified=false");
    }

    @Test
    @Transactional
    void getAllContaClinicasBySenhaContainsSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where senha contains DEFAULT_SENHA
        defaultContaClinicaShouldBeFound("senha.contains=" + DEFAULT_SENHA);

        // Get all the contaClinicaList where senha contains UPDATED_SENHA
        defaultContaClinicaShouldNotBeFound("senha.contains=" + UPDATED_SENHA);
    }

    @Test
    @Transactional
    void getAllContaClinicasBySenhaNotContainsSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        // Get all the contaClinicaList where senha does not contain DEFAULT_SENHA
        defaultContaClinicaShouldNotBeFound("senha.doesNotContain=" + DEFAULT_SENHA);

        // Get all the contaClinicaList where senha does not contain UPDATED_SENHA
        defaultContaClinicaShouldBeFound("senha.doesNotContain=" + UPDATED_SENHA);
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);
        Paciente cpf = PacienteResourceIT.createEntity(em);
        em.persist(cpf);
        em.flush();
        contaClinica.setCpf(cpf);
        contaClinicaRepository.saveAndFlush(contaClinica);
        Long cpfId = cpf.getId();

        // Get all the contaClinicaList where cpf equals to cpfId
        defaultContaClinicaShouldBeFound("cpfId.equals=" + cpfId);

        // Get all the contaClinicaList where cpf equals to (cpfId + 1)
        defaultContaClinicaShouldNotBeFound("cpfId.equals=" + (cpfId + 1));
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);
        TipoConvenio cpf = TipoConvenioResourceIT.createEntity(em);
        em.persist(cpf);
        em.flush();
        contaClinica.addCpf(cpf);
        contaClinicaRepository.saveAndFlush(contaClinica);
        Long cpfId = cpf.getId();

        // Get all the contaClinicaList where cpf equals to cpfId
        defaultContaClinicaShouldBeFound("cpfId.equals=" + cpfId);

        // Get all the contaClinicaList where cpf equals to (cpfId + 1)
        defaultContaClinicaShouldNotBeFound("cpfId.equals=" + (cpfId + 1));
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);
        Exame cpf = ExameResourceIT.createEntity(em);
        em.persist(cpf);
        em.flush();
        contaClinica.addCpf(cpf);
        contaClinicaRepository.saveAndFlush(contaClinica);
        Long cpfId = cpf.getId();

        // Get all the contaClinicaList where cpf equals to cpfId
        defaultContaClinicaShouldBeFound("cpfId.equals=" + cpfId);

        // Get all the contaClinicaList where cpf equals to (cpfId + 1)
        defaultContaClinicaShouldNotBeFound("cpfId.equals=" + (cpfId + 1));
    }

    @Test
    @Transactional
    void getAllContaClinicasByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);
        Paciente cpf = PacienteResourceIT.createEntity(em);
        em.persist(cpf);
        em.flush();
        contaClinica.setCpf(cpf);
        cpf.setCpf(contaClinica);
        contaClinicaRepository.saveAndFlush(contaClinica);
        Long cpfId = cpf.getId();

        // Get all the contaClinicaList where cpf equals to cpfId
        defaultContaClinicaShouldBeFound("cpfId.equals=" + cpfId);

        // Get all the contaClinicaList where cpf equals to (cpfId + 1)
        defaultContaClinicaShouldNotBeFound("cpfId.equals=" + (cpfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContaClinicaShouldBeFound(String filter) throws Exception {
        restContaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaClinica.getId().intValue())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA)));

        // Check, that the count call also returns 1
        restContaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContaClinicaShouldNotBeFound(String filter) throws Exception {
        restContaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContaClinica() throws Exception {
        // Get the contaClinica
        restContaClinicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContaClinica() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        int databaseSizeBeforeUpdate = contaClinicaRepository.findAll().size();

        // Update the contaClinica
        ContaClinica updatedContaClinica = contaClinicaRepository.findById(contaClinica.getId()).get();
        // Disconnect from session so that the updates on updatedContaClinica are not directly saved in db
        em.detach(updatedContaClinica);
        updatedContaClinica.cpf(UPDATED_CPF).senha(UPDATED_SENHA);
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(updatedContaClinica);

        restContaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaClinicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeUpdate);
        ContaClinica testContaClinica = contaClinicaList.get(contaClinicaList.size() - 1);
        assertThat(testContaClinica.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testContaClinica.getSenha()).isEqualTo(UPDATED_SENHA);
    }

    @Test
    @Transactional
    void putNonExistingContaClinica() throws Exception {
        int databaseSizeBeforeUpdate = contaClinicaRepository.findAll().size();
        contaClinica.setId(count.incrementAndGet());

        // Create the ContaClinica
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(contaClinica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaClinicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContaClinica() throws Exception {
        int databaseSizeBeforeUpdate = contaClinicaRepository.findAll().size();
        contaClinica.setId(count.incrementAndGet());

        // Create the ContaClinica
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(contaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContaClinica() throws Exception {
        int databaseSizeBeforeUpdate = contaClinicaRepository.findAll().size();
        contaClinica.setId(count.incrementAndGet());

        // Create the ContaClinica
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(contaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContaClinicaWithPatch() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        int databaseSizeBeforeUpdate = contaClinicaRepository.findAll().size();

        // Update the contaClinica using partial update
        ContaClinica partialUpdatedContaClinica = new ContaClinica();
        partialUpdatedContaClinica.setId(contaClinica.getId());

        restContaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaClinica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaClinica))
            )
            .andExpect(status().isOk());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeUpdate);
        ContaClinica testContaClinica = contaClinicaList.get(contaClinicaList.size() - 1);
        assertThat(testContaClinica.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testContaClinica.getSenha()).isEqualTo(DEFAULT_SENHA);
    }

    @Test
    @Transactional
    void fullUpdateContaClinicaWithPatch() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        int databaseSizeBeforeUpdate = contaClinicaRepository.findAll().size();

        // Update the contaClinica using partial update
        ContaClinica partialUpdatedContaClinica = new ContaClinica();
        partialUpdatedContaClinica.setId(contaClinica.getId());

        partialUpdatedContaClinica.cpf(UPDATED_CPF).senha(UPDATED_SENHA);

        restContaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaClinica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaClinica))
            )
            .andExpect(status().isOk());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeUpdate);
        ContaClinica testContaClinica = contaClinicaList.get(contaClinicaList.size() - 1);
        assertThat(testContaClinica.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testContaClinica.getSenha()).isEqualTo(UPDATED_SENHA);
    }

    @Test
    @Transactional
    void patchNonExistingContaClinica() throws Exception {
        int databaseSizeBeforeUpdate = contaClinicaRepository.findAll().size();
        contaClinica.setId(count.incrementAndGet());

        // Create the ContaClinica
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(contaClinica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contaClinicaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContaClinica() throws Exception {
        int databaseSizeBeforeUpdate = contaClinicaRepository.findAll().size();
        contaClinica.setId(count.incrementAndGet());

        // Create the ContaClinica
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(contaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContaClinica() throws Exception {
        int databaseSizeBeforeUpdate = contaClinicaRepository.findAll().size();
        contaClinica.setId(count.incrementAndGet());

        // Create the ContaClinica
        ContaClinicaDTO contaClinicaDTO = contaClinicaMapper.toDto(contaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaClinicaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaClinica in the database
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContaClinica() throws Exception {
        // Initialize the database
        contaClinicaRepository.saveAndFlush(contaClinica);

        int databaseSizeBeforeDelete = contaClinicaRepository.findAll().size();

        // Delete the contaClinica
        restContaClinicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, contaClinica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContaClinica> contaClinicaList = contaClinicaRepository.findAll();
        assertThat(contaClinicaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
