package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReportDataSourceAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReportDataSource;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ReportDataSourceRepository;
import com.mycompany.myapp.service.dto.ReportDataSourceDTO;
import com.mycompany.myapp.service.mapper.ReportDataSourceMapper;
import java.time.Duration;
import java.util.List;
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
 * Integration tests for the {@link ReportDataSourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportDataSourceResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-data-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{rid}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportDataSourceRepository reportDataSourceRepository;

    @Autowired
    private ReportDataSourceMapper reportDataSourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ReportDataSource reportDataSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDataSource createEntity(EntityManager em) {
        ReportDataSource reportDataSource = new ReportDataSource()
            .rid(UUID.randomUUID().toString())
            .type(DEFAULT_TYPE)
            .url(DEFAULT_URL)
            .user(DEFAULT_USER)
            .password(DEFAULT_PASSWORD);
        return reportDataSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDataSource createUpdatedEntity(EntityManager em) {
        ReportDataSource reportDataSource = new ReportDataSource()
            .rid(UUID.randomUUID().toString())
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .user(UPDATED_USER)
            .password(UPDATED_PASSWORD);
        return reportDataSource;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ReportDataSource.class).block();
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
        reportDataSource = createEntity(em);
    }

    @Test
    void createReportDataSource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);
        var returnedReportDataSourceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDataSourceDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReportDataSourceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ReportDataSource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportDataSource = reportDataSourceMapper.toEntity(returnedReportDataSourceDTO);
        assertReportDataSourceUpdatableFieldsEquals(returnedReportDataSource, getPersistedReportDataSource(returnedReportDataSource));
    }

    @Test
    void createReportDataSourceWithExistingId() throws Exception {
        // Create the ReportDataSource with an existing ID
        reportDataSourceRepository.save(reportDataSource).block();
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDataSourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReportDataSourcesAsStream() {
        // Initialize the database
        reportDataSource.setRid(UUID.randomUUID().toString());
        reportDataSourceRepository.save(reportDataSource).block();

        List<ReportDataSource> reportDataSourceList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ReportDataSourceDTO.class)
            .getResponseBody()
            .map(reportDataSourceMapper::toEntity)
            .filter(reportDataSource::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(reportDataSourceList).isNotNull();
        assertThat(reportDataSourceList).hasSize(1);
        ReportDataSource testReportDataSource = reportDataSourceList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertReportDataSourceAllPropertiesEquals(reportDataSource, testReportDataSource);
        assertReportDataSourceUpdatableFieldsEquals(reportDataSource, testReportDataSource);
    }

    @Test
    void getAllReportDataSources() {
        // Initialize the database
        reportDataSource.setRid(UUID.randomUUID().toString());
        reportDataSourceRepository.save(reportDataSource).block();

        // Get all the reportDataSourceList
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
            .value(hasItem(reportDataSource.getRid()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL))
            .jsonPath("$.[*].user")
            .value(hasItem(DEFAULT_USER))
            .jsonPath("$.[*].password")
            .value(hasItem(DEFAULT_PASSWORD));
    }

    @Test
    void getReportDataSource() {
        // Initialize the database
        reportDataSource.setRid(UUID.randomUUID().toString());
        reportDataSourceRepository.save(reportDataSource).block();

        // Get the reportDataSource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reportDataSource.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.rid")
            .value(is(reportDataSource.getRid()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL))
            .jsonPath("$.user")
            .value(is(DEFAULT_USER))
            .jsonPath("$.password")
            .value(is(DEFAULT_PASSWORD));
    }

    @Test
    void getNonExistingReportDataSource() {
        // Get the reportDataSource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReportDataSource() throws Exception {
        // Initialize the database
        reportDataSource.setRid(UUID.randomUUID().toString());
        reportDataSourceRepository.save(reportDataSource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDataSource
        ReportDataSource updatedReportDataSource = reportDataSourceRepository.findById(reportDataSource.getRid()).block();
        updatedReportDataSource.type(UPDATED_TYPE).url(UPDATED_URL).user(UPDATED_USER).password(UPDATED_PASSWORD);
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(updatedReportDataSource);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDataSourceDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDataSourceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportDataSourceToMatchAllProperties(updatedReportDataSource);
    }

    @Test
    void putNonExistingReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setRid(UUID.randomUUID().toString());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDataSourceDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDataSourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setRid(UUID.randomUUID().toString());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDataSourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setRid(UUID.randomUUID().toString());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDataSourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportDataSourceWithPatch() throws Exception {
        // Initialize the database
        reportDataSource.setRid(UUID.randomUUID().toString());
        reportDataSourceRepository.save(reportDataSource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDataSource using partial update
        ReportDataSource partialUpdatedReportDataSource = new ReportDataSource();
        partialUpdatedReportDataSource.setRid(reportDataSource.getRid());

        partialUpdatedReportDataSource.user(UPDATED_USER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportDataSource.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportDataSource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportDataSource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDataSourceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportDataSource, reportDataSource),
            getPersistedReportDataSource(reportDataSource)
        );
    }

    @Test
    void fullUpdateReportDataSourceWithPatch() throws Exception {
        // Initialize the database
        reportDataSource.setRid(UUID.randomUUID().toString());
        reportDataSourceRepository.save(reportDataSource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDataSource using partial update
        ReportDataSource partialUpdatedReportDataSource = new ReportDataSource();
        partialUpdatedReportDataSource.setRid(reportDataSource.getRid());

        partialUpdatedReportDataSource.type(UPDATED_TYPE).url(UPDATED_URL).user(UPDATED_USER).password(UPDATED_PASSWORD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportDataSource.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportDataSource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportDataSource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDataSourceUpdatableFieldsEquals(
            partialUpdatedReportDataSource,
            getPersistedReportDataSource(partialUpdatedReportDataSource)
        );
    }

    @Test
    void patchNonExistingReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setRid(UUID.randomUUID().toString());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportDataSourceDTO.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDataSourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setRid(UUID.randomUUID().toString());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDataSourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReportDataSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDataSource.setRid(UUID.randomUUID().toString());

        // Create the ReportDataSource
        ReportDataSourceDTO reportDataSourceDTO = reportDataSourceMapper.toDto(reportDataSource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDataSourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportDataSource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReportDataSource() {
        // Initialize the database
        reportDataSource.setRid(UUID.randomUUID().toString());
        reportDataSourceRepository.save(reportDataSource).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportDataSource
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reportDataSource.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportDataSourceRepository.count().block();
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

    protected ReportDataSource getPersistedReportDataSource(ReportDataSource reportDataSource) {
        return reportDataSourceRepository.findById(reportDataSource.getRid()).block();
    }

    protected void assertPersistedReportDataSourceToMatchAllProperties(ReportDataSource expectedReportDataSource) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportDataSourceAllPropertiesEquals(expectedReportDataSource, getPersistedReportDataSource(expectedReportDataSource));
        assertReportDataSourceUpdatableFieldsEquals(expectedReportDataSource, getPersistedReportDataSource(expectedReportDataSource));
    }

    protected void assertPersistedReportDataSourceToMatchUpdatableProperties(ReportDataSource expectedReportDataSource) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportDataSourceAllUpdatablePropertiesEquals(expectedReportDataSource, getPersistedReportDataSource(expectedReportDataSource));
        assertReportDataSourceUpdatableFieldsEquals(expectedReportDataSource, getPersistedReportDataSource(expectedReportDataSource));
    }
}
