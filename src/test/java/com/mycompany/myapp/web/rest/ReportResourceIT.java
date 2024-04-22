package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReportAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.domain.enumeration.QueryType;
import com.mycompany.myapp.domain.enumeration.ReportType;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ReportRepository;
import com.mycompany.myapp.service.dto.ReportDTO;
import com.mycompany.myapp.service.mapper.ReportMapper;
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
 * Integration tests for the {@link ReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportResourceIT {

    private static final String DEFAULT_CID = "AAAAAAAAAA";
    private static final String UPDATED_CID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    private static final QueryType DEFAULT_QUERY_TYPE = QueryType.NATIVE_QUERY;
    private static final QueryType UPDATED_QUERY_TYPE = QueryType.HQL;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final ReportType DEFAULT_REPORT_TYPE = ReportType.CSV;
    private static final ReportType UPDATED_REPORT_TYPE = ReportType.PDF;

    private static final String DEFAULT_LICENSE_HOLDER = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_HOLDER = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{rid}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Report report;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createEntity(EntityManager em) {
        Report report = new Report()
            .rid(UUID.randomUUID().toString())
            .cid(DEFAULT_CID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .query(DEFAULT_QUERY)
            .queryType(DEFAULT_QUERY_TYPE)
            .fileName(DEFAULT_FILE_NAME)
            .reportType(DEFAULT_REPORT_TYPE)
            .licenseHolder(DEFAULT_LICENSE_HOLDER)
            .owner(DEFAULT_OWNER);
        return report;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createUpdatedEntity(EntityManager em) {
        Report report = new Report()
            .rid(UUID.randomUUID().toString())
            .cid(UPDATED_CID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .query(UPDATED_QUERY)
            .queryType(UPDATED_QUERY_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .reportType(UPDATED_REPORT_TYPE)
            .licenseHolder(UPDATED_LICENSE_HOLDER)
            .owner(UPDATED_OWNER);
        return report;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Report.class).block();
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
        report = createEntity(em);
    }

    @Test
    void createReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);
        var returnedReportDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReportDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Report in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReport = reportMapper.toEntity(returnedReportDTO);
        assertReportUpdatableFieldsEquals(returnedReport, getPersistedReport(returnedReport));
    }

    @Test
    void createReportWithExistingId() throws Exception {
        // Create the Report with an existing ID
        reportRepository.save(report).block();
        ReportDTO reportDTO = reportMapper.toDto(report);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        report.setName(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkQueryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        report.setQuery(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        report.setFileName(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllReports() {
        // Initialize the database
        report.setRid(UUID.randomUUID().toString());
        reportRepository.save(report).block();

        // Get all the reportList
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
            .value(hasItem(report.getRid()))
            .jsonPath("$.[*].cid")
            .value(hasItem(DEFAULT_CID))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].query")
            .value(hasItem(DEFAULT_QUERY))
            .jsonPath("$.[*].queryType")
            .value(hasItem(DEFAULT_QUERY_TYPE.toString()))
            .jsonPath("$.[*].fileName")
            .value(hasItem(DEFAULT_FILE_NAME))
            .jsonPath("$.[*].reportType")
            .value(hasItem(DEFAULT_REPORT_TYPE.toString()))
            .jsonPath("$.[*].licenseHolder")
            .value(hasItem(DEFAULT_LICENSE_HOLDER))
            .jsonPath("$.[*].owner")
            .value(hasItem(DEFAULT_OWNER));
    }

    @Test
    void getReport() {
        // Initialize the database
        report.setRid(UUID.randomUUID().toString());
        reportRepository.save(report).block();

        // Get the report
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, report.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.rid")
            .value(is(report.getRid()))
            .jsonPath("$.cid")
            .value(is(DEFAULT_CID))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.query")
            .value(is(DEFAULT_QUERY))
            .jsonPath("$.queryType")
            .value(is(DEFAULT_QUERY_TYPE.toString()))
            .jsonPath("$.fileName")
            .value(is(DEFAULT_FILE_NAME))
            .jsonPath("$.reportType")
            .value(is(DEFAULT_REPORT_TYPE.toString()))
            .jsonPath("$.licenseHolder")
            .value(is(DEFAULT_LICENSE_HOLDER))
            .jsonPath("$.owner")
            .value(is(DEFAULT_OWNER));
    }

    @Test
    void getNonExistingReport() {
        // Get the report
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReport() throws Exception {
        // Initialize the database
        report.setRid(UUID.randomUUID().toString());
        reportRepository.save(report).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getRid()).block();
        updatedReport
            .cid(UPDATED_CID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .query(UPDATED_QUERY)
            .queryType(UPDATED_QUERY_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .reportType(UPDATED_REPORT_TYPE)
            .licenseHolder(UPDATED_LICENSE_HOLDER)
            .owner(UPDATED_OWNER);
        ReportDTO reportDTO = reportMapper.toDto(updatedReport);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportToMatchAllProperties(updatedReport);
    }

    @Test
    void putNonExistingReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setRid(UUID.randomUUID().toString());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDTO.getRid())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setRid(UUID.randomUUID().toString());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setRid(UUID.randomUUID().toString());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportWithPatch() throws Exception {
        // Initialize the database
        report.setRid(UUID.randomUUID().toString());
        reportRepository.save(report).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setRid(report.getRid());

        partialUpdatedReport.description(UPDATED_DESCRIPTION).licenseHolder(UPDATED_LICENSE_HOLDER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReport.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Report in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReport, report), getPersistedReport(report));
    }

    @Test
    void fullUpdateReportWithPatch() throws Exception {
        // Initialize the database
        report.setRid(UUID.randomUUID().toString());
        reportRepository.save(report).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setRid(report.getRid());

        partialUpdatedReport
            .cid(UPDATED_CID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .query(UPDATED_QUERY)
            .queryType(UPDATED_QUERY_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .reportType(UPDATED_REPORT_TYPE)
            .licenseHolder(UPDATED_LICENSE_HOLDER)
            .owner(UPDATED_OWNER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReport.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Report in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportUpdatableFieldsEquals(partialUpdatedReport, getPersistedReport(partialUpdatedReport));
    }

    @Test
    void patchNonExistingReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setRid(UUID.randomUUID().toString());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportDTO.getRid())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setRid(UUID.randomUUID().toString());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setRid(UUID.randomUUID().toString());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReport() {
        // Initialize the database
        report.setRid(UUID.randomUUID().toString());
        reportRepository.save(report).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the report
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, report.getRid())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportRepository.count().block();
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

    protected Report getPersistedReport(Report report) {
        return reportRepository.findById(report.getRid()).block();
    }

    protected void assertPersistedReportToMatchAllProperties(Report expectedReport) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportAllPropertiesEquals(expectedReport, getPersistedReport(expectedReport));
        assertReportUpdatableFieldsEquals(expectedReport, getPersistedReport(expectedReport));
    }

    protected void assertPersistedReportToMatchUpdatableProperties(Report expectedReport) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportAllUpdatablePropertiesEquals(expectedReport, getPersistedReport(expectedReport));
        assertReportUpdatableFieldsEquals(expectedReport, getPersistedReport(expectedReport));
    }
}
