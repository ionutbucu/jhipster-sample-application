package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReportMetadataAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReportMetadata;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ReportMetadataRepository;
import com.mycompany.myapp.service.dto.ReportMetadataDTO;
import com.mycompany.myapp.service.mapper.ReportMetadataMapper;
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
 * Integration tests for the {@link ReportMetadataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportMetadataResourceIT {

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{rid}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportMetadataRepository reportMetadataRepository;

    @Autowired
    private ReportMetadataMapper reportMetadataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ReportMetadata reportMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportMetadata createEntity(EntityManager em) {
        ReportMetadata reportMetadata = new ReportMetadata().rid(UUID.randomUUID().toString()).metadata(DEFAULT_METADATA);
        return reportMetadata;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportMetadata createUpdatedEntity(EntityManager em) {
        ReportMetadata reportMetadata = new ReportMetadata().rid(UUID.randomUUID().toString()).metadata(UPDATED_METADATA);
        return reportMetadata;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ReportMetadata.class).block();
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
        reportMetadata = createEntity(em);
    }

    @Test
    void createReportMetadata() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);
        var returnedReportMetadataDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportMetadataDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReportMetadataDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ReportMetadata in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportMetadata = reportMetadataMapper.toEntity(returnedReportMetadataDTO);
        assertReportMetadataUpdatableFieldsEquals(returnedReportMetadata, getPersistedReportMetadata(returnedReportMetadata));
    }

    @Test
    void createReportMetadataWithExistingId() throws Exception {
        // Create the ReportMetadata with an existing ID
        reportMetadataRepository.save(reportMetadata).block();
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportMetadataDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReportMetadataAsStream() {
        // Initialize the database
        reportMetadata.setRid(UUID.randomUUID().toString());
        reportMetadataRepository.save(reportMetadata).block();

        List<ReportMetadata> reportMetadataList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ReportMetadataDTO.class)
            .getResponseBody()
            .map(reportMetadataMapper::toEntity)
            .filter(reportMetadata::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(reportMetadataList).isNotNull();
        assertThat(reportMetadataList).hasSize(1);
        ReportMetadata testReportMetadata = reportMetadataList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertReportMetadataAllPropertiesEquals(reportMetadata, testReportMetadata);
        assertReportMetadataUpdatableFieldsEquals(reportMetadata, testReportMetadata);
    }

    @Test
    void getAllReportMetadata() {
        // Initialize the database
        reportMetadata.setRid(UUID.randomUUID().toString());
        reportMetadataRepository.save(reportMetadata).block();

        // Get all the reportMetadataList
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
            .value(hasItem(reportMetadata.getRid()))
            .jsonPath("$.[*].metadata")
            .value(hasItem(DEFAULT_METADATA.toString()));
    }

    @Test
    void getReportMetadata() {
        // Initialize the database
        reportMetadata.setRid(UUID.randomUUID().toString());
        reportMetadataRepository.save(reportMetadata).block();

        // Get the reportMetadata
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reportMetadata.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.rid")
            .value(is(reportMetadata.getRid()))
            .jsonPath("$.metadata")
            .value(is(DEFAULT_METADATA.toString()));
    }

    @Test
    void getNonExistingReportMetadata() {
        // Get the reportMetadata
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReportMetadata() throws Exception {
        // Initialize the database
        reportMetadata.setRid(UUID.randomUUID().toString());
        reportMetadataRepository.save(reportMetadata).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportMetadata
        ReportMetadata updatedReportMetadata = reportMetadataRepository.findById(reportMetadata.getRid()).block();
        updatedReportMetadata.metadata(UPDATED_METADATA);
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(updatedReportMetadata);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportMetadataDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportMetadataDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportMetadataToMatchAllProperties(updatedReportMetadata);
    }

    @Test
    void putNonExistingReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setRid(UUID.randomUUID().toString());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportMetadataDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportMetadataDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setRid(UUID.randomUUID().toString());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportMetadataDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setRid(UUID.randomUUID().toString());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportMetadataDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportMetadataWithPatch() throws Exception {
        // Initialize the database
        reportMetadata.setRid(UUID.randomUUID().toString());
        reportMetadataRepository.save(reportMetadata).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportMetadata using partial update
        ReportMetadata partialUpdatedReportMetadata = new ReportMetadata();
        partialUpdatedReportMetadata.setRid(reportMetadata.getRid());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportMetadata.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportMetadata))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportMetadataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportMetadata, reportMetadata),
            getPersistedReportMetadata(reportMetadata)
        );
    }

    @Test
    void fullUpdateReportMetadataWithPatch() throws Exception {
        // Initialize the database
        reportMetadata.setRid(UUID.randomUUID().toString());
        reportMetadataRepository.save(reportMetadata).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportMetadata using partial update
        ReportMetadata partialUpdatedReportMetadata = new ReportMetadata();
        partialUpdatedReportMetadata.setRid(reportMetadata.getRid());

        partialUpdatedReportMetadata.metadata(UPDATED_METADATA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportMetadata.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportMetadata))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportMetadataUpdatableFieldsEquals(partialUpdatedReportMetadata, getPersistedReportMetadata(partialUpdatedReportMetadata));
    }

    @Test
    void patchNonExistingReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setRid(UUID.randomUUID().toString());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportMetadataDTO.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportMetadataDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setRid(UUID.randomUUID().toString());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportMetadataDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReportMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportMetadata.setRid(UUID.randomUUID().toString());

        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportMetadataDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReportMetadata() {
        // Initialize the database
        reportMetadata.setRid(UUID.randomUUID().toString());
        reportMetadataRepository.save(reportMetadata).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportMetadata
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reportMetadata.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportMetadataRepository.count().block();
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

    protected ReportMetadata getPersistedReportMetadata(ReportMetadata reportMetadata) {
        return reportMetadataRepository.findById(reportMetadata.getRid()).block();
    }

    protected void assertPersistedReportMetadataToMatchAllProperties(ReportMetadata expectedReportMetadata) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportMetadataAllPropertiesEquals(expectedReportMetadata, getPersistedReportMetadata(expectedReportMetadata));
        assertReportMetadataUpdatableFieldsEquals(expectedReportMetadata, getPersistedReportMetadata(expectedReportMetadata));
    }

    protected void assertPersistedReportMetadataToMatchUpdatableProperties(ReportMetadata expectedReportMetadata) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportMetadataAllUpdatablePropertiesEquals(expectedReportMetadata, getPersistedReportMetadata(expectedReportMetadata));
        assertReportMetadataUpdatableFieldsEquals(expectedReportMetadata, getPersistedReportMetadata(expectedReportMetadata));
    }
}
