package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReportParamAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReportParam;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ReportParamRepository;
import com.mycompany.myapp.service.dto.ReportParamDTO;
import com.mycompany.myapp.service.mapper.ReportParamMapper;
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
 * Integration tests for the {@link ReportParamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportParamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CONVERSION_RULE = "AAAAAAAAAA";
    private static final String UPDATED_CONVERSION_RULE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-params";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{rid}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportParamRepository reportParamRepository;

    @Autowired
    private ReportParamMapper reportParamMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ReportParam reportParam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportParam createEntity(EntityManager em) {
        ReportParam reportParam = new ReportParam()
            .rid(UUID.randomUUID().toString())
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE)
            .conversionRule(DEFAULT_CONVERSION_RULE);
        return reportParam;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportParam createUpdatedEntity(EntityManager em) {
        ReportParam reportParam = new ReportParam()
            .rid(UUID.randomUUID().toString())
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .conversionRule(UPDATED_CONVERSION_RULE);
        return reportParam;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ReportParam.class).block();
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
        reportParam = createEntity(em);
    }

    @Test
    void createReportParam() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);
        var returnedReportParamDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportParamDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReportParamDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ReportParam in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportParam = reportParamMapper.toEntity(returnedReportParamDTO);
        assertReportParamUpdatableFieldsEquals(returnedReportParam, getPersistedReportParam(returnedReportParam));
    }

    @Test
    void createReportParamWithExistingId() throws Exception {
        // Create the ReportParam with an existing ID
        reportParamRepository.save(reportParam).block();
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportParamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReportParamsAsStream() {
        // Initialize the database
        reportParam.setRid(UUID.randomUUID().toString());
        reportParamRepository.save(reportParam).block();

        List<ReportParam> reportParamList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ReportParamDTO.class)
            .getResponseBody()
            .map(reportParamMapper::toEntity)
            .filter(reportParam::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(reportParamList).isNotNull();
        assertThat(reportParamList).hasSize(1);
        ReportParam testReportParam = reportParamList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertReportParamAllPropertiesEquals(reportParam, testReportParam);
        assertReportParamUpdatableFieldsEquals(reportParam, testReportParam);
    }

    @Test
    void getAllReportParams() {
        // Initialize the database
        reportParam.setRid(UUID.randomUUID().toString());
        reportParamRepository.save(reportParam).block();

        // Get all the reportParamList
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
            .value(hasItem(reportParam.getRid()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE))
            .jsonPath("$.[*].conversionRule")
            .value(hasItem(DEFAULT_CONVERSION_RULE));
    }

    @Test
    void getReportParam() {
        // Initialize the database
        reportParam.setRid(UUID.randomUUID().toString());
        reportParamRepository.save(reportParam).block();

        // Get the reportParam
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reportParam.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.rid")
            .value(is(reportParam.getRid()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.value")
            .value(is(DEFAULT_VALUE))
            .jsonPath("$.conversionRule")
            .value(is(DEFAULT_CONVERSION_RULE));
    }

    @Test
    void getNonExistingReportParam() {
        // Get the reportParam
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReportParam() throws Exception {
        // Initialize the database
        reportParam.setRid(UUID.randomUUID().toString());
        reportParamRepository.save(reportParam).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportParam
        ReportParam updatedReportParam = reportParamRepository.findById(reportParam.getRid()).block();
        updatedReportParam.name(UPDATED_NAME).type(UPDATED_TYPE).value(UPDATED_VALUE).conversionRule(UPDATED_CONVERSION_RULE);
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(updatedReportParam);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportParamDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportParamDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportParamToMatchAllProperties(updatedReportParam);
    }

    @Test
    void putNonExistingReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setRid(UUID.randomUUID().toString());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportParamDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportParamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setRid(UUID.randomUUID().toString());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportParamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setRid(UUID.randomUUID().toString());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportParamDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportParamWithPatch() throws Exception {
        // Initialize the database
        reportParam.setRid(UUID.randomUUID().toString());
        reportParamRepository.save(reportParam).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportParam using partial update
        ReportParam partialUpdatedReportParam = new ReportParam();
        partialUpdatedReportParam.setRid(reportParam.getRid());

        partialUpdatedReportParam.name(UPDATED_NAME).conversionRule(UPDATED_CONVERSION_RULE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportParam.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportParam))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportParam in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportParamUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportParam, reportParam),
            getPersistedReportParam(reportParam)
        );
    }

    @Test
    void fullUpdateReportParamWithPatch() throws Exception {
        // Initialize the database
        reportParam.setRid(UUID.randomUUID().toString());
        reportParamRepository.save(reportParam).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportParam using partial update
        ReportParam partialUpdatedReportParam = new ReportParam();
        partialUpdatedReportParam.setRid(reportParam.getRid());

        partialUpdatedReportParam.name(UPDATED_NAME).type(UPDATED_TYPE).value(UPDATED_VALUE).conversionRule(UPDATED_CONVERSION_RULE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportParam.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportParam))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportParam in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportParamUpdatableFieldsEquals(partialUpdatedReportParam, getPersistedReportParam(partialUpdatedReportParam));
    }

    @Test
    void patchNonExistingReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setRid(UUID.randomUUID().toString());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportParamDTO.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportParamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setRid(UUID.randomUUID().toString());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportParamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReportParam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportParam.setRid(UUID.randomUUID().toString());

        // Create the ReportParam
        ReportParamDTO reportParamDTO = reportParamMapper.toDto(reportParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportParamDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportParam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReportParam() {
        // Initialize the database
        reportParam.setRid(UUID.randomUUID().toString());
        reportParamRepository.save(reportParam).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportParam
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reportParam.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportParamRepository.count().block();
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

    protected ReportParam getPersistedReportParam(ReportParam reportParam) {
        return reportParamRepository.findById(reportParam.getRid()).block();
    }

    protected void assertPersistedReportParamToMatchAllProperties(ReportParam expectedReportParam) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportParamAllPropertiesEquals(expectedReportParam, getPersistedReportParam(expectedReportParam));
        assertReportParamUpdatableFieldsEquals(expectedReportParam, getPersistedReportParam(expectedReportParam));
    }

    protected void assertPersistedReportParamToMatchUpdatableProperties(ReportParam expectedReportParam) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportParamAllUpdatablePropertiesEquals(expectedReportParam, getPersistedReportParam(expectedReportParam));
        assertReportParamUpdatableFieldsEquals(expectedReportParam, getPersistedReportParam(expectedReportParam));
    }
}
