package dev.clinic.simple.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.clinic.simple.IntegrationTest;
import dev.clinic.simple.domain.ContatoPaciente;
import dev.clinic.simple.domain.Paciente;
import dev.clinic.simple.repository.ContatoPacienteRepository;
import dev.clinic.simple.service.criteria.ContatoPacienteCriteria;
import dev.clinic.simple.service.dto.ContatoPacienteDTO;
import dev.clinic.simple.service.mapper.ContatoPacienteMapper;
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
 * Integration tests for the {@link ContatoPacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContatoPacienteResourceIT {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEUDO = "AAAAAAAAAA";
    private static final String UPDATED_CONTEUDO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contato-pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContatoPacienteRepository contatoPacienteRepository;

    @Autowired
    private ContatoPacienteMapper contatoPacienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContatoPacienteMockMvc;

    private ContatoPaciente contatoPaciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContatoPaciente createEntity(EntityManager em) {
        ContatoPaciente contatoPaciente = new ContatoPaciente().tipo(DEFAULT_TIPO).conteudo(DEFAULT_CONTEUDO);
        return contatoPaciente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContatoPaciente createUpdatedEntity(EntityManager em) {
        ContatoPaciente contatoPaciente = new ContatoPaciente().tipo(UPDATED_TIPO).conteudo(UPDATED_CONTEUDO);
        return contatoPaciente;
    }

    @BeforeEach
    public void initTest() {
        contatoPaciente = createEntity(em);
    }

    @Test
    @Transactional
    void createContatoPaciente() throws Exception {
        int databaseSizeBeforeCreate = contatoPacienteRepository.findAll().size();
        // Create the ContatoPaciente
        ContatoPacienteDTO contatoPacienteDTO = contatoPacienteMapper.toDto(contatoPaciente);
        restContatoPacienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contatoPacienteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeCreate + 1);
        ContatoPaciente testContatoPaciente = contatoPacienteList.get(contatoPacienteList.size() - 1);
        assertThat(testContatoPaciente.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testContatoPaciente.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
    }

    @Test
    @Transactional
    void createContatoPacienteWithExistingId() throws Exception {
        // Create the ContatoPaciente with an existing ID
        contatoPaciente.setId(1L);
        ContatoPacienteDTO contatoPacienteDTO = contatoPacienteMapper.toDto(contatoPaciente);

        int databaseSizeBeforeCreate = contatoPacienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContatoPacienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contatoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContatoPacientes() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList
        restContatoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contatoPaciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO)));
    }

    @Test
    @Transactional
    void getContatoPaciente() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get the contatoPaciente
        restContatoPacienteMockMvc
            .perform(get(ENTITY_API_URL_ID, contatoPaciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contatoPaciente.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.conteudo").value(DEFAULT_CONTEUDO));
    }

    @Test
    @Transactional
    void getContatoPacientesByIdFiltering() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        Long id = contatoPaciente.getId();

        defaultContatoPacienteShouldBeFound("id.equals=" + id);
        defaultContatoPacienteShouldNotBeFound("id.notEquals=" + id);

        defaultContatoPacienteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContatoPacienteShouldNotBeFound("id.greaterThan=" + id);

        defaultContatoPacienteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContatoPacienteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where tipo equals to DEFAULT_TIPO
        defaultContatoPacienteShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the contatoPacienteList where tipo equals to UPDATED_TIPO
        defaultContatoPacienteShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByTipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where tipo not equals to DEFAULT_TIPO
        defaultContatoPacienteShouldNotBeFound("tipo.notEquals=" + DEFAULT_TIPO);

        // Get all the contatoPacienteList where tipo not equals to UPDATED_TIPO
        defaultContatoPacienteShouldBeFound("tipo.notEquals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultContatoPacienteShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the contatoPacienteList where tipo equals to UPDATED_TIPO
        defaultContatoPacienteShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where tipo is not null
        defaultContatoPacienteShouldBeFound("tipo.specified=true");

        // Get all the contatoPacienteList where tipo is null
        defaultContatoPacienteShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllContatoPacientesByTipoContainsSomething() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where tipo contains DEFAULT_TIPO
        defaultContatoPacienteShouldBeFound("tipo.contains=" + DEFAULT_TIPO);

        // Get all the contatoPacienteList where tipo contains UPDATED_TIPO
        defaultContatoPacienteShouldNotBeFound("tipo.contains=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByTipoNotContainsSomething() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where tipo does not contain DEFAULT_TIPO
        defaultContatoPacienteShouldNotBeFound("tipo.doesNotContain=" + DEFAULT_TIPO);

        // Get all the contatoPacienteList where tipo does not contain UPDATED_TIPO
        defaultContatoPacienteShouldBeFound("tipo.doesNotContain=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByConteudoIsEqualToSomething() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where conteudo equals to DEFAULT_CONTEUDO
        defaultContatoPacienteShouldBeFound("conteudo.equals=" + DEFAULT_CONTEUDO);

        // Get all the contatoPacienteList where conteudo equals to UPDATED_CONTEUDO
        defaultContatoPacienteShouldNotBeFound("conteudo.equals=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByConteudoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where conteudo not equals to DEFAULT_CONTEUDO
        defaultContatoPacienteShouldNotBeFound("conteudo.notEquals=" + DEFAULT_CONTEUDO);

        // Get all the contatoPacienteList where conteudo not equals to UPDATED_CONTEUDO
        defaultContatoPacienteShouldBeFound("conteudo.notEquals=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByConteudoIsInShouldWork() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where conteudo in DEFAULT_CONTEUDO or UPDATED_CONTEUDO
        defaultContatoPacienteShouldBeFound("conteudo.in=" + DEFAULT_CONTEUDO + "," + UPDATED_CONTEUDO);

        // Get all the contatoPacienteList where conteudo equals to UPDATED_CONTEUDO
        defaultContatoPacienteShouldNotBeFound("conteudo.in=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByConteudoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where conteudo is not null
        defaultContatoPacienteShouldBeFound("conteudo.specified=true");

        // Get all the contatoPacienteList where conteudo is null
        defaultContatoPacienteShouldNotBeFound("conteudo.specified=false");
    }

    @Test
    @Transactional
    void getAllContatoPacientesByConteudoContainsSomething() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where conteudo contains DEFAULT_CONTEUDO
        defaultContatoPacienteShouldBeFound("conteudo.contains=" + DEFAULT_CONTEUDO);

        // Get all the contatoPacienteList where conteudo contains UPDATED_CONTEUDO
        defaultContatoPacienteShouldNotBeFound("conteudo.contains=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByConteudoNotContainsSomething() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        // Get all the contatoPacienteList where conteudo does not contain DEFAULT_CONTEUDO
        defaultContatoPacienteShouldNotBeFound("conteudo.doesNotContain=" + DEFAULT_CONTEUDO);

        // Get all the contatoPacienteList where conteudo does not contain UPDATED_CONTEUDO
        defaultContatoPacienteShouldBeFound("conteudo.doesNotContain=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllContatoPacientesByPacienteIsEqualToSomething() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);
        Paciente paciente = PacienteResourceIT.createEntity(em);
        em.persist(paciente);
        em.flush();
        contatoPaciente.setPaciente(paciente);
        contatoPacienteRepository.saveAndFlush(contatoPaciente);
        Long pacienteId = paciente.getId();

        // Get all the contatoPacienteList where paciente equals to pacienteId
        defaultContatoPacienteShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the contatoPacienteList where paciente equals to (pacienteId + 1)
        defaultContatoPacienteShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContatoPacienteShouldBeFound(String filter) throws Exception {
        restContatoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contatoPaciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO)));

        // Check, that the count call also returns 1
        restContatoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContatoPacienteShouldNotBeFound(String filter) throws Exception {
        restContatoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContatoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContatoPaciente() throws Exception {
        // Get the contatoPaciente
        restContatoPacienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContatoPaciente() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        int databaseSizeBeforeUpdate = contatoPacienteRepository.findAll().size();

        // Update the contatoPaciente
        ContatoPaciente updatedContatoPaciente = contatoPacienteRepository.findById(contatoPaciente.getId()).get();
        // Disconnect from session so that the updates on updatedContatoPaciente are not directly saved in db
        em.detach(updatedContatoPaciente);
        updatedContatoPaciente.tipo(UPDATED_TIPO).conteudo(UPDATED_CONTEUDO);
        ContatoPacienteDTO contatoPacienteDTO = contatoPacienteMapper.toDto(updatedContatoPaciente);

        restContatoPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contatoPacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contatoPacienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeUpdate);
        ContatoPaciente testContatoPaciente = contatoPacienteList.get(contatoPacienteList.size() - 1);
        assertThat(testContatoPaciente.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testContatoPaciente.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void putNonExistingContatoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = contatoPacienteRepository.findAll().size();
        contatoPaciente.setId(count.incrementAndGet());

        // Create the ContatoPaciente
        ContatoPacienteDTO contatoPacienteDTO = contatoPacienteMapper.toDto(contatoPaciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContatoPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contatoPacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contatoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContatoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = contatoPacienteRepository.findAll().size();
        contatoPaciente.setId(count.incrementAndGet());

        // Create the ContatoPaciente
        ContatoPacienteDTO contatoPacienteDTO = contatoPacienteMapper.toDto(contatoPaciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContatoPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contatoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContatoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = contatoPacienteRepository.findAll().size();
        contatoPaciente.setId(count.incrementAndGet());

        // Create the ContatoPaciente
        ContatoPacienteDTO contatoPacienteDTO = contatoPacienteMapper.toDto(contatoPaciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContatoPacienteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contatoPacienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContatoPacienteWithPatch() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        int databaseSizeBeforeUpdate = contatoPacienteRepository.findAll().size();

        // Update the contatoPaciente using partial update
        ContatoPaciente partialUpdatedContatoPaciente = new ContatoPaciente();
        partialUpdatedContatoPaciente.setId(contatoPaciente.getId());

        restContatoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContatoPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContatoPaciente))
            )
            .andExpect(status().isOk());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeUpdate);
        ContatoPaciente testContatoPaciente = contatoPacienteList.get(contatoPacienteList.size() - 1);
        assertThat(testContatoPaciente.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testContatoPaciente.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
    }

    @Test
    @Transactional
    void fullUpdateContatoPacienteWithPatch() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        int databaseSizeBeforeUpdate = contatoPacienteRepository.findAll().size();

        // Update the contatoPaciente using partial update
        ContatoPaciente partialUpdatedContatoPaciente = new ContatoPaciente();
        partialUpdatedContatoPaciente.setId(contatoPaciente.getId());

        partialUpdatedContatoPaciente.tipo(UPDATED_TIPO).conteudo(UPDATED_CONTEUDO);

        restContatoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContatoPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContatoPaciente))
            )
            .andExpect(status().isOk());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeUpdate);
        ContatoPaciente testContatoPaciente = contatoPacienteList.get(contatoPacienteList.size() - 1);
        assertThat(testContatoPaciente.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testContatoPaciente.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void patchNonExistingContatoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = contatoPacienteRepository.findAll().size();
        contatoPaciente.setId(count.incrementAndGet());

        // Create the ContatoPaciente
        ContatoPacienteDTO contatoPacienteDTO = contatoPacienteMapper.toDto(contatoPaciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContatoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contatoPacienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contatoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContatoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = contatoPacienteRepository.findAll().size();
        contatoPaciente.setId(count.incrementAndGet());

        // Create the ContatoPaciente
        ContatoPacienteDTO contatoPacienteDTO = contatoPacienteMapper.toDto(contatoPaciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContatoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contatoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContatoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = contatoPacienteRepository.findAll().size();
        contatoPaciente.setId(count.incrementAndGet());

        // Create the ContatoPaciente
        ContatoPacienteDTO contatoPacienteDTO = contatoPacienteMapper.toDto(contatoPaciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContatoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contatoPacienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContatoPaciente in the database
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContatoPaciente() throws Exception {
        // Initialize the database
        contatoPacienteRepository.saveAndFlush(contatoPaciente);

        int databaseSizeBeforeDelete = contatoPacienteRepository.findAll().size();

        // Delete the contatoPaciente
        restContatoPacienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, contatoPaciente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContatoPaciente> contatoPacienteList = contatoPacienteRepository.findAll();
        assertThat(contatoPacienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
