package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ReportDataSourceRepository;
import com.mycompany.myapp.service.ReportDataSourceService;
import com.mycompany.myapp.service.dto.ReportDataSourceDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ReportDataSource}.
 */
@RestController
@RequestMapping("/api/report-data-sources")
public class ReportDataSourceResource {

    private final Logger log = LoggerFactory.getLogger(ReportDataSourceResource.class);

    private static final String ENTITY_NAME = "reportDataSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportDataSourceService reportDataSourceService;

    private final ReportDataSourceRepository reportDataSourceRepository;

    public ReportDataSourceResource(
        ReportDataSourceService reportDataSourceService,
        ReportDataSourceRepository reportDataSourceRepository
    ) {
        this.reportDataSourceService = reportDataSourceService;
        this.reportDataSourceRepository = reportDataSourceRepository;
    }

    /**
     * {@code POST  /report-data-sources} : Create a new reportDataSource.
     *
     * @param reportDataSourceDTO the reportDataSourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportDataSourceDTO, or with status {@code 400 (Bad Request)} if the reportDataSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ReportDataSourceDTO>> createReportDataSource(@RequestBody ReportDataSourceDTO reportDataSourceDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportDataSource : {}", reportDataSourceDTO);
        return reportDataSourceRepository
            .existsById(reportDataSourceDTO.getRid())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new BadRequestAlertException("reportDataSource already exists", ENTITY_NAME, "idexists"));
                }
                return reportDataSourceService
                    .save(reportDataSourceDTO)
                    .map(result -> {
                        try {
                            return ResponseEntity.created(new URI("/api/report-data-sources/" + result.getRid()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getRid()))
                                .body(result);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    });
            });
    }

    /**
     * {@code PUT  /report-data-sources/:rid} : Updates an existing reportDataSource.
     *
     * @param rid the id of the reportDataSourceDTO to save.
     * @param reportDataSourceDTO the reportDataSourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDataSourceDTO,
     * or with status {@code 400 (Bad Request)} if the reportDataSourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportDataSourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{rid}")
    public Mono<ResponseEntity<ReportDataSourceDTO>> updateReportDataSource(
        @PathVariable(value = "rid", required = false) final String rid,
        @RequestBody ReportDataSourceDTO reportDataSourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportDataSource : {}, {}", rid, reportDataSourceDTO);
        if (reportDataSourceDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportDataSourceDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportDataSourceRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reportDataSourceService
                    .update(reportDataSourceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getRid()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /report-data-sources/:rid} : Partial updates given fields of an existing reportDataSource, field will ignore if it is null
     *
     * @param rid the id of the reportDataSourceDTO to save.
     * @param reportDataSourceDTO the reportDataSourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDataSourceDTO,
     * or with status {@code 400 (Bad Request)} if the reportDataSourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportDataSourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportDataSourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{rid}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReportDataSourceDTO>> partialUpdateReportDataSource(
        @PathVariable(value = "rid", required = false) final String rid,
        @RequestBody ReportDataSourceDTO reportDataSourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportDataSource partially : {}, {}", rid, reportDataSourceDTO);
        if (reportDataSourceDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportDataSourceDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportDataSourceRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReportDataSourceDTO> result = reportDataSourceService.partialUpdate(reportDataSourceDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getRid()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /report-data-sources} : get all the reportDataSources.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportDataSources in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ReportDataSourceDTO>> getAllReportDataSources(@RequestParam(name = "filter", required = false) String filter) {
        if ("report-is-null".equals(filter)) {
            log.debug("REST request to get all ReportDataSources where report is null");
            return reportDataSourceService.findAllWhereReportIsNull().collectList();
        }
        log.debug("REST request to get all ReportDataSources");
        return reportDataSourceService.findAll().collectList();
    }

    /**
     * {@code GET  /report-data-sources} : get all the reportDataSources as a stream.
     * @return the {@link Flux} of reportDataSources.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ReportDataSourceDTO> getAllReportDataSourcesAsStream() {
        log.debug("REST request to get all ReportDataSources as a stream");
        return reportDataSourceService.findAll();
    }

    /**
     * {@code GET  /report-data-sources/:id} : get the "id" reportDataSource.
     *
     * @param id the id of the reportDataSourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportDataSourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReportDataSourceDTO>> getReportDataSource(@PathVariable("id") String id) {
        log.debug("REST request to get ReportDataSource : {}", id);
        Mono<ReportDataSourceDTO> reportDataSourceDTO = reportDataSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportDataSourceDTO);
    }

    /**
     * {@code DELETE  /report-data-sources/:id} : delete the "id" reportDataSource.
     *
     * @param id the id of the reportDataSourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReportDataSource(@PathVariable("id") String id) {
        log.debug("REST request to delete ReportDataSource : {}", id);
        return reportDataSourceService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
