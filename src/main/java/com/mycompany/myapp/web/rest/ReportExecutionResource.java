package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ReportExecutionRepository;
import com.mycompany.myapp.service.ReportExecutionService;
import com.mycompany.myapp.service.dto.ReportExecutionDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReportExecution}.
 */
@RestController
@RequestMapping("/api/report-executions")
public class ReportExecutionResource {

    private final Logger log = LoggerFactory.getLogger(ReportExecutionResource.class);

    private static final String ENTITY_NAME = "reportExecution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportExecutionService reportExecutionService;

    private final ReportExecutionRepository reportExecutionRepository;

    public ReportExecutionResource(ReportExecutionService reportExecutionService, ReportExecutionRepository reportExecutionRepository) {
        this.reportExecutionService = reportExecutionService;
        this.reportExecutionRepository = reportExecutionRepository;
    }

    /**
     * {@code POST  /report-executions} : Create a new reportExecution.
     *
     * @param reportExecutionDTO the reportExecutionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportExecutionDTO, or with status {@code 400 (Bad Request)} if the reportExecution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ReportExecutionDTO>> createReportExecution(@Valid @RequestBody ReportExecutionDTO reportExecutionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportExecution : {}", reportExecutionDTO);
        return reportExecutionRepository
            .existsById(reportExecutionDTO.getRid())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new BadRequestAlertException("reportExecution already exists", ENTITY_NAME, "idexists"));
                }
                return reportExecutionService
                    .save(reportExecutionDTO)
                    .map(result -> {
                        try {
                            return ResponseEntity.created(new URI("/api/report-executions/" + result.getRid()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getRid()))
                                .body(result);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    });
            });
    }

    /**
     * {@code PUT  /report-executions/:rid} : Updates an existing reportExecution.
     *
     * @param rid the id of the reportExecutionDTO to save.
     * @param reportExecutionDTO the reportExecutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportExecutionDTO,
     * or with status {@code 400 (Bad Request)} if the reportExecutionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportExecutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{rid}")
    public Mono<ResponseEntity<ReportExecutionDTO>> updateReportExecution(
        @PathVariable(value = "rid", required = false) final String rid,
        @Valid @RequestBody ReportExecutionDTO reportExecutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportExecution : {}, {}", rid, reportExecutionDTO);
        if (reportExecutionDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportExecutionDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportExecutionRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reportExecutionService
                    .update(reportExecutionDTO)
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
     * {@code PATCH  /report-executions/:rid} : Partial updates given fields of an existing reportExecution, field will ignore if it is null
     *
     * @param rid the id of the reportExecutionDTO to save.
     * @param reportExecutionDTO the reportExecutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportExecutionDTO,
     * or with status {@code 400 (Bad Request)} if the reportExecutionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportExecutionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportExecutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{rid}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReportExecutionDTO>> partialUpdateReportExecution(
        @PathVariable(value = "rid", required = false) final String rid,
        @NotNull @RequestBody ReportExecutionDTO reportExecutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportExecution partially : {}, {}", rid, reportExecutionDTO);
        if (reportExecutionDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportExecutionDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportExecutionRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReportExecutionDTO> result = reportExecutionService.partialUpdate(reportExecutionDTO);

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
     * {@code GET  /report-executions} : get all the reportExecutions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportExecutions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ReportExecutionDTO>>> getAllReportExecutions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ReportExecutions");
        return reportExecutionService
            .countAll()
            .zipWith(reportExecutionService.findAll(pageable).collectList())
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
     * {@code GET  /report-executions/:id} : get the "id" reportExecution.
     *
     * @param id the id of the reportExecutionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportExecutionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReportExecutionDTO>> getReportExecution(@PathVariable("id") String id) {
        log.debug("REST request to get ReportExecution : {}", id);
        Mono<ReportExecutionDTO> reportExecutionDTO = reportExecutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportExecutionDTO);
    }

    /**
     * {@code DELETE  /report-executions/:id} : delete the "id" reportExecution.
     *
     * @param id the id of the reportExecutionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReportExecution(@PathVariable("id") String id) {
        log.debug("REST request to delete ReportExecution : {}", id);
        return reportExecutionService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
