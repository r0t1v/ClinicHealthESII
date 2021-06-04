package dev.clinic.simple.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.clinic.simple.IntegrationTest;
import dev.clinic.simple.domain.Exame;
import dev.clinic.simple.domain.PagamentoExame;
import dev.clinic.simple.repository.PagamentoExameRepository;
import dev.clinic.simple.service.criteria.PagamentoExameCriteria;
import dev.clinic.simple.service.dto.PagamentoExameDTO;
import dev.clinic.simple.service.mapper.PagamentoExameMapper;
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
 * Integration tests for the {@link PagamentoExameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PagamentoExameResourceIT {

    private static final String DEFAULT_FORMAPAGAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_FORMAPAGAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEUDO = "AAAAAAAAAA";
    private static final String UPDATED_CONTEUDO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SELIQUIDADO = false;
    private static final Boolean UPDATED_SELIQUIDADO = true;

    private static final String ENTITY_API_URL = "/api/pagamento-exames";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PagamentoExameRepository pagamentoExameRepository;

    @Autowired
    private PagamentoExameMapper pagamentoExameMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPagamentoExameMockMvc;

    private PagamentoExame pagamentoExame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PagamentoExame createEntity(EntityManager em) {
        PagamentoExame pagamentoExame = new PagamentoExame()
            .formapagamento(DEFAULT_FORMAPAGAMENTO)
            .conteudo(DEFAULT_CONTEUDO)
            .seliquidado(DEFAULT_SELIQUIDADO);
        return pagamentoExame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PagamentoExame createUpdatedEntity(EntityManager em) {
        PagamentoExame pagamentoExame = new PagamentoExame()
            .formapagamento(UPDATED_FORMAPAGAMENTO)
            .conteudo(UPDATED_CONTEUDO)
            .seliquidado(UPDATED_SELIQUIDADO);
        return pagamentoExame;
    }

    @BeforeEach
    public void initTest() {
        pagamentoExame = createEntity(em);
    }

    @Test
    @Transactional
    void createPagamentoExame() throws Exception {
        int databaseSizeBeforeCreate = pagamentoExameRepository.findAll().size();
        // Create the PagamentoExame
        PagamentoExameDTO pagamentoExameDTO = pagamentoExameMapper.toDto(pagamentoExame);
        restPagamentoExameMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagamentoExameDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeCreate + 1);
        PagamentoExame testPagamentoExame = pagamentoExameList.get(pagamentoExameList.size() - 1);
        assertThat(testPagamentoExame.getFormapagamento()).isEqualTo(DEFAULT_FORMAPAGAMENTO);
        assertThat(testPagamentoExame.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
        assertThat(testPagamentoExame.getSeliquidado()).isEqualTo(DEFAULT_SELIQUIDADO);
    }

    @Test
    @Transactional
    void createPagamentoExameWithExistingId() throws Exception {
        // Create the PagamentoExame with an existing ID
        pagamentoExame.setId(1L);
        PagamentoExameDTO pagamentoExameDTO = pagamentoExameMapper.toDto(pagamentoExame);

        int databaseSizeBeforeCreate = pagamentoExameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagamentoExameMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagamentoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPagamentoExames() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList
        restPagamentoExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pagamentoExame.getId().intValue())))
            .andExpect(jsonPath("$.[*].formapagamento").value(hasItem(DEFAULT_FORMAPAGAMENTO)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO)))
            .andExpect(jsonPath("$.[*].seliquidado").value(hasItem(DEFAULT_SELIQUIDADO.booleanValue())));
    }

    @Test
    @Transactional
    void getPagamentoExame() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get the pagamentoExame
        restPagamentoExameMockMvc
            .perform(get(ENTITY_API_URL_ID, pagamentoExame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pagamentoExame.getId().intValue()))
            .andExpect(jsonPath("$.formapagamento").value(DEFAULT_FORMAPAGAMENTO))
            .andExpect(jsonPath("$.conteudo").value(DEFAULT_CONTEUDO))
            .andExpect(jsonPath("$.seliquidado").value(DEFAULT_SELIQUIDADO.booleanValue()));
    }

    @Test
    @Transactional
    void getPagamentoExamesByIdFiltering() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        Long id = pagamentoExame.getId();

        defaultPagamentoExameShouldBeFound("id.equals=" + id);
        defaultPagamentoExameShouldNotBeFound("id.notEquals=" + id);

        defaultPagamentoExameShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPagamentoExameShouldNotBeFound("id.greaterThan=" + id);

        defaultPagamentoExameShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPagamentoExameShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByFormapagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where formapagamento equals to DEFAULT_FORMAPAGAMENTO
        defaultPagamentoExameShouldBeFound("formapagamento.equals=" + DEFAULT_FORMAPAGAMENTO);

        // Get all the pagamentoExameList where formapagamento equals to UPDATED_FORMAPAGAMENTO
        defaultPagamentoExameShouldNotBeFound("formapagamento.equals=" + UPDATED_FORMAPAGAMENTO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByFormapagamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where formapagamento not equals to DEFAULT_FORMAPAGAMENTO
        defaultPagamentoExameShouldNotBeFound("formapagamento.notEquals=" + DEFAULT_FORMAPAGAMENTO);

        // Get all the pagamentoExameList where formapagamento not equals to UPDATED_FORMAPAGAMENTO
        defaultPagamentoExameShouldBeFound("formapagamento.notEquals=" + UPDATED_FORMAPAGAMENTO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByFormapagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where formapagamento in DEFAULT_FORMAPAGAMENTO or UPDATED_FORMAPAGAMENTO
        defaultPagamentoExameShouldBeFound("formapagamento.in=" + DEFAULT_FORMAPAGAMENTO + "," + UPDATED_FORMAPAGAMENTO);

        // Get all the pagamentoExameList where formapagamento equals to UPDATED_FORMAPAGAMENTO
        defaultPagamentoExameShouldNotBeFound("formapagamento.in=" + UPDATED_FORMAPAGAMENTO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByFormapagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where formapagamento is not null
        defaultPagamentoExameShouldBeFound("formapagamento.specified=true");

        // Get all the pagamentoExameList where formapagamento is null
        defaultPagamentoExameShouldNotBeFound("formapagamento.specified=false");
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByFormapagamentoContainsSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where formapagamento contains DEFAULT_FORMAPAGAMENTO
        defaultPagamentoExameShouldBeFound("formapagamento.contains=" + DEFAULT_FORMAPAGAMENTO);

        // Get all the pagamentoExameList where formapagamento contains UPDATED_FORMAPAGAMENTO
        defaultPagamentoExameShouldNotBeFound("formapagamento.contains=" + UPDATED_FORMAPAGAMENTO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByFormapagamentoNotContainsSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where formapagamento does not contain DEFAULT_FORMAPAGAMENTO
        defaultPagamentoExameShouldNotBeFound("formapagamento.doesNotContain=" + DEFAULT_FORMAPAGAMENTO);

        // Get all the pagamentoExameList where formapagamento does not contain UPDATED_FORMAPAGAMENTO
        defaultPagamentoExameShouldBeFound("formapagamento.doesNotContain=" + UPDATED_FORMAPAGAMENTO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByConteudoIsEqualToSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where conteudo equals to DEFAULT_CONTEUDO
        defaultPagamentoExameShouldBeFound("conteudo.equals=" + DEFAULT_CONTEUDO);

        // Get all the pagamentoExameList where conteudo equals to UPDATED_CONTEUDO
        defaultPagamentoExameShouldNotBeFound("conteudo.equals=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByConteudoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where conteudo not equals to DEFAULT_CONTEUDO
        defaultPagamentoExameShouldNotBeFound("conteudo.notEquals=" + DEFAULT_CONTEUDO);

        // Get all the pagamentoExameList where conteudo not equals to UPDATED_CONTEUDO
        defaultPagamentoExameShouldBeFound("conteudo.notEquals=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByConteudoIsInShouldWork() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where conteudo in DEFAULT_CONTEUDO or UPDATED_CONTEUDO
        defaultPagamentoExameShouldBeFound("conteudo.in=" + DEFAULT_CONTEUDO + "," + UPDATED_CONTEUDO);

        // Get all the pagamentoExameList where conteudo equals to UPDATED_CONTEUDO
        defaultPagamentoExameShouldNotBeFound("conteudo.in=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByConteudoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where conteudo is not null
        defaultPagamentoExameShouldBeFound("conteudo.specified=true");

        // Get all the pagamentoExameList where conteudo is null
        defaultPagamentoExameShouldNotBeFound("conteudo.specified=false");
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByConteudoContainsSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where conteudo contains DEFAULT_CONTEUDO
        defaultPagamentoExameShouldBeFound("conteudo.contains=" + DEFAULT_CONTEUDO);

        // Get all the pagamentoExameList where conteudo contains UPDATED_CONTEUDO
        defaultPagamentoExameShouldNotBeFound("conteudo.contains=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByConteudoNotContainsSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where conteudo does not contain DEFAULT_CONTEUDO
        defaultPagamentoExameShouldNotBeFound("conteudo.doesNotContain=" + DEFAULT_CONTEUDO);

        // Get all the pagamentoExameList where conteudo does not contain UPDATED_CONTEUDO
        defaultPagamentoExameShouldBeFound("conteudo.doesNotContain=" + UPDATED_CONTEUDO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesBySeliquidadoIsEqualToSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where seliquidado equals to DEFAULT_SELIQUIDADO
        defaultPagamentoExameShouldBeFound("seliquidado.equals=" + DEFAULT_SELIQUIDADO);

        // Get all the pagamentoExameList where seliquidado equals to UPDATED_SELIQUIDADO
        defaultPagamentoExameShouldNotBeFound("seliquidado.equals=" + UPDATED_SELIQUIDADO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesBySeliquidadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where seliquidado not equals to DEFAULT_SELIQUIDADO
        defaultPagamentoExameShouldNotBeFound("seliquidado.notEquals=" + DEFAULT_SELIQUIDADO);

        // Get all the pagamentoExameList where seliquidado not equals to UPDATED_SELIQUIDADO
        defaultPagamentoExameShouldBeFound("seliquidado.notEquals=" + UPDATED_SELIQUIDADO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesBySeliquidadoIsInShouldWork() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where seliquidado in DEFAULT_SELIQUIDADO or UPDATED_SELIQUIDADO
        defaultPagamentoExameShouldBeFound("seliquidado.in=" + DEFAULT_SELIQUIDADO + "," + UPDATED_SELIQUIDADO);

        // Get all the pagamentoExameList where seliquidado equals to UPDATED_SELIQUIDADO
        defaultPagamentoExameShouldNotBeFound("seliquidado.in=" + UPDATED_SELIQUIDADO);
    }

    @Test
    @Transactional
    void getAllPagamentoExamesBySeliquidadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        // Get all the pagamentoExameList where seliquidado is not null
        defaultPagamentoExameShouldBeFound("seliquidado.specified=true");

        // Get all the pagamentoExameList where seliquidado is null
        defaultPagamentoExameShouldNotBeFound("seliquidado.specified=false");
    }

    @Test
    @Transactional
    void getAllPagamentoExamesByExameIsEqualToSomething() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);
        Exame exame = ExameResourceIT.createEntity(em);
        em.persist(exame);
        em.flush();
        pagamentoExame.setExame(exame);
        pagamentoExameRepository.saveAndFlush(pagamentoExame);
        Long exameId = exame.getId();

        // Get all the pagamentoExameList where exame equals to exameId
        defaultPagamentoExameShouldBeFound("exameId.equals=" + exameId);

        // Get all the pagamentoExameList where exame equals to (exameId + 1)
        defaultPagamentoExameShouldNotBeFound("exameId.equals=" + (exameId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPagamentoExameShouldBeFound(String filter) throws Exception {
        restPagamentoExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pagamentoExame.getId().intValue())))
            .andExpect(jsonPath("$.[*].formapagamento").value(hasItem(DEFAULT_FORMAPAGAMENTO)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO)))
            .andExpect(jsonPath("$.[*].seliquidado").value(hasItem(DEFAULT_SELIQUIDADO.booleanValue())));

        // Check, that the count call also returns 1
        restPagamentoExameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPagamentoExameShouldNotBeFound(String filter) throws Exception {
        restPagamentoExameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPagamentoExameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPagamentoExame() throws Exception {
        // Get the pagamentoExame
        restPagamentoExameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPagamentoExame() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        int databaseSizeBeforeUpdate = pagamentoExameRepository.findAll().size();

        // Update the pagamentoExame
        PagamentoExame updatedPagamentoExame = pagamentoExameRepository.findById(pagamentoExame.getId()).get();
        // Disconnect from session so that the updates on updatedPagamentoExame are not directly saved in db
        em.detach(updatedPagamentoExame);
        updatedPagamentoExame.formapagamento(UPDATED_FORMAPAGAMENTO).conteudo(UPDATED_CONTEUDO).seliquidado(UPDATED_SELIQUIDADO);
        PagamentoExameDTO pagamentoExameDTO = pagamentoExameMapper.toDto(updatedPagamentoExame);

        restPagamentoExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pagamentoExameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagamentoExameDTO))
            )
            .andExpect(status().isOk());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeUpdate);
        PagamentoExame testPagamentoExame = pagamentoExameList.get(pagamentoExameList.size() - 1);
        assertThat(testPagamentoExame.getFormapagamento()).isEqualTo(UPDATED_FORMAPAGAMENTO);
        assertThat(testPagamentoExame.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
        assertThat(testPagamentoExame.getSeliquidado()).isEqualTo(UPDATED_SELIQUIDADO);
    }

    @Test
    @Transactional
    void putNonExistingPagamentoExame() throws Exception {
        int databaseSizeBeforeUpdate = pagamentoExameRepository.findAll().size();
        pagamentoExame.setId(count.incrementAndGet());

        // Create the PagamentoExame
        PagamentoExameDTO pagamentoExameDTO = pagamentoExameMapper.toDto(pagamentoExame);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagamentoExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pagamentoExameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagamentoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPagamentoExame() throws Exception {
        int databaseSizeBeforeUpdate = pagamentoExameRepository.findAll().size();
        pagamentoExame.setId(count.incrementAndGet());

        // Create the PagamentoExame
        PagamentoExameDTO pagamentoExameDTO = pagamentoExameMapper.toDto(pagamentoExame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagamentoExameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagamentoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPagamentoExame() throws Exception {
        int databaseSizeBeforeUpdate = pagamentoExameRepository.findAll().size();
        pagamentoExame.setId(count.incrementAndGet());

        // Create the PagamentoExame
        PagamentoExameDTO pagamentoExameDTO = pagamentoExameMapper.toDto(pagamentoExame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagamentoExameMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagamentoExameDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePagamentoExameWithPatch() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        int databaseSizeBeforeUpdate = pagamentoExameRepository.findAll().size();

        // Update the pagamentoExame using partial update
        PagamentoExame partialUpdatedPagamentoExame = new PagamentoExame();
        partialUpdatedPagamentoExame.setId(pagamentoExame.getId());

        partialUpdatedPagamentoExame.seliquidado(UPDATED_SELIQUIDADO);

        restPagamentoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPagamentoExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPagamentoExame))
            )
            .andExpect(status().isOk());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeUpdate);
        PagamentoExame testPagamentoExame = pagamentoExameList.get(pagamentoExameList.size() - 1);
        assertThat(testPagamentoExame.getFormapagamento()).isEqualTo(DEFAULT_FORMAPAGAMENTO);
        assertThat(testPagamentoExame.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
        assertThat(testPagamentoExame.getSeliquidado()).isEqualTo(UPDATED_SELIQUIDADO);
    }

    @Test
    @Transactional
    void fullUpdatePagamentoExameWithPatch() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        int databaseSizeBeforeUpdate = pagamentoExameRepository.findAll().size();

        // Update the pagamentoExame using partial update
        PagamentoExame partialUpdatedPagamentoExame = new PagamentoExame();
        partialUpdatedPagamentoExame.setId(pagamentoExame.getId());

        partialUpdatedPagamentoExame.formapagamento(UPDATED_FORMAPAGAMENTO).conteudo(UPDATED_CONTEUDO).seliquidado(UPDATED_SELIQUIDADO);

        restPagamentoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPagamentoExame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPagamentoExame))
            )
            .andExpect(status().isOk());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeUpdate);
        PagamentoExame testPagamentoExame = pagamentoExameList.get(pagamentoExameList.size() - 1);
        assertThat(testPagamentoExame.getFormapagamento()).isEqualTo(UPDATED_FORMAPAGAMENTO);
        assertThat(testPagamentoExame.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
        assertThat(testPagamentoExame.getSeliquidado()).isEqualTo(UPDATED_SELIQUIDADO);
    }

    @Test
    @Transactional
    void patchNonExistingPagamentoExame() throws Exception {
        int databaseSizeBeforeUpdate = pagamentoExameRepository.findAll().size();
        pagamentoExame.setId(count.incrementAndGet());

        // Create the PagamentoExame
        PagamentoExameDTO pagamentoExameDTO = pagamentoExameMapper.toDto(pagamentoExame);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagamentoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pagamentoExameDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pagamentoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPagamentoExame() throws Exception {
        int databaseSizeBeforeUpdate = pagamentoExameRepository.findAll().size();
        pagamentoExame.setId(count.incrementAndGet());

        // Create the PagamentoExame
        PagamentoExameDTO pagamentoExameDTO = pagamentoExameMapper.toDto(pagamentoExame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagamentoExameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pagamentoExameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPagamentoExame() throws Exception {
        int databaseSizeBeforeUpdate = pagamentoExameRepository.findAll().size();
        pagamentoExame.setId(count.incrementAndGet());

        // Create the PagamentoExame
        PagamentoExameDTO pagamentoExameDTO = pagamentoExameMapper.toDto(pagamentoExame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagamentoExameMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pagamentoExameDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PagamentoExame in the database
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePagamentoExame() throws Exception {
        // Initialize the database
        pagamentoExameRepository.saveAndFlush(pagamentoExame);

        int databaseSizeBeforeDelete = pagamentoExameRepository.findAll().size();

        // Delete the pagamentoExame
        restPagamentoExameMockMvc
            .perform(delete(ENTITY_API_URL_ID, pagamentoExame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PagamentoExame> pagamentoExameList = pagamentoExameRepository.findAll();
        assertThat(pagamentoExameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
