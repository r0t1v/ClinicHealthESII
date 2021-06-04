package dev.clinic.simple.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.clinic.simple.IntegrationTest;
import dev.clinic.simple.domain.EnderecoPaciente;
import dev.clinic.simple.domain.Paciente;
import dev.clinic.simple.repository.EnderecoPacienteRepository;
import dev.clinic.simple.service.criteria.EnderecoPacienteCriteria;
import dev.clinic.simple.service.dto.EnderecoPacienteDTO;
import dev.clinic.simple.service.mapper.EnderecoPacienteMapper;
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
 * Integration tests for the {@link EnderecoPacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnderecoPacienteResourceIT {

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_LOGRADOURO = "AAAAAAAAAA";
    private static final String UPDATED_LOGRADOURO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;
    private static final Integer SMALLER_NUMERO = 1 - 1;

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/endereco-pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnderecoPacienteRepository enderecoPacienteRepository;

    @Autowired
    private EnderecoPacienteMapper enderecoPacienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnderecoPacienteMockMvc;

    private EnderecoPaciente enderecoPaciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EnderecoPaciente createEntity(EntityManager em) {
        EnderecoPaciente enderecoPaciente = new EnderecoPaciente()
            .cidade(DEFAULT_CIDADE)
            .logradouro(DEFAULT_LOGRADOURO)
            .bairro(DEFAULT_BAIRRO)
            .numero(DEFAULT_NUMERO)
            .referencia(DEFAULT_REFERENCIA)
            .cep(DEFAULT_CEP);
        return enderecoPaciente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EnderecoPaciente createUpdatedEntity(EntityManager em) {
        EnderecoPaciente enderecoPaciente = new EnderecoPaciente()
            .cidade(UPDATED_CIDADE)
            .logradouro(UPDATED_LOGRADOURO)
            .bairro(UPDATED_BAIRRO)
            .numero(UPDATED_NUMERO)
            .referencia(UPDATED_REFERENCIA)
            .cep(UPDATED_CEP);
        return enderecoPaciente;
    }

    @BeforeEach
    public void initTest() {
        enderecoPaciente = createEntity(em);
    }

    @Test
    @Transactional
    void createEnderecoPaciente() throws Exception {
        int databaseSizeBeforeCreate = enderecoPacienteRepository.findAll().size();
        // Create the EnderecoPaciente
        EnderecoPacienteDTO enderecoPacienteDTO = enderecoPacienteMapper.toDto(enderecoPaciente);
        restEnderecoPacienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoPacienteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeCreate + 1);
        EnderecoPaciente testEnderecoPaciente = enderecoPacienteList.get(enderecoPacienteList.size() - 1);
        assertThat(testEnderecoPaciente.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testEnderecoPaciente.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testEnderecoPaciente.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEnderecoPaciente.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEnderecoPaciente.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
        assertThat(testEnderecoPaciente.getCep()).isEqualTo(DEFAULT_CEP);
    }

    @Test
    @Transactional
    void createEnderecoPacienteWithExistingId() throws Exception {
        // Create the EnderecoPaciente with an existing ID
        enderecoPaciente.setId(1L);
        EnderecoPacienteDTO enderecoPacienteDTO = enderecoPacienteMapper.toDto(enderecoPaciente);

        int databaseSizeBeforeCreate = enderecoPacienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnderecoPacienteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientes() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList
        restEnderecoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enderecoPaciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)));
    }

    @Test
    @Transactional
    void getEnderecoPaciente() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get the enderecoPaciente
        restEnderecoPacienteMockMvc
            .perform(get(ENTITY_API_URL_ID, enderecoPaciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enderecoPaciente.getId().intValue()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP));
    }

    @Test
    @Transactional
    void getEnderecoPacientesByIdFiltering() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        Long id = enderecoPaciente.getId();

        defaultEnderecoPacienteShouldBeFound("id.equals=" + id);
        defaultEnderecoPacienteShouldNotBeFound("id.notEquals=" + id);

        defaultEnderecoPacienteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEnderecoPacienteShouldNotBeFound("id.greaterThan=" + id);

        defaultEnderecoPacienteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEnderecoPacienteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cidade equals to DEFAULT_CIDADE
        defaultEnderecoPacienteShouldBeFound("cidade.equals=" + DEFAULT_CIDADE);

        // Get all the enderecoPacienteList where cidade equals to UPDATED_CIDADE
        defaultEnderecoPacienteShouldNotBeFound("cidade.equals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCidadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cidade not equals to DEFAULT_CIDADE
        defaultEnderecoPacienteShouldNotBeFound("cidade.notEquals=" + DEFAULT_CIDADE);

        // Get all the enderecoPacienteList where cidade not equals to UPDATED_CIDADE
        defaultEnderecoPacienteShouldBeFound("cidade.notEquals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCidadeIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cidade in DEFAULT_CIDADE or UPDATED_CIDADE
        defaultEnderecoPacienteShouldBeFound("cidade.in=" + DEFAULT_CIDADE + "," + UPDATED_CIDADE);

        // Get all the enderecoPacienteList where cidade equals to UPDATED_CIDADE
        defaultEnderecoPacienteShouldNotBeFound("cidade.in=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cidade is not null
        defaultEnderecoPacienteShouldBeFound("cidade.specified=true");

        // Get all the enderecoPacienteList where cidade is null
        defaultEnderecoPacienteShouldNotBeFound("cidade.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCidadeContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cidade contains DEFAULT_CIDADE
        defaultEnderecoPacienteShouldBeFound("cidade.contains=" + DEFAULT_CIDADE);

        // Get all the enderecoPacienteList where cidade contains UPDATED_CIDADE
        defaultEnderecoPacienteShouldNotBeFound("cidade.contains=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCidadeNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cidade does not contain DEFAULT_CIDADE
        defaultEnderecoPacienteShouldNotBeFound("cidade.doesNotContain=" + DEFAULT_CIDADE);

        // Get all the enderecoPacienteList where cidade does not contain UPDATED_CIDADE
        defaultEnderecoPacienteShouldBeFound("cidade.doesNotContain=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByLogradouroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where logradouro equals to DEFAULT_LOGRADOURO
        defaultEnderecoPacienteShouldBeFound("logradouro.equals=" + DEFAULT_LOGRADOURO);

        // Get all the enderecoPacienteList where logradouro equals to UPDATED_LOGRADOURO
        defaultEnderecoPacienteShouldNotBeFound("logradouro.equals=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByLogradouroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where logradouro not equals to DEFAULT_LOGRADOURO
        defaultEnderecoPacienteShouldNotBeFound("logradouro.notEquals=" + DEFAULT_LOGRADOURO);

        // Get all the enderecoPacienteList where logradouro not equals to UPDATED_LOGRADOURO
        defaultEnderecoPacienteShouldBeFound("logradouro.notEquals=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByLogradouroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where logradouro in DEFAULT_LOGRADOURO or UPDATED_LOGRADOURO
        defaultEnderecoPacienteShouldBeFound("logradouro.in=" + DEFAULT_LOGRADOURO + "," + UPDATED_LOGRADOURO);

        // Get all the enderecoPacienteList where logradouro equals to UPDATED_LOGRADOURO
        defaultEnderecoPacienteShouldNotBeFound("logradouro.in=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByLogradouroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where logradouro is not null
        defaultEnderecoPacienteShouldBeFound("logradouro.specified=true");

        // Get all the enderecoPacienteList where logradouro is null
        defaultEnderecoPacienteShouldNotBeFound("logradouro.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByLogradouroContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where logradouro contains DEFAULT_LOGRADOURO
        defaultEnderecoPacienteShouldBeFound("logradouro.contains=" + DEFAULT_LOGRADOURO);

        // Get all the enderecoPacienteList where logradouro contains UPDATED_LOGRADOURO
        defaultEnderecoPacienteShouldNotBeFound("logradouro.contains=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByLogradouroNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where logradouro does not contain DEFAULT_LOGRADOURO
        defaultEnderecoPacienteShouldNotBeFound("logradouro.doesNotContain=" + DEFAULT_LOGRADOURO);

        // Get all the enderecoPacienteList where logradouro does not contain UPDATED_LOGRADOURO
        defaultEnderecoPacienteShouldBeFound("logradouro.doesNotContain=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByBairroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where bairro equals to DEFAULT_BAIRRO
        defaultEnderecoPacienteShouldBeFound("bairro.equals=" + DEFAULT_BAIRRO);

        // Get all the enderecoPacienteList where bairro equals to UPDATED_BAIRRO
        defaultEnderecoPacienteShouldNotBeFound("bairro.equals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByBairroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where bairro not equals to DEFAULT_BAIRRO
        defaultEnderecoPacienteShouldNotBeFound("bairro.notEquals=" + DEFAULT_BAIRRO);

        // Get all the enderecoPacienteList where bairro not equals to UPDATED_BAIRRO
        defaultEnderecoPacienteShouldBeFound("bairro.notEquals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByBairroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where bairro in DEFAULT_BAIRRO or UPDATED_BAIRRO
        defaultEnderecoPacienteShouldBeFound("bairro.in=" + DEFAULT_BAIRRO + "," + UPDATED_BAIRRO);

        // Get all the enderecoPacienteList where bairro equals to UPDATED_BAIRRO
        defaultEnderecoPacienteShouldNotBeFound("bairro.in=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByBairroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where bairro is not null
        defaultEnderecoPacienteShouldBeFound("bairro.specified=true");

        // Get all the enderecoPacienteList where bairro is null
        defaultEnderecoPacienteShouldNotBeFound("bairro.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByBairroContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where bairro contains DEFAULT_BAIRRO
        defaultEnderecoPacienteShouldBeFound("bairro.contains=" + DEFAULT_BAIRRO);

        // Get all the enderecoPacienteList where bairro contains UPDATED_BAIRRO
        defaultEnderecoPacienteShouldNotBeFound("bairro.contains=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByBairroNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where bairro does not contain DEFAULT_BAIRRO
        defaultEnderecoPacienteShouldNotBeFound("bairro.doesNotContain=" + DEFAULT_BAIRRO);

        // Get all the enderecoPacienteList where bairro does not contain UPDATED_BAIRRO
        defaultEnderecoPacienteShouldBeFound("bairro.doesNotContain=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where numero equals to DEFAULT_NUMERO
        defaultEnderecoPacienteShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the enderecoPacienteList where numero equals to UPDATED_NUMERO
        defaultEnderecoPacienteShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByNumeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where numero not equals to DEFAULT_NUMERO
        defaultEnderecoPacienteShouldNotBeFound("numero.notEquals=" + DEFAULT_NUMERO);

        // Get all the enderecoPacienteList where numero not equals to UPDATED_NUMERO
        defaultEnderecoPacienteShouldBeFound("numero.notEquals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultEnderecoPacienteShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the enderecoPacienteList where numero equals to UPDATED_NUMERO
        defaultEnderecoPacienteShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where numero is not null
        defaultEnderecoPacienteShouldBeFound("numero.specified=true");

        // Get all the enderecoPacienteList where numero is null
        defaultEnderecoPacienteShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByNumeroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where numero is greater than or equal to DEFAULT_NUMERO
        defaultEnderecoPacienteShouldBeFound("numero.greaterThanOrEqual=" + DEFAULT_NUMERO);

        // Get all the enderecoPacienteList where numero is greater than or equal to UPDATED_NUMERO
        defaultEnderecoPacienteShouldNotBeFound("numero.greaterThanOrEqual=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByNumeroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where numero is less than or equal to DEFAULT_NUMERO
        defaultEnderecoPacienteShouldBeFound("numero.lessThanOrEqual=" + DEFAULT_NUMERO);

        // Get all the enderecoPacienteList where numero is less than or equal to SMALLER_NUMERO
        defaultEnderecoPacienteShouldNotBeFound("numero.lessThanOrEqual=" + SMALLER_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByNumeroIsLessThanSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where numero is less than DEFAULT_NUMERO
        defaultEnderecoPacienteShouldNotBeFound("numero.lessThan=" + DEFAULT_NUMERO);

        // Get all the enderecoPacienteList where numero is less than UPDATED_NUMERO
        defaultEnderecoPacienteShouldBeFound("numero.lessThan=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByNumeroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where numero is greater than DEFAULT_NUMERO
        defaultEnderecoPacienteShouldNotBeFound("numero.greaterThan=" + DEFAULT_NUMERO);

        // Get all the enderecoPacienteList where numero is greater than SMALLER_NUMERO
        defaultEnderecoPacienteShouldBeFound("numero.greaterThan=" + SMALLER_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where referencia equals to DEFAULT_REFERENCIA
        defaultEnderecoPacienteShouldBeFound("referencia.equals=" + DEFAULT_REFERENCIA);

        // Get all the enderecoPacienteList where referencia equals to UPDATED_REFERENCIA
        defaultEnderecoPacienteShouldNotBeFound("referencia.equals=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByReferenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where referencia not equals to DEFAULT_REFERENCIA
        defaultEnderecoPacienteShouldNotBeFound("referencia.notEquals=" + DEFAULT_REFERENCIA);

        // Get all the enderecoPacienteList where referencia not equals to UPDATED_REFERENCIA
        defaultEnderecoPacienteShouldBeFound("referencia.notEquals=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where referencia in DEFAULT_REFERENCIA or UPDATED_REFERENCIA
        defaultEnderecoPacienteShouldBeFound("referencia.in=" + DEFAULT_REFERENCIA + "," + UPDATED_REFERENCIA);

        // Get all the enderecoPacienteList where referencia equals to UPDATED_REFERENCIA
        defaultEnderecoPacienteShouldNotBeFound("referencia.in=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where referencia is not null
        defaultEnderecoPacienteShouldBeFound("referencia.specified=true");

        // Get all the enderecoPacienteList where referencia is null
        defaultEnderecoPacienteShouldNotBeFound("referencia.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByReferenciaContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where referencia contains DEFAULT_REFERENCIA
        defaultEnderecoPacienteShouldBeFound("referencia.contains=" + DEFAULT_REFERENCIA);

        // Get all the enderecoPacienteList where referencia contains UPDATED_REFERENCIA
        defaultEnderecoPacienteShouldNotBeFound("referencia.contains=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByReferenciaNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where referencia does not contain DEFAULT_REFERENCIA
        defaultEnderecoPacienteShouldNotBeFound("referencia.doesNotContain=" + DEFAULT_REFERENCIA);

        // Get all the enderecoPacienteList where referencia does not contain UPDATED_REFERENCIA
        defaultEnderecoPacienteShouldBeFound("referencia.doesNotContain=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cep equals to DEFAULT_CEP
        defaultEnderecoPacienteShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the enderecoPacienteList where cep equals to UPDATED_CEP
        defaultEnderecoPacienteShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCepIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cep not equals to DEFAULT_CEP
        defaultEnderecoPacienteShouldNotBeFound("cep.notEquals=" + DEFAULT_CEP);

        // Get all the enderecoPacienteList where cep not equals to UPDATED_CEP
        defaultEnderecoPacienteShouldBeFound("cep.notEquals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCepIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultEnderecoPacienteShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the enderecoPacienteList where cep equals to UPDATED_CEP
        defaultEnderecoPacienteShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cep is not null
        defaultEnderecoPacienteShouldBeFound("cep.specified=true");

        // Get all the enderecoPacienteList where cep is null
        defaultEnderecoPacienteShouldNotBeFound("cep.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCepContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cep contains DEFAULT_CEP
        defaultEnderecoPacienteShouldBeFound("cep.contains=" + DEFAULT_CEP);

        // Get all the enderecoPacienteList where cep contains UPDATED_CEP
        defaultEnderecoPacienteShouldNotBeFound("cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByCepNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        // Get all the enderecoPacienteList where cep does not contain DEFAULT_CEP
        defaultEnderecoPacienteShouldNotBeFound("cep.doesNotContain=" + DEFAULT_CEP);

        // Get all the enderecoPacienteList where cep does not contain UPDATED_CEP
        defaultEnderecoPacienteShouldBeFound("cep.doesNotContain=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecoPacientesByPacienteIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);
        Paciente paciente = PacienteResourceIT.createEntity(em);
        em.persist(paciente);
        em.flush();
        enderecoPaciente.setPaciente(paciente);
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);
        Long pacienteId = paciente.getId();

        // Get all the enderecoPacienteList where paciente equals to pacienteId
        defaultEnderecoPacienteShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the enderecoPacienteList where paciente equals to (pacienteId + 1)
        defaultEnderecoPacienteShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnderecoPacienteShouldBeFound(String filter) throws Exception {
        restEnderecoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enderecoPaciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)));

        // Check, that the count call also returns 1
        restEnderecoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnderecoPacienteShouldNotBeFound(String filter) throws Exception {
        restEnderecoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnderecoPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEnderecoPaciente() throws Exception {
        // Get the enderecoPaciente
        restEnderecoPacienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEnderecoPaciente() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        int databaseSizeBeforeUpdate = enderecoPacienteRepository.findAll().size();

        // Update the enderecoPaciente
        EnderecoPaciente updatedEnderecoPaciente = enderecoPacienteRepository.findById(enderecoPaciente.getId()).get();
        // Disconnect from session so that the updates on updatedEnderecoPaciente are not directly saved in db
        em.detach(updatedEnderecoPaciente);
        updatedEnderecoPaciente
            .cidade(UPDATED_CIDADE)
            .logradouro(UPDATED_LOGRADOURO)
            .bairro(UPDATED_BAIRRO)
            .numero(UPDATED_NUMERO)
            .referencia(UPDATED_REFERENCIA)
            .cep(UPDATED_CEP);
        EnderecoPacienteDTO enderecoPacienteDTO = enderecoPacienteMapper.toDto(updatedEnderecoPaciente);

        restEnderecoPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enderecoPacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enderecoPacienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeUpdate);
        EnderecoPaciente testEnderecoPaciente = enderecoPacienteList.get(enderecoPacienteList.size() - 1);
        assertThat(testEnderecoPaciente.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testEnderecoPaciente.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEnderecoPaciente.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEnderecoPaciente.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEnderecoPaciente.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testEnderecoPaciente.getCep()).isEqualTo(UPDATED_CEP);
    }

    @Test
    @Transactional
    void putNonExistingEnderecoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = enderecoPacienteRepository.findAll().size();
        enderecoPaciente.setId(count.incrementAndGet());

        // Create the EnderecoPaciente
        EnderecoPacienteDTO enderecoPacienteDTO = enderecoPacienteMapper.toDto(enderecoPaciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enderecoPacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enderecoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnderecoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = enderecoPacienteRepository.findAll().size();
        enderecoPaciente.setId(count.incrementAndGet());

        // Create the EnderecoPaciente
        EnderecoPacienteDTO enderecoPacienteDTO = enderecoPacienteMapper.toDto(enderecoPaciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enderecoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnderecoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = enderecoPacienteRepository.findAll().size();
        enderecoPaciente.setId(count.incrementAndGet());

        // Create the EnderecoPaciente
        EnderecoPacienteDTO enderecoPacienteDTO = enderecoPacienteMapper.toDto(enderecoPaciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoPacienteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoPacienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnderecoPacienteWithPatch() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        int databaseSizeBeforeUpdate = enderecoPacienteRepository.findAll().size();

        // Update the enderecoPaciente using partial update
        EnderecoPaciente partialUpdatedEnderecoPaciente = new EnderecoPaciente();
        partialUpdatedEnderecoPaciente.setId(enderecoPaciente.getId());

        partialUpdatedEnderecoPaciente
            .cidade(UPDATED_CIDADE)
            .logradouro(UPDATED_LOGRADOURO)
            .bairro(UPDATED_BAIRRO)
            .referencia(UPDATED_REFERENCIA);

        restEnderecoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnderecoPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnderecoPaciente))
            )
            .andExpect(status().isOk());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeUpdate);
        EnderecoPaciente testEnderecoPaciente = enderecoPacienteList.get(enderecoPacienteList.size() - 1);
        assertThat(testEnderecoPaciente.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testEnderecoPaciente.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEnderecoPaciente.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEnderecoPaciente.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEnderecoPaciente.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testEnderecoPaciente.getCep()).isEqualTo(DEFAULT_CEP);
    }

    @Test
    @Transactional
    void fullUpdateEnderecoPacienteWithPatch() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        int databaseSizeBeforeUpdate = enderecoPacienteRepository.findAll().size();

        // Update the enderecoPaciente using partial update
        EnderecoPaciente partialUpdatedEnderecoPaciente = new EnderecoPaciente();
        partialUpdatedEnderecoPaciente.setId(enderecoPaciente.getId());

        partialUpdatedEnderecoPaciente
            .cidade(UPDATED_CIDADE)
            .logradouro(UPDATED_LOGRADOURO)
            .bairro(UPDATED_BAIRRO)
            .numero(UPDATED_NUMERO)
            .referencia(UPDATED_REFERENCIA)
            .cep(UPDATED_CEP);

        restEnderecoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnderecoPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnderecoPaciente))
            )
            .andExpect(status().isOk());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeUpdate);
        EnderecoPaciente testEnderecoPaciente = enderecoPacienteList.get(enderecoPacienteList.size() - 1);
        assertThat(testEnderecoPaciente.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testEnderecoPaciente.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEnderecoPaciente.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEnderecoPaciente.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEnderecoPaciente.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testEnderecoPaciente.getCep()).isEqualTo(UPDATED_CEP);
    }

    @Test
    @Transactional
    void patchNonExistingEnderecoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = enderecoPacienteRepository.findAll().size();
        enderecoPaciente.setId(count.incrementAndGet());

        // Create the EnderecoPaciente
        EnderecoPacienteDTO enderecoPacienteDTO = enderecoPacienteMapper.toDto(enderecoPaciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enderecoPacienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enderecoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnderecoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = enderecoPacienteRepository.findAll().size();
        enderecoPaciente.setId(count.incrementAndGet());

        // Create the EnderecoPaciente
        EnderecoPacienteDTO enderecoPacienteDTO = enderecoPacienteMapper.toDto(enderecoPaciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enderecoPacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnderecoPaciente() throws Exception {
        int databaseSizeBeforeUpdate = enderecoPacienteRepository.findAll().size();
        enderecoPaciente.setId(count.incrementAndGet());

        // Create the EnderecoPaciente
        EnderecoPacienteDTO enderecoPacienteDTO = enderecoPacienteMapper.toDto(enderecoPaciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enderecoPacienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EnderecoPaciente in the database
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnderecoPaciente() throws Exception {
        // Initialize the database
        enderecoPacienteRepository.saveAndFlush(enderecoPaciente);

        int databaseSizeBeforeDelete = enderecoPacienteRepository.findAll().size();

        // Delete the enderecoPaciente
        restEnderecoPacienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, enderecoPaciente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EnderecoPaciente> enderecoPacienteList = enderecoPacienteRepository.findAll();
        assertThat(enderecoPacienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
