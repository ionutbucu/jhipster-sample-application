package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReportExecutionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReportExecution;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ReportExecutionRepository;
import com.mycompany.myapp.service.dto.ReportExecutionDTO;
import com.mycompany.myapp.service.mapper.ReportExecutionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ReportExecutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportExecutionResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_ERROR = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-executions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{rid}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportExecutionRepository reportExecutionRepository;

    @Autowired
    private ReportExecutionMapper reportExecutionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ReportExecution reportExecution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportExecution createEntity(EntityManager em) {
        ReportExecution reportExecution = new ReportExecution()
            .rid(UUID.randomUUID().toString())
            .date(DEFAULT_DATE)
            .error(DEFAULT_ERROR)
            .url(DEFAULT_URL)
            .user(DEFAULT_USER)
            .additionalInfo(DEFAULT_ADDITIONAL_INFO);
        return reportExecution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportExecution createUpdatedEntity(EntityManager em) {
        ReportExecution reportExecution = new ReportExecution()
            .rid(UUID.randomUUID().toString())
            .date(UPDATED_DATE)
            .error(UPDATED_ERROR)
            .url(UPDATED_URL)
            .user(UPDATED_USER)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);
        return reportExecution;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ReportExecution.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        reportExecution = createEntity(em);
    }

    @Test
    void createReportExecution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);
        var returnedReportExecutionDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReportExecutionDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ReportExecution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportExecution = reportExecutionMapper.toEntity(returnedReportExecutionDTO);
        assertReportExecutionUpdatableFieldsEquals(returnedReportExecution, getPersistedReportExecution(returnedReportExecution));
    }

    @Test
    void createReportExecutionWithExistingId() throws Exception {
        // Create the ReportExecution with an existing ID
        reportExecutionRepository.save(reportExecution).block();
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportExecution.setDate(null);

        // Create the ReportExecution, which fails.
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllReportExecutions() {
        // Initialize the database
        reportExecution.setRid(UUID.randomUUID().toString());
        reportExecutionRepository.save(reportExecution).block();

        // Get all the reportExecutionList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=rid,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].rid")
            .value(hasItem(reportExecution.getRid()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].error")
            .value(hasItem(DEFAULT_ERROR))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL))
            .jsonPath("$.[*].user")
            .value(hasItem(DEFAULT_USER))
            .jsonPath("$.[*].additionalInfo")
            .value(hasItem(DEFAULT_ADDITIONAL_INFO));
    }

    @Test
    void getReportExecution() {
        // Initialize the database
        reportExecution.setRid(UUID.randomUUID().toString());
        reportExecutionRepository.save(reportExecution).block();

        // Get the reportExecution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reportExecution.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.rid")
            .value(is(reportExecution.getRid()))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE.toString()))
            .jsonPath("$.error")
            .value(is(DEFAULT_ERROR))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL))
            .jsonPath("$.user")
            .value(is(DEFAULT_USER))
            .jsonPath("$.additionalInfo")
            .value(is(DEFAULT_ADDITIONAL_INFO));
    }

    @Test
    void getNonExistingReportExecution() {
        // Get the reportExecution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReportExecution() throws Exception {
        // Initialize the database
        reportExecution.setRid(UUID.randomUUID().toString());
        reportExecutionRepository.save(reportExecution).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportExecution
        ReportExecution updatedReportExecution = reportExecutionRepository.findById(reportExecution.getRid()).block();
        updatedReportExecution
            .date(UPDATED_DATE)
            .error(UPDATED_ERROR)
            .url(UPDATED_URL)
            .user(UPDATED_USER)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(updatedReportExecution);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportExecutionDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportExecutionToMatchAllProperties(updatedReportExecution);
    }

    @Test
    void putNonExistingReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setRid(UUID.randomUUID().toString());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportExecutionDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setRid(UUID.randomUUID().toString());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setRid(UUID.randomUUID().toString());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportExecutionWithPatch() throws Exception {
        // Initialize the database
        reportExecution.setRid(UUID.randomUUID().toString());
        reportExecutionRepository.save(reportExecution).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportExecution using partial update
        ReportExecution partialUpdatedReportExecution = new ReportExecution();
        partialUpdatedReportExecution.setRid(reportExecution.getRid());

        partialUpdatedReportExecution.error(UPDATED_ERROR).url(UPDATED_URL).user(UPDATED_USER).additionalInfo(UPDATED_ADDITIONAL_INFO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportExecution.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportExecution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportExecutionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportExecution, reportExecution),
            getPersistedReportExecution(reportExecution)
        );
    }

    @Test
    void fullUpdateReportExecutionWithPatch() throws Exception {
        // Initialize the database
        reportExecution.setRid(UUID.randomUUID().toString());
        reportExecutionRepository.save(reportExecution).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportExecution using partial update
        ReportExecution partialUpdatedReportExecution = new ReportExecution();
        partialUpdatedReportExecution.setRid(reportExecution.getRid());

        partialUpdatedReportExecution
            .date(UPDATED_DATE)
            .error(UPDATED_ERROR)
            .url(UPDATED_URL)
            .user(UPDATED_USER)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportExecution.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportExecution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportExecutionUpdatableFieldsEquals(
            partialUpdatedReportExecution,
            getPersistedReportExecution(partialUpdatedReportExecution)
        );
    }

    @Test
    void patchNonExistingReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setRid(UUID.randomUUID().toString());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportExecutionDTO.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setRid(UUID.randomUUID().toString());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setRid(UUID.randomUUID().toString());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportExecutionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReportExecution() {
        // Initialize the database
        reportExecution.setRid(UUID.randomUUID().toString());
        reportExecutionRepository.save(reportExecution).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportExecution
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reportExecution.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportExecutionRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ReportExecution getPersistedReportExecution(ReportExecution reportExecution) {
        return reportExecutionRepository.findById(reportExecution.getRid()).block();
    }

    protected void assertPersistedReportExecutionToMatchAllProperties(ReportExecution expectedReportExecution) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportExecutionAllPropertiesEquals(expectedReportExecution, getPersistedReportExecution(expectedReportExecution));
        assertReportExecutionUpdatableFieldsEquals(expectedReportExecution, getPersistedReportExecution(expectedReportExecution));
    }

    protected void assertPersistedReportExecutionToMatchUpdatableProperties(ReportExecution expectedReportExecution) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportExecutionAllUpdatablePropertiesEquals(expectedReportExecution, getPersistedReportExecution(expectedReportExecution));
        assertReportExecutionUpdatableFieldsEquals(expectedReportExecution, getPersistedReportExecution(expectedReportExecution));
    }
}
