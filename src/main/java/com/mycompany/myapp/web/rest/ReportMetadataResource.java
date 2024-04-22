package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ReportMetadataRepository;
import com.mycompany.myapp.service.ReportMetadataService;
import com.mycompany.myapp.service.dto.ReportMetadataDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReportMetadata}.
 */
@RestController
@RequestMapping("/api/report-metadata")
public class ReportMetadataResource {

    private final Logger log = LoggerFactory.getLogger(ReportMetadataResource.class);

    private static final String ENTITY_NAME = "reportMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportMetadataService reportMetadataService;

    private final ReportMetadataRepository reportMetadataRepository;

    public ReportMetadataResource(ReportMetadataService reportMetadataService, ReportMetadataRepository reportMetadataRepository) {
        this.reportMetadataService = reportMetadataService;
        this.reportMetadataRepository = reportMetadataRepository;
    }

    /**
     * {@code POST  /report-metadata} : Create a new reportMetadata.
     *
     * @param reportMetadataDTO the reportMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportMetadataDTO, or with status {@code 400 (Bad Request)} if the reportMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ReportMetadataDTO>> createReportMetadata(@RequestBody ReportMetadataDTO reportMetadataDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportMetadata : {}", reportMetadataDTO);
        return reportMetadataRepository
            .existsById(reportMetadataDTO.getRid())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new BadRequestAlertException("reportMetadata already exists", ENTITY_NAME, "idexists"));
                }
                return reportMetadataService
                    .save(reportMetadataDTO)
                    .map(result -> {
                        try {
                            return ResponseEntity.created(new URI("/api/report-metadata/" + result.getRid()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getRid()))
                                .body(result);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    });
            });
    }

    /**
     * {@code PUT  /report-metadata/:rid} : Updates an existing reportMetadata.
     *
     * @param rid the id of the reportMetadataDTO to save.
     * @param reportMetadataDTO the reportMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the reportMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{rid}")
    public Mono<ResponseEntity<ReportMetadataDTO>> updateReportMetadata(
        @PathVariable(value = "rid", required = false) final String rid,
        @RequestBody ReportMetadataDTO reportMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportMetadata : {}, {}", rid, reportMetadataDTO);
        if (reportMetadataDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportMetadataDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportMetadataRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reportMetadataService
                    .update(reportMetadataDTO)
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
     * {@code PATCH  /report-metadata/:rid} : Partial updates given fields of an existing reportMetadata, field will ignore if it is null
     *
     * @param rid the id of the reportMetadataDTO to save.
     * @param reportMetadataDTO the reportMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the reportMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportMetadataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{rid}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReportMetadataDTO>> partialUpdateReportMetadata(
        @PathVariable(value = "rid", required = false) final String rid,
        @RequestBody ReportMetadataDTO reportMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportMetadata partially : {}, {}", rid, reportMetadataDTO);
        if (reportMetadataDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportMetadataDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportMetadataRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReportMetadataDTO> result = reportMetadataService.partialUpdate(reportMetadataDTO);

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
     * {@code GET  /report-metadata} : get all the reportMetadata.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportMetadata in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ReportMetadataDTO>> getAllReportMetadata(@RequestParam(name = "filter", required = false) String filter) {
        if ("report-is-null".equals(filter)) {
            log.debug("REST request to get all ReportMetadatas where report is null");
            return reportMetadataService.findAllWhereReportIsNull().collectList();
        }
        log.debug("REST request to get all ReportMetadata");
        return reportMetadataService.findAll().collectList();
    }

    /**
     * {@code GET  /report-metadata} : get all the reportMetadata as a stream.
     * @return the {@link Flux} of reportMetadata.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ReportMetadataDTO> getAllReportMetadataAsStream() {
        log.debug("REST request to get all ReportMetadata as a stream");
        return reportMetadataService.findAll();
    }

    /**
     * {@code GET  /report-metadata/:id} : get the "id" reportMetadata.
     *
     * @param id the id of the reportMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReportMetadataDTO>> getReportMetadata(@PathVariable("id") String id) {
        log.debug("REST request to get ReportMetadata : {}", id);
        Mono<ReportMetadataDTO> reportMetadataDTO = reportMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportMetadataDTO);
    }

    /**
     * {@code DELETE  /report-metadata/:id} : delete the "id" reportMetadata.
     *
     * @param id the id of the reportMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReportMetadata(@PathVariable("id") String id) {
        log.debug("REST request to delete ReportMetadata : {}", id);
        return reportMetadataService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
