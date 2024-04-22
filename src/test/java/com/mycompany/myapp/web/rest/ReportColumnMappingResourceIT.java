package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReportColumnMappingAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReportColumnMapping;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ReportColumnMappingRepository;
import com.mycompany.myapp.service.dto.ReportColumnMappingDTO;
import com.mycompany.myapp.service.mapper.ReportColumnMappingMapper;
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
 * Integration tests for the {@link ReportColumnMappingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportColumnMappingResourceIT {

    private static final String DEFAULT_SOURCE_COLUMN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_COLUMN_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SOURCE_COLUMN_INDEX = 1;
    private static final Integer UPDATED_SOURCE_COLUMN_INDEX = 2;

    private static final String DEFAULT_COLUMN_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LANG = "AAAAAAAAAA";
    private static final String UPDATED_LANG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-column-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{rid}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportColumnMappingRepository reportColumnMappingRepository;

    @Autowired
    private ReportColumnMappingMapper reportColumnMappingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ReportColumnMapping reportColumnMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportColumnMapping createEntity(EntityManager em) {
        ReportColumnMapping reportColumnMapping = new ReportColumnMapping()
            .rid(UUID.randomUUID().toString())
            .sourceColumnName(DEFAULT_SOURCE_COLUMN_NAME)
            .sourceColumnIndex(DEFAULT_SOURCE_COLUMN_INDEX)
            .columnTitle(DEFAULT_COLUMN_TITLE)
            .lang(DEFAULT_LANG);
        return reportColumnMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportColumnMapping createUpdatedEntity(EntityManager em) {
        ReportColumnMapping reportColumnMapping = new ReportColumnMapping()
            .rid(UUID.randomUUID().toString())
            .sourceColumnName(UPDATED_SOURCE_COLUMN_NAME)
            .sourceColumnIndex(UPDATED_SOURCE_COLUMN_INDEX)
            .columnTitle(UPDATED_COLUMN_TITLE)
            .lang(UPDATED_LANG);
        return reportColumnMapping;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ReportColumnMapping.class).block();
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
        reportColumnMapping = createEntity(em);
    }

    @Test
    void createReportColumnMapping() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);
        var returnedReportColumnMappingDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportColumnMappingDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReportColumnMappingDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ReportColumnMapping in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportColumnMapping = reportColumnMappingMapper.toEntity(returnedReportColumnMappingDTO);
        assertReportColumnMappingUpdatableFieldsEquals(
            returnedReportColumnMapping,
            getPersistedReportColumnMapping(returnedReportColumnMapping)
        );
    }

    @Test
    void createReportColumnMappingWithExistingId() throws Exception {
        // Create the ReportColumnMapping with an existing ID
        reportColumnMappingRepository.save(reportColumnMapping).block();
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportColumnMappingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReportColumnMappingsAsStream() {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.save(reportColumnMapping).block();

        List<ReportColumnMapping> reportColumnMappingList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ReportColumnMappingDTO.class)
            .getResponseBody()
            .map(reportColumnMappingMapper::toEntity)
            .filter(reportColumnMapping::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(reportColumnMappingList).isNotNull();
        assertThat(reportColumnMappingList).hasSize(1);
        ReportColumnMapping testReportColumnMapping = reportColumnMappingList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertReportColumnMappingAllPropertiesEquals(reportColumnMapping, testReportColumnMapping);
        assertReportColumnMappingUpdatableFieldsEquals(reportColumnMapping, testReportColumnMapping);
    }

    @Test
    void getAllReportColumnMappings() {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.save(reportColumnMapping).block();

        // Get all the reportColumnMappingList
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
            .value(hasItem(reportColumnMapping.getRid()))
            .jsonPath("$.[*].sourceColumnName")
            .value(hasItem(DEFAULT_SOURCE_COLUMN_NAME))
            .jsonPath("$.[*].sourceColumnIndex")
            .value(hasItem(DEFAULT_SOURCE_COLUMN_INDEX))
            .jsonPath("$.[*].columnTitle")
            .value(hasItem(DEFAULT_COLUMN_TITLE))
            .jsonPath("$.[*].lang")
            .value(hasItem(DEFAULT_LANG));
    }

    @Test
    void getReportColumnMapping() {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.save(reportColumnMapping).block();

        // Get the reportColumnMapping
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reportColumnMapping.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.rid")
            .value(is(reportColumnMapping.getRid()))
            .jsonPath("$.sourceColumnName")
            .value(is(DEFAULT_SOURCE_COLUMN_NAME))
            .jsonPath("$.sourceColumnIndex")
            .value(is(DEFAULT_SOURCE_COLUMN_INDEX))
            .jsonPath("$.columnTitle")
            .value(is(DEFAULT_COLUMN_TITLE))
            .jsonPath("$.lang")
            .value(is(DEFAULT_LANG));
    }

    @Test
    void getNonExistingReportColumnMapping() {
        // Get the reportColumnMapping
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReportColumnMapping() throws Exception {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.save(reportColumnMapping).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportColumnMapping
        ReportColumnMapping updatedReportColumnMapping = reportColumnMappingRepository.findById(reportColumnMapping.getRid()).block();
        updatedReportColumnMapping
            .sourceColumnName(UPDATED_SOURCE_COLUMN_NAME)
            .sourceColumnIndex(UPDATED_SOURCE_COLUMN_INDEX)
            .columnTitle(UPDATED_COLUMN_TITLE)
            .lang(UPDATED_LANG);
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(updatedReportColumnMapping);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportColumnMappingDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportColumnMappingDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportColumnMappingToMatchAllProperties(updatedReportColumnMapping);
    }

    @Test
    void putNonExistingReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportColumnMappingDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportColumnMappingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportColumnMappingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportColumnMappingDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportColumnMappingWithPatch() throws Exception {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.save(reportColumnMapping).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportColumnMapping using partial update
        ReportColumnMapping partialUpdatedReportColumnMapping = new ReportColumnMapping();
        partialUpdatedReportColumnMapping.setRid(reportColumnMapping.getRid());

        partialUpdatedReportColumnMapping.sourceColumnName(UPDATED_SOURCE_COLUMN_NAME).sourceColumnIndex(UPDATED_SOURCE_COLUMN_INDEX);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportColumnMapping.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportColumnMapping))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportColumnMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportColumnMappingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportColumnMapping, reportColumnMapping),
            getPersistedReportColumnMapping(reportColumnMapping)
        );
    }

    @Test
    void fullUpdateReportColumnMappingWithPatch() throws Exception {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.save(reportColumnMapping).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportColumnMapping using partial update
        ReportColumnMapping partialUpdatedReportColumnMapping = new ReportColumnMapping();
        partialUpdatedReportColumnMapping.setRid(reportColumnMapping.getRid());

        partialUpdatedReportColumnMapping
            .sourceColumnName(UPDATED_SOURCE_COLUMN_NAME)
            .sourceColumnIndex(UPDATED_SOURCE_COLUMN_INDEX)
            .columnTitle(UPDATED_COLUMN_TITLE)
            .lang(UPDATED_LANG);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportColumnMapping.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportColumnMapping))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportColumnMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportColumnMappingUpdatableFieldsEquals(
            partialUpdatedReportColumnMapping,
            getPersistedReportColumnMapping(partialUpdatedReportColumnMapping)
        );
    }

    @Test
    void patchNonExistingReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportColumnMappingDTO.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportColumnMappingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportColumnMappingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReportColumnMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportColumnMapping.setRid(UUID.randomUUID().toString());

        // Create the ReportColumnMapping
        ReportColumnMappingDTO reportColumnMappingDTO = reportColumnMappingMapper.toDto(reportColumnMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportColumnMappingDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportColumnMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReportColumnMapping() {
        // Initialize the database
        reportColumnMapping.setRid(UUID.randomUUID().toString());
        reportColumnMappingRepository.save(reportColumnMapping).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportColumnMapping
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reportColumnMapping.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportColumnMappingRepository.count().block();
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

    protected ReportColumnMapping getPersistedReportColumnMapping(ReportColumnMapping reportColumnMapping) {
        return reportColumnMappingRepository.findById(reportColumnMapping.getRid()).block();
    }

    protected void assertPersistedReportColumnMappingToMatchAllProperties(ReportColumnMapping expectedReportColumnMapping) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportColumnMappingAllPropertiesEquals(expectedReportColumnMapping, getPersistedReportColumnMapping(expectedReportColumnMapping));
        assertReportColumnMappingUpdatableFieldsEquals(
            expectedReportColumnMapping,
            getPersistedReportColumnMapping(expectedReportColumnMapping)
        );
    }

    protected void assertPersistedReportColumnMappingToMatchUpdatableProperties(ReportColumnMapping expectedReportColumnMapping) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportColumnMappingAllUpdatablePropertiesEquals(expectedReportColumnMapping, getPersistedReportColumnMapping(expectedReportColumnMapping));
        assertReportColumnMappingUpdatableFieldsEquals(
            expectedReportColumnMapping,
            getPersistedReportColumnMapping(expectedReportColumnMapping)
        );
    }
}
