package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ReportRepository;
import com.mycompany.myapp.service.ReportService;
import com.mycompany.myapp.service.dto.ReportDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Report}.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private static final String ENTITY_NAME = "report";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportService reportService;

    private final ReportRepository reportRepository;

    public ReportResource(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    /**
     * {@code POST  /reports} : Create a new report.
     *
     * @param reportDTO the reportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportDTO, or with status {@code 400 (Bad Request)} if the report has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ReportDTO>> createReport(@Valid @RequestBody ReportDTO reportDTO) throws URISyntaxException {
        log.debug("REST request to save Report : {}", reportDTO);
        return reportRepository
            .existsById(reportDTO.getRid())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new BadRequestAlertException("report already exists", ENTITY_NAME, "idexists"));
                }
                return reportService
                    .save(reportDTO)
                    .map(result -> {
                        try {
                            return ResponseEntity.created(new URI("/api/reports/" + result.getRid()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getRid()))
                                .body(result);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    });
            });
    }

    /**
     * {@code PUT  /reports/:rid} : Updates an existing report.
     *
     * @param rid the id of the reportDTO to save.
     * @param reportDTO the reportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDTO,
     * or with status {@code 400 (Bad Request)} if the reportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{rid}")
    public Mono<ResponseEntity<ReportDTO>> updateReport(
        @PathVariable(value = "rid", required = false) final String rid,
        @Valid @RequestBody ReportDTO reportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Report : {}, {}", rid, reportDTO);
        if (reportDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reportService
                    .update(reportDTO)
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
     * {@code PATCH  /reports/:rid} : Partial updates given fields of an existing report, field will ignore if it is null
     *
     * @param rid the id of the reportDTO to save.
     * @param reportDTO the reportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDTO,
     * or with status {@code 400 (Bad Request)} if the reportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{rid}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReportDTO>> partialUpdateReport(
        @PathVariable(value = "rid", required = false) final String rid,
        @NotNull @RequestBody ReportDTO reportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Report partially : {}, {}", rid, reportDTO);
        if (reportDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReportDTO> result = reportService.partialUpdate(reportDTO);

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
     * {@code GET  /reports} : get all the reports.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reports in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ReportDTO>>> getAllReports(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Reports");
        return reportService
            .countAll()
            .zipWith(reportService.findAll(pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /reports/:id} : get the "id" report.
     *
     * @param id the id of the reportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReportDTO>> getReport(@PathVariable("id") String id) {
        log.debug("REST request to get Report : {}", id);
        Mono<ReportDTO> reportDTO = reportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportDTO);
    }

    /**
     * {@code DELETE  /reports/:id} : delete the "id" report.
     *
     * @param id the id of the reportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReport(@PathVariable("id") String id) {
        log.debug("REST request to delete Report : {}", id);
        return reportService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
