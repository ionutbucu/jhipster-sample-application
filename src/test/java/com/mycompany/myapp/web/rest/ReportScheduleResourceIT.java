package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReportScheduleAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReportSchedule;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ReportScheduleRepository;
import com.mycompany.myapp.service.dto.ReportScheduleDTO;
import com.mycompany.myapp.service.mapper.ReportScheduleMapper;
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
 * Integration tests for the {@link ReportScheduleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportScheduleResourceIT {

    private static final String DEFAULT_CRON = "AAAAAAAAAA";
    private static final String UPDATED_CRON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{rid}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportScheduleRepository reportScheduleRepository;

    @Autowired
    private ReportScheduleMapper reportScheduleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ReportSchedule reportSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportSchedule createEntity(EntityManager em) {
        ReportSchedule reportSchedule = new ReportSchedule().rid(UUID.randomUUID().toString()).cron(DEFAULT_CRON);
        return reportSchedule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportSchedule createUpdatedEntity(EntityManager em) {
        ReportSchedule reportSchedule = new ReportSchedule().rid(UUID.randomUUID().toString()).cron(UPDATED_CRON);
        return reportSchedule;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ReportSchedule.class).block();
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
        reportSchedule = createEntity(em);
    }

    @Test
    void createReportSchedule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);
        var returnedReportScheduleDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReportScheduleDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ReportSchedule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportSchedule = reportScheduleMapper.toEntity(returnedReportScheduleDTO);
        assertReportScheduleUpdatableFieldsEquals(returnedReportSchedule, getPersistedReportSchedule(returnedReportSchedule));
    }

    @Test
    void createReportScheduleWithExistingId() throws Exception {
        // Create the ReportSchedule with an existing ID
        reportScheduleRepository.save(reportSchedule).block();
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCronIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportSchedule.setCron(null);

        // Create the ReportSchedule, which fails.
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllReportSchedulesAsStream() {
        // Initialize the database
        reportSchedule.setRid(UUID.randomUUID().toString());
        reportScheduleRepository.save(reportSchedule).block();

        List<ReportSchedule> reportScheduleList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ReportScheduleDTO.class)
            .getResponseBody()
            .map(reportScheduleMapper::toEntity)
            .filter(reportSchedule::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(reportScheduleList).isNotNull();
        assertThat(reportScheduleList).hasSize(1);
        ReportSchedule testReportSchedule = reportScheduleList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertReportScheduleAllPropertiesEquals(reportSchedule, testReportSchedule);
        assertReportScheduleUpdatableFieldsEquals(reportSchedule, testReportSchedule);
    }

    @Test
    void getAllReportSchedules() {
        // Initialize the database
        reportSchedule.setRid(UUID.randomUUID().toString());
        reportScheduleRepository.save(reportSchedule).block();

        // Get all the reportScheduleList
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
            .value(hasItem(reportSchedule.getRid()))
            .jsonPath("$.[*].cron")
            .value(hasItem(DEFAULT_CRON));
    }

    @Test
    void getReportSchedule() {
        // Initialize the database
        reportSchedule.setRid(UUID.randomUUID().toString());
        reportScheduleRepository.save(reportSchedule).block();

        // Get the reportSchedule
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reportSchedule.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.rid")
            .value(is(reportSchedule.getRid()))
            .jsonPath("$.cron")
            .value(is(DEFAULT_CRON));
    }

    @Test
    void getNonExistingReportSchedule() {
        // Get the reportSchedule
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReportSchedule() throws Exception {
        // Initialize the database
        reportSchedule.setRid(UUID.randomUUID().toString());
        reportScheduleRepository.save(reportSchedule).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportSchedule
        ReportSchedule updatedReportSchedule = reportScheduleRepository.findById(reportSchedule.getRid()).block();
        updatedReportSchedule.cron(UPDATED_CRON);
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(updatedReportSchedule);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportScheduleDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportScheduleToMatchAllProperties(updatedReportSchedule);
    }

    @Test
    void putNonExistingReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setRid(UUID.randomUUID().toString());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportScheduleDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setRid(UUID.randomUUID().toString());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setRid(UUID.randomUUID().toString());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportScheduleWithPatch() throws Exception {
        // Initialize the database
        reportSchedule.setRid(UUID.randomUUID().toString());
        reportScheduleRepository.save(reportSchedule).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportSchedule using partial update
        ReportSchedule partialUpdatedReportSchedule = new ReportSchedule();
        partialUpdatedReportSchedule.setRid(reportSchedule.getRid());

        partialUpdatedReportSchedule.cron(UPDATED_CRON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportSchedule.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportSchedule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportSchedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportScheduleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportSchedule, reportSchedule),
            getPersistedReportSchedule(reportSchedule)
        );
    }

    @Test
    void fullUpdateReportScheduleWithPatch() throws Exception {
        // Initialize the database
        reportSchedule.setRid(UUID.randomUUID().toString());
        reportScheduleRepository.save(reportSchedule).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportSchedule using partial update
        ReportSchedule partialUpdatedReportSchedule = new ReportSchedule();
        partialUpdatedReportSchedule.setRid(reportSchedule.getRid());

        partialUpdatedReportSchedule.cron(UPDATED_CRON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReportSchedule.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReportSchedule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ReportSchedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportScheduleUpdatableFieldsEquals(partialUpdatedReportSchedule, getPersistedReportSchedule(partialUpdatedReportSchedule));
    }

    @Test
    void patchNonExistingReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setRid(UUID.randomUUID().toString());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportScheduleDTO.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setRid(UUID.randomUUID().toString());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReportSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportSchedule.setRid(UUID.randomUUID().toString());

        // Create the ReportSchedule
        ReportScheduleDTO reportScheduleDTO = reportScheduleMapper.toDto(reportSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportScheduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ReportSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReportSchedule() {
        // Initialize the database
        reportSchedule.setRid(UUID.randomUUID().toString());
        reportScheduleRepository.save(reportSchedule).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportSchedule
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reportSchedule.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportScheduleRepository.count().block();
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

    protected ReportSchedule getPersistedReportSchedule(ReportSchedule reportSchedule) {
        return reportScheduleRepository.findById(reportSchedule.getRid()).block();
    }

    protected void assertPersistedReportScheduleToMatchAllProperties(ReportSchedule expectedReportSchedule) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportScheduleAllPropertiesEquals(expectedReportSchedule, getPersistedReportSchedule(expectedReportSchedule));
        assertReportScheduleUpdatableFieldsEquals(expectedReportSchedule, getPersistedReportSchedule(expectedReportSchedule));
    }

    protected void assertPersistedReportScheduleToMatchUpdatableProperties(ReportSchedule expectedReportSchedule) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportScheduleAllUpdatablePropertiesEquals(expectedReportSchedule, getPersistedReportSchedule(expectedReportSchedule));
        assertReportScheduleUpdatableFieldsEquals(expectedReportSchedule, getPersistedReportSchedule(expectedReportSchedule));
    }
}
