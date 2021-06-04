package dev.clinic.simple.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.clinic.simple.IntegrationTest;
import dev.clinic.simple.domain.ContaClinica;
import dev.clinic.simple.domain.ContatoPaciente;
import dev.clinic.simple.domain.EnderecoPaciente;
import dev.clinic.simple.domain.Paciente;
import dev.clinic.simple.repository.PacienteRepository;
import dev.clinic.simple.service.criteria.PacienteCriteria;
import dev.clinic.simple.service.dto.PacienteDTO;
import dev.clinic.simple.service.mapper.PacienteMapper;
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
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PacienteResourceIT {

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDADE = "AAAAAAAAAA";
    private static final String UPDATED_IDADE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity(EntityManager em) {
        Paciente paciente = new Paciente().cpf(DEFAULT_CPF).nome(DEFAULT_NOME).idade(DEFAULT_IDADE);
        return paciente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity(EntityManager em) {
        Paciente paciente = new Paciente().cpf(UPDATED_CPF).nome(UPDATED_NOME).idade(UPDATED_IDADE);
        return paciente;
    }

    @BeforeEach
    public void initTest() {
        paciente = createEntity(em);
    }

    @Test
    @Transactional
    void createPaciente() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();
        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);
        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isCreated());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate + 1);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPaciente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPaciente.getIdade()).isEqualTo(DEFAULT_IDADE);
    }

    @Test
    @Transactional
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPacientes() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].idade").value(hasItem(DEFAULT_IDADE)));
    }

    @Test
    @Transactional
    void getPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL_ID, paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.idade").value(DEFAULT_IDADE));
    }

    @Test
    @Transactional
    void getPacientesByIdFiltering() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        Long id = paciente.getId();

        defaultPacienteShouldBeFound("id.equals=" + id);
        defaultPacienteShouldNotBeFound("id.notEquals=" + id);

        defaultPacienteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPacienteShouldNotBeFound("id.greaterThan=" + id);

        defaultPacienteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPacienteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPacientesByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpf equals to DEFAULT_CPF
        defaultPacienteShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the pacienteList where cpf equals to UPDATED_CPF
        defaultPacienteShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPacientesByCpfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpf not equals to DEFAULT_CPF
        defaultPacienteShouldNotBeFound("cpf.notEquals=" + DEFAULT_CPF);

        // Get all the pacienteList where cpf not equals to UPDATED_CPF
        defaultPacienteShouldBeFound("cpf.notEquals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPacientesByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultPacienteShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the pacienteList where cpf equals to UPDATED_CPF
        defaultPacienteShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPacientesByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpf is not null
        defaultPacienteShouldBeFound("cpf.specified=true");

        // Get all the pacienteList where cpf is null
        defaultPacienteShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByCpfContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpf contains DEFAULT_CPF
        defaultPacienteShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the pacienteList where cpf contains UPDATED_CPF
        defaultPacienteShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPacientesByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where cpf does not contain DEFAULT_CPF
        defaultPacienteShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the pacienteList where cpf does not contain UPDATED_CPF
        defaultPacienteShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPacientesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome equals to DEFAULT_NOME
        defaultPacienteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the pacienteList where nome equals to UPDATED_NOME
        defaultPacienteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPacientesByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome not equals to DEFAULT_NOME
        defaultPacienteShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the pacienteList where nome not equals to UPDATED_NOME
        defaultPacienteShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPacientesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultPacienteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the pacienteList where nome equals to UPDATED_NOME
        defaultPacienteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPacientesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome is not null
        defaultPacienteShouldBeFound("nome.specified=true");

        // Get all the pacienteList where nome is null
        defaultPacienteShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByNomeContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome contains DEFAULT_NOME
        defaultPacienteShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the pacienteList where nome contains UPDATED_NOME
        defaultPacienteShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPacientesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nome does not contain DEFAULT_NOME
        defaultPacienteShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the pacienteList where nome does not contain UPDATED_NOME
        defaultPacienteShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idade equals to DEFAULT_IDADE
        defaultPacienteShouldBeFound("idade.equals=" + DEFAULT_IDADE);

        // Get all the pacienteList where idade equals to UPDATED_IDADE
        defaultPacienteShouldNotBeFound("idade.equals=" + UPDATED_IDADE);
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idade not equals to DEFAULT_IDADE
        defaultPacienteShouldNotBeFound("idade.notEquals=" + DEFAULT_IDADE);

        // Get all the pacienteList where idade not equals to UPDATED_IDADE
        defaultPacienteShouldBeFound("idade.notEquals=" + UPDATED_IDADE);
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idade in DEFAULT_IDADE or UPDATED_IDADE
        defaultPacienteShouldBeFound("idade.in=" + DEFAULT_IDADE + "," + UPDATED_IDADE);

        // Get all the pacienteList where idade equals to UPDATED_IDADE
        defaultPacienteShouldNotBeFound("idade.in=" + UPDATED_IDADE);
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idade is not null
        defaultPacienteShouldBeFound("idade.specified=true");

        // Get all the pacienteList where idade is null
        defaultPacienteShouldNotBeFound("idade.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idade contains DEFAULT_IDADE
        defaultPacienteShouldBeFound("idade.contains=" + DEFAULT_IDADE);

        // Get all the pacienteList where idade contains UPDATED_IDADE
        defaultPacienteShouldNotBeFound("idade.contains=" + UPDATED_IDADE);
    }

    @Test
    @Transactional
    void getAllPacientesByIdadeNotContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where idade does not contain DEFAULT_IDADE
        defaultPacienteShouldNotBeFound("idade.doesNotContain=" + DEFAULT_IDADE);

        // Get all the pacienteList where idade does not contain UPDATED_IDADE
        defaultPacienteShouldBeFound("idade.doesNotContain=" + UPDATED_IDADE);
    }

    @Test
    @Transactional
    void getAllPacientesByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        ContaClinica cpf = ContaClinicaResourceIT.createEntity(em);
        em.persist(cpf);
        em.flush();
        paciente.setCpf(cpf);
        pacienteRepository.saveAndFlush(paciente);
        Long cpfId = cpf.getId();

        // Get all the pacienteList where cpf equals to cpfId
        defaultPacienteShouldBeFound("cpfId.equals=" + cpfId);

        // Get all the pacienteList where cpf equals to (cpfId + 1)
        defaultPacienteShouldNotBeFound("cpfId.equals=" + (cpfId + 1));
    }

    @Test
    @Transactional
    void getAllPacientesByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        EnderecoPaciente cpf = EnderecoPacienteResourceIT.createEntity(em);
        em.persist(cpf);
        em.flush();
        paciente.addCpf(cpf);
        pacienteRepository.saveAndFlush(paciente);
        Long cpfId = cpf.getId();

        // Get all the pacienteList where cpf equals to cpfId
        defaultPacienteShouldBeFound("cpfId.equals=" + cpfId);

        // Get all the pacienteList where cpf equals to (cpfId + 1)
        defaultPacienteShouldNotBeFound("cpfId.equals=" + (cpfId + 1));
    }

    @Test
    @Transactional
    void getAllPacientesByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        ContatoPaciente cpf = ContatoPacienteResourceIT.createEntity(em);
        em.persist(cpf);
        em.flush();
        paciente.addCpf(cpf);
        pacienteRepository.saveAndFlush(paciente);
        Long cpfId = cpf.getId();

        // Get all the pacienteList where cpf equals to cpfId
        defaultPacienteShouldBeFound("cpfId.equals=" + cpfId);

        // Get all the pacienteList where cpf equals to (cpfId + 1)
        defaultPacienteShouldNotBeFound("cpfId.equals=" + (cpfId + 1));
    }

    @Test
    @Transactional
    void getAllPacientesByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        ContaClinica cpf = ContaClinicaResourceIT.createEntity(em);
        em.persist(cpf);
        em.flush();
        paciente.setCpf(cpf);
        cpf.setCpf(paciente);
        pacienteRepository.saveAndFlush(paciente);
        Long cpfId = cpf.getId();

        // Get all the pacienteList where cpf equals to cpfId
        defaultPacienteShouldBeFound("cpfId.equals=" + cpfId);

        // Get all the pacienteList where cpf equals to (cpfId + 1)
        defaultPacienteShouldNotBeFound("cpfId.equals=" + (cpfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPacienteShouldBeFound(String filter) throws Exception {
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].idade").value(hasItem(DEFAULT_IDADE)));

        // Check, that the count call also returns 1
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPacienteShouldNotBeFound(String filter) throws Exception {
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).get();
        // Disconnect from session so that the updates on updatedPaciente are not directly saved in db
        em.detach(updatedPaciente);
        updatedPaciente.cpf(UPDATED_CPF).nome(UPDATED_NOME).idade(UPDATED_IDADE);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(updatedPaciente);

        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPaciente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPaciente.getIdade()).isEqualTo(UPDATED_IDADE);
    }

    @Test
    @Transactional
    void putNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.nome(UPDATED_NOME).idade(UPDATED_IDADE);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPaciente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPaciente.getIdade()).isEqualTo(UPDATED_IDADE);
    }

    @Test
    @Transactional
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.cpf(UPDATED_CPF).nome(UPDATED_NOME).idade(UPDATED_IDADE);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPaciente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPaciente.getIdade()).isEqualTo(UPDATED_IDADE);
    }

    @Test
    @Transactional
    void patchNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pacienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeDelete = pacienteRepository.findAll().size();

        // Delete the paciente
        restPacienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paciente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
