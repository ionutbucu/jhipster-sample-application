package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReportDistributionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReportDistribution;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ReportDistributionRepository;
import com.mycompany.myapp.service.dto.ReportDistributionDTO;
import com.mycompany.myapp.service.mapper.ReportDistributionMapper;
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
 * Integration tests for the {@link ReportDistributionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportDistributionResourceIT {

    private static final String DEFAULT_EMAIL = "cz@6ad=fo";
    private static final String UPDATED_EMAIL = "[<<3/@j-3_0O";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-distributions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{rid}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportDistributionRepository reportDistributionRepository;

    @Autowired
    private ReportDistributionMapper reportDistributionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ReportDistribution reportDistribution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDistribution createEntity(EntityManager em) {
        ReportDistribution reportDistribution = new ReportDistribution()
            .rid(UUID.randomUUID().toString())
            .email(DEFAULT_EMAIL)
            .description(DEFAULT_DESCRIPTION);
        return reportDistribution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDistribution createUpdatedEntity(EntityManager em) {
        ReportDistribution reportDistribution = new ReportDistribution()
            .rid(UUID.randomUUID().toString())
            .email(UPDATED_EMAIL)
            .description(UPDATED_DESCRIPTION);
        return reportDistribution;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ReportDistribution.class).block();
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
        reportDistribution = createEntity(em);
    }

    @Test
    void createReportDistribution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);
        var returnedReportDistributionDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReportDistributionDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ReportDistribution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportDistribution = reportDistributionMapper.toEntity(returnedReportDistributionDTO);
        assertReportDistributionUpdatableFieldsEquals(
            returnedReportDistribution,
            getPersistedReportDistribution(returnedReportDistribution)
        );
    }

    @Test
    void createReportDistributionWithExistingId() throws Exception {
        // Create the ReportDistribution with an existing ID
        reportDistributionRepository.save(reportDistribution).block();
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportDistribution.setEmail(null);

        // Create the ReportDistribution, which fails.
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllReportDistributionsAsStream() {
        // Initialize the database
        reportDistribution.setRid(UUID.randomUUID().toString());
        reportDistributionRepository.save(reportDistribution).block();

        List<ReportDistribution> reportDistributionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ReportDistributionDTO.class)
            .getResponseBody()
            .map(reportDistributionMapper::toEntity)
            .filter(reportDistribution::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(reportDistributionList).isNotNull();
        assertThat(reportDistributionList).hasSize(1);
        ReportDistribution testReportDistribution = reportDistributionList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertReportDistributionAllPropertiesEquals(reportDistribution, testReportDistribution);
        assertReportDistributionUpdatableFieldsEquals(reportDistribution, testReportDistribution);
    }

    @Test
    void getAllReportDistributions() {
        // Initialize the database
        reportDistribution.setRid(UUID.randomUUID().toString());
        reportDistributionRepository.save(reportDistribution).block();

        // Get all the reportDistributionList
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
            .value(hasItem(reportDistribution.getRid()))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getReportDistribution() {
        // Initialize the database
        reportDistribution.setRid(UUID.randomUUID().toString());
        reportDistributionRepository.save(reportDistribution).block();

        // Get the reportDistribution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reportDistribution.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.rid")
            .value(is(reportDistribution.getRid()))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingReportDistribution() {
        // Get the reportDistribution
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReportDistribution() throws Exception {
        // Initialize the database
        reportDistribution.setRid(UUID.randomUUID().toString());
        reportDistributionRepository.save(reportDistribution).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDistribution
        ReportDistribution updatedReportDistribution = reportDistributionRepository.findById(reportDistribution.getRid()).block();
        updatedReportDistribution.email(UPDATED_EMAIL).description(UPDATED_DESCRIPTION);
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(updatedReportDistribution);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDistributionDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportDistributionToMatchAllProperties(updatedReportDistribution);
    }

    @Test
    void putNonExistingReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setRid(UUID.randomUUID().toString());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDistributionDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setRid(UUID.randomUUID().toString());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setRid(UUID.randomUUID().toString());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportDistributionWithPatch() throws Exception {
        // Initialize the database
        reportDistribution.setRid(UUID.randomUUID().toString());
        reportDistributionRepository.save(reportDistribution).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDistribution using partial update
        ReportDistribution partialUpdatedReportDistribution = new ReportDistribution();
        partialUpdatedReportDistribution.setRid(reportDistribution.getRid());

        partialUpdatedReportDistribution.email(UPDATED_EMAIL).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportDistribution.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportDistribution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportDistribution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDistributionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportDistribution, reportDistribution),
            getPersistedReportDistribution(reportDistribution)
        );
    }

    @Test
    void fullUpdateReportDistributionWithPatch() throws Exception {
        // Initialize the database
        reportDistribution.setRid(UUID.randomUUID().toString());
        reportDistributionRepository.save(reportDistribution).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDistribution using partial update
        ReportDistribution partialUpdatedReportDistribution = new ReportDistribution();
        partialUpdatedReportDistribution.setRid(reportDistribution.getRid());

        partialUpdatedReportDistribution.email(UPDATED_EMAIL).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportDistribution.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportDistribution))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportDistribution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDistributionUpdatableFieldsEquals(
            partialUpdatedReportDistribution,
            getPersistedReportDistribution(partialUpdatedReportDistribution)
        );
    }

    @Test
    void patchNonExistingReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setRid(UUID.randomUUID().toString());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportDistributionDTO.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setRid(UUID.randomUUID().toString());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReportDistribution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDistribution.setRid(UUID.randomUUID().toString());

        // Create the ReportDistribution
        ReportDistributionDTO reportDistributionDTO = reportDistributionMapper.toDto(reportDistribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDistributionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportDistribution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReportDistribution() {
        // Initialize the database
        reportDistribution.setRid(UUID.randomUUID().toString());
        reportDistributionRepository.save(reportDistribution).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportDistribution
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reportDistribution.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportDistributionRepository.count().block();
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

    protected ReportDistribution getPersistedReportDistribution(ReportDistribution reportDistribution) {
        return reportDistributionRepository.findById(reportDistribution.getRid()).block();
    }

    protected void assertPersistedReportDistributionToMatchAllProperties(ReportDistribution expectedReportDistribution) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportDistributionAllPropertiesEquals(expectedReportDistribution, getPersistedReportDistribution(expectedReportDistribution));
        assertReportDistributionUpdatableFieldsEquals(
            expectedReportDistribution,
            getPersistedReportDistribution(expectedReportDistribution)
        );
    }

    protected void assertPersistedReportDistributionToMatchUpdatableProperties(ReportDistribution expectedReportDistribution) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportDistributionAllUpdatablePropertiesEquals(expectedReportDistribution, getPersistedReportDistribution(expectedReportDistribution));
        assertReportDistributionUpdatableFieldsEquals(
            expectedReportDistribution,
            getPersistedReportDistribution(expectedReportDistribution)
        );
    }
}
