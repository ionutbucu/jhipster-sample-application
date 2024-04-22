package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ReportParamRepository;
import com.mycompany.myapp.service.ReportParamService;
import com.mycompany.myapp.service.dto.ReportParamDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReportParam}.
 */
@RestController
@RequestMapping("/api/report-params")
public class ReportParamResource {

    private final Logger log = LoggerFactory.getLogger(ReportParamResource.class);

    private static final String ENTITY_NAME = "reportParam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportParamService reportParamService;

    private final ReportParamRepository reportParamRepository;

    public ReportParamResource(ReportParamService reportParamService, ReportParamRepository reportParamRepository) {
        this.reportParamService = reportParamService;
        this.reportParamRepository = reportParamRepository;
    }

    /**
     * {@code POST  /report-params} : Create a new reportParam.
     *
     * @param reportParamDTO the reportParamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportParamDTO, or with status {@code 400 (Bad Request)} if the reportParam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ReportParamDTO>> createReportParam(@RequestBody ReportParamDTO reportParamDTO) throws URISyntaxException {
        log.debug("REST request to save ReportParam : {}", reportParamDTO);
        return reportParamRepository
            .existsById(reportParamDTO.getRid())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new BadRequestAlertException("reportParam already exists", ENTITY_NAME, "idexists"));
                }
                return reportParamService
                    .save(reportParamDTO)
                    .map(result -> {
                        try {
                            return ResponseEntity.created(new URI("/api/report-params/" + result.getRid()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getRid()))
                                .body(result);
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    });
            });
    }

    /**
     * {@code PUT  /report-params/:rid} : Updates an existing reportParam.
     *
     * @param rid the id of the reportParamDTO to save.
     * @param reportParamDTO the reportParamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportParamDTO,
     * or with status {@code 400 (Bad Request)} if the reportParamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportParamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{rid}")
    public Mono<ResponseEntity<ReportParamDTO>> updateReportParam(
        @PathVariable(value = "rid", required = false) final String rid,
        @RequestBody ReportParamDTO reportParamDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportParam : {}, {}", rid, reportParamDTO);
        if (reportParamDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportParamDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportParamRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reportParamService
                    .update(reportParamDTO)
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
     * {@code PATCH  /report-params/:rid} : Partial updates given fields of an existing reportParam, field will ignore if it is null
     *
     * @param rid the id of the reportParamDTO to save.
     * @param reportParamDTO the reportParamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportParamDTO,
     * or with status {@code 400 (Bad Request)} if the reportParamDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportParamDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportParamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{rid}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReportParamDTO>> partialUpdateReportParam(
        @PathVariable(value = "rid", required = false) final String rid,
        @RequestBody ReportParamDTO reportParamDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportParam partially : {}, {}", rid, reportParamDTO);
        if (reportParamDTO.getRid() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(rid, reportParamDTO.getRid())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reportParamRepository
            .existsById(rid)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReportParamDTO> result = reportParamService.partialUpdate(reportParamDTO);

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
     * {@code GET  /report-params} : get all the reportParams.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportParams in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ReportParamDTO>> getAllReportParams() {
        log.debug("REST request to get all ReportParams");
        return reportParamService.findAll().collectList();
    }

    /**
     * {@code GET  /report-params} : get all the reportParams as a stream.
     * @return the {@link Flux} of reportParams.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ReportParamDTO> getAllReportParamsAsStream() {
        log.debug("REST request to get all ReportParams as a stream");
        return reportParamService.findAll();
    }

    /**
     * {@code GET  /report-params/:id} : get the "id" reportParam.
     *
     * @param id the id of the reportParamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportParamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReportParamDTO>> getReportParam(@PathVariable("id") String id) {
        log.debug("REST request to get ReportParam : {}", id);
        Mono<ReportParamDTO> reportParamDTO = reportParamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportParamDTO);
    }

    /**
     * {@code DELETE  /report-params/:id} : delete the "id" reportParam.
     *
     * @param id the id of the reportParamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReportParam(@PathVariable("id") String id) {
        log.debug("REST request to delete ReportParam : {}", id);
        return reportParamService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
