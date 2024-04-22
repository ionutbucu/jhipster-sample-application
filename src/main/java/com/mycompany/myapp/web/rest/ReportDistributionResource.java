package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ReportDistributionRepository;
import com.mycompany.myapp.service.ReportDistributionService;
import com.mycompany.myapp.service.dto.ReportDistributionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReportDistribution}.
 */
@RestController
@RequestMapping("/api/report-distributions")
public class ReportDistributionResource {

    private final Logger log = LoggerFactory.getLogger(ReportDistributionResource.class);

    private static final String ENTITY_NAME = "reportDistribution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportDistributionService reportDistributionService;

    private final ReportDistributionRepository reportDistributionRepository;

    public ReportDistributionResource(
        ReportDistributionService reportDistributionService,
        ReportDistributionRepository reportDistributionRepository
    ) {
        this.reportDistributionService = reportDistributionService;
        this.reportDistributionRepository = reportDistributionRepository;
    }

    /**
     * {@code POST  /report-distributions} : Create a new reportDistribution.
     *
     * @param reportDistributionDTO the reportDistributionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportDistributionDTO, or with status {@code 400 (Bad Request)} if the reportDistribution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ReportDistributionDTO>> createReportDistribution(
        @Valid @RequestBody ReportDistributionDTO reportDistributionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ReportDistribution : {}", reportDistributionDTO);
        return reportDistributionRepository
            .existsById(reportDistributionDTO.getRid())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new BadRequestAlertException("reportDistribution already exists", ENTITY_NAME, "idexists"));
                }
                return reportDistributionService
                    .save(reportDistributionDTO)
                    .map(result -> {
                        try {
                            return ResponseEntity.created(new URI("/api/report-distributions/" + result.getRid()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getRid()))
                                .body(result);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    });
            });
    }

    /**
     * {@code PUT  /report-distributions/:rid} : Updates an existing reportDistribution.
     *
     * @param rid the id of the reportDistributionDTO to save.
     * @param reportDistributionDTO the reportDistributionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDistributionDTO,
     * or with status {@code 400 (Bad Request)} if the reportDistributionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportDistributionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{rid}")
    public Mono<ResponseEntity<ReportDistributionDTO>> updateReportDistribution(
        @PathVariable(value = "rid", required = false) final String rid,
        @Valid @RequestBody ReportDistributionDTO reportDistributionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportDistribution : {}, {}", rid, reportDistributionDTO);
        if (reportDistributionDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportDistributionDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportDistributionRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reportDistributionService
                    .update(reportDistributionDTO)
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
     * {@code PATCH  /report-distributions/:rid} : Partial updates given fields of an existing reportDistribution, field will ignore if it is null
     *
     * @param rid the id of the reportDistributionDTO to save.
     * @param reportDistributionDTO the reportDistributionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDistributionDTO,
     * or with status {@code 400 (Bad Request)} if the reportDistributionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportDistributionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportDistributionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{rid}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReportDistributionDTO>> partialUpdateReportDistribution(
        @PathVariable(value = "rid", required = false) final String rid,
        @NotNull @RequestBody ReportDistributionDTO reportDistributionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportDistribution partially : {}, {}", rid, reportDistributionDTO);
        if (reportDistributionDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportDistributionDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportDistributionRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReportDistributionDTO> result = reportDistributionService.partialUpdate(reportDistributionDTO);

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
     * {@code GET  /report-distributions} : get all the reportDistributions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportDistributions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ReportDistributionDTO>> getAllReportDistributions() {
        log.debug("REST request to get all ReportDistributions");
        return reportDistributionService.findAll().collectList();
    }

    /**
     * {@code GET  /report-distributions} : get all the reportDistributions as a stream.
     * @return the {@link Flux} of reportDistributions.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ReportDistributionDTO> getAllReportDistributionsAsStream() {
        log.debug("REST request to get all ReportDistributions as a stream");
        return reportDistributionService.findAll();
    }

    /**
     * {@code GET  /report-distributions/:id} : get the "id" reportDistribution.
     *
     * @param id the id of the reportDistributionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportDistributionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReportDistributionDTO>> getReportDistribution(@PathVariable("id") String id) {
        log.debug("REST request to get ReportDistribution : {}", id);
        Mono<ReportDistributionDTO> reportDistributionDTO = reportDistributionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportDistributionDTO);
    }

    /**
     * {@code DELETE  /report-distributions/:id} : delete the "id" reportDistribution.
     *
     * @param id the id of the reportDistributionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReportDistribution(@PathVariable("id") String id) {
        log.debug("REST request to delete ReportDistribution : {}", id);
        return reportDistributionService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
