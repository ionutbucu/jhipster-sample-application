package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.ReportParamRepository;
import com.mycompany.myapp.service.dto.ReportParamDTO;
import com.mycompany.myapp.service.mapper.ReportParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ReportParam}.
 */
@Service
@Transactional
public class ReportParamService {

    private final Logger log = LoggerFactory.getLogger(ReportParamService.class);

    private final ReportParamRepository reportParamRepository;

    private final ReportParamMapper reportParamMapper;

    public ReportParamService(ReportParamRepository reportParamRepository, ReportParamMapper reportParamMapper) {
        this.reportParamRepository = reportParamRepository;
        this.reportParamMapper = reportParamMapper;
    }

    /**
     * Save a reportParam.
     *
     * @param reportParamDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportParamDTO> save(ReportParamDTO reportParamDTO) {
        log.debug("Request to save ReportParam : {}", reportParamDTO);
        return reportParamRepository.save(reportParamMapper.toEntity(reportParamDTO)).map(reportParamMapper::toDto);
    }

    /**
     * Update a reportParam.
     *
     * @param reportParamDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportParamDTO> update(ReportParamDTO reportParamDTO) {
        log.debug("Request to update ReportParam : {}", reportParamDTO);
        return reportParamRepository.save(reportParamMapper.toEntity(reportParamDTO).setIsPersisted()).map(reportParamMapper::toDto);
    }

    /**
     * Partially update a reportParam.
     *
     * @param reportParamDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReportParamDTO> partialUpdate(ReportParamDTO reportParamDTO) {
        log.debug("Request to partially update ReportParam : {}", reportParamDTO);

        return reportParamRepository
            .findById(reportParamDTO.getRid())
            .map(existingReportParam -> {
                reportParamMapper.partialUpdate(existingReportParam, reportParamDTO);

                return existingReportParam;
            })
            .flatMap(reportParamRepository::save)
            .map(reportParamMapper::toDto);
    }

    /**
     * Get all the reportParams.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportParamDTO> findAll() {
        log.debug("Request to get all ReportParams");
        return reportParamRepository.findAll().map(reportParamMapper::toDto);
    }

    /**
     * Returns the number of reportParams available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reportParamRepository.count();
    }

    /**
     * Get one reportParam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReportParamDTO> findOne(String id) {
        log.debug("Request to get ReportParam : {}", id);
        return reportParamRepository.findById(id).map(reportParamMapper::toDto);
    }

    /**
     * Delete the reportParam by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ReportParam : {}", id);
        return reportParamRepository.deleteById(id);
    }
}
