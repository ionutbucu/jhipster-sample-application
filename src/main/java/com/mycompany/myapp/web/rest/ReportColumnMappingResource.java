package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ReportColumnMappingRepository;
import com.mycompany.myapp.service.ReportColumnMappingService;
import com.mycompany.myapp.service.dto.ReportColumnMappingDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReportColumnMapping}.
 */
@RestController
@RequestMapping("/api/report-column-mappings")
public class ReportColumnMappingResource {

    private final Logger log = LoggerFactory.getLogger(ReportColumnMappingResource.class);

    private static final String ENTITY_NAME = "reportColumnMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportColumnMappingService reportColumnMappingService;

    private final ReportColumnMappingRepository reportColumnMappingRepository;

    public ReportColumnMappingResource(
        ReportColumnMappingService reportColumnMappingService,
        ReportColumnMappingRepository reportColumnMappingRepository
    ) {
        this.reportColumnMappingService = reportColumnMappingService;
        this.reportColumnMappingRepository = reportColumnMappingRepository;
    }

    /**
     * {@code POST  /report-column-mappings} : Create a new reportColumnMapping.
     *
     * @param reportColumnMappingDTO the reportColumnMappingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportColumnMappingDTO, or with status {@code 400 (Bad Request)} if the reportColumnMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ReportColumnMappingDTO>> createReportColumnMapping(
        @RequestBody ReportColumnMappingDTO reportColumnMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ReportColumnMapping : {}", reportColumnMappingDTO);
        return reportColumnMappingRepository
            .existsById(reportColumnMappingDTO.getRid())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new BadRequestAlertException("reportColumnMapping already exists", ENTITY_NAME, "idexists"));
                }
                return reportColumnMappingService
                    .save(reportColumnMappingDTO)
                    .map(result -> {
                        try {
                            return ResponseEntity.created(new URI("/api/report-column-mappings/" + result.getRid()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getRid()))
                                .body(result);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    });
            });
    }

    /**
     * {@code PUT  /report-column-mappings/:rid} : Updates an existing reportColumnMapping.
     *
     * @param rid the id of the reportColumnMappingDTO to save.
     * @param reportColumnMappingDTO the reportColumnMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportColumnMappingDTO,
     * or with status {@code 400 (Bad Request)} if the reportColumnMappingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportColumnMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{rid}")
    public Mono<ResponseEntity<ReportColumnMappingDTO>> updateReportColumnMapping(
        @PathVariable(value = "rid", required = false) final String rid,
        @RequestBody ReportColumnMappingDTO reportColumnMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportColumnMapping : {}, {}", rid, reportColumnMappingDTO);
        if (reportColumnMappingDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportColumnMappingDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportColumnMappingRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reportColumnMappingService
                    .update(reportColumnMappingDTO)
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
     * {@code PATCH  /report-column-mappings/:rid} : Partial updates given fields of an existing reportColumnMapping, field will ignore if it is null
     *
     * @param rid the id of the reportColumnMappingDTO to save.
     * @param reportColumnMappingDTO the reportColumnMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportColumnMappingDTO,
     * or with status {@code 400 (Bad Request)} if the reportColumnMappingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportColumnMappingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportColumnMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{rid}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReportColumnMappingDTO>> partialUpdateReportColumnMapping(
        @PathVariable(value = "rid", required = false) final String rid,
        @RequestBody ReportColumnMappingDTO reportColumnMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportColumnMapping partially : {}, {}", rid, reportColumnMappingDTO);
        if (reportColumnMappingDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportColumnMappingDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportColumnMappingRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReportColumnMappingDTO> result = reportColumnMappingService.partialUpdate(reportColumnMappingDTO);

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
     * {@code GET  /report-column-mappings} : get all the reportColumnMappings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportColumnMappings in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ReportColumnMappingDTO>> getAllReportColumnMappings() {
        log.debug("REST request to get all ReportColumnMappings");
        return reportColumnMappingService.findAll().collectList();
    }

    /**
     * {@code GET  /report-column-mappings} : get all the reportColumnMappings as a stream.
     * @return the {@link Flux} of reportColumnMappings.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ReportColumnMappingDTO> getAllReportColumnMappingsAsStream() {
        log.debug("REST request to get all ReportColumnMappings as a stream");
        return reportColumnMappingService.findAll();
    }

    /**
     * {@code GET  /report-column-mappings/:id} : get the "id" reportColumnMapping.
     *
     * @param id the id of the reportColumnMappingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportColumnMappingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReportColumnMappingDTO>> getReportColumnMapping(@PathVariable("id") String id) {
        log.debug("REST request to get ReportColumnMapping : {}", id);
        Mono<ReportColumnMappingDTO> reportColumnMappingDTO = reportColumnMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportColumnMappingDTO);
    }

    /**
     * {@code DELETE  /report-column-mappings/:id} : delete the "id" reportColumnMapping.
     *
     * @param id the id of the reportColumnMappingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReportColumnMapping(@PathVariable("id") String id) {
        log.debug("REST request to delete ReportColumnMapping : {}", id);
        return reportColumnMappingService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
