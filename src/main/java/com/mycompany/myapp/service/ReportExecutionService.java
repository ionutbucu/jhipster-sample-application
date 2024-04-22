package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.ReportExecutionRepository;
import com.mycompany.myapp.service.dto.ReportExecutionDTO;
import com.mycompany.myapp.service.mapper.ReportExecutionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ReportExecution}.
 */
@Service
@Transactional
public class ReportExecutionService {

    private final Logger log = LoggerFactory.getLogger(ReportExecutionService.class);

    private final ReportExecutionRepository reportExecutionRepository;

    private final ReportExecutionMapper reportExecutionMapper;

    public ReportExecutionService(ReportExecutionRepository reportExecutionRepository, ReportExecutionMapper reportExecutionMapper) {
        this.reportExecutionRepository = reportExecutionRepository;
        this.reportExecutionMapper = reportExecutionMapper;
    }

    /**
     * Save a reportExecution.
     *
     * @param reportExecutionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportExecutionDTO> save(ReportExecutionDTO reportExecutionDTO) {
        log.debug("Request to save ReportExecution : {}", reportExecutionDTO);
        return reportExecutionRepository.save(reportExecutionMapper.toEntity(reportExecutionDTO)).map(reportExecutionMapper::toDto);
    }

    /**
     * Update a reportExecution.
     *
     * @param reportExecutionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportExecutionDTO> update(ReportExecutionDTO reportExecutionDTO) {
        log.debug("Request to update ReportExecution : {}", reportExecutionDTO);
        return reportExecutionRepository
            .save(reportExecutionMapper.toEntity(reportExecutionDTO).setIsPersisted())
            .map(reportExecutionMapper::toDto);
    }

    /**
     * Partially update a reportExecution.
     *
     * @param reportExecutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReportExecutionDTO> partialUpdate(ReportExecutionDTO reportExecutionDTO) {
        log.debug("Request to partially update ReportExecution : {}", reportExecutionDTO);

        return reportExecutionRepository
            .findById(reportExecutionDTO.getRid())
            .map(existingReportExecution -> {
                reportExecutionMapper.partialUpdate(existingReportExecution, reportExecutionDTO);

                return existingReportExecution;
            })
            .flatMap(reportExecutionRepository::save)
            .map(reportExecutionMapper::toDto);
    }

    /**
     * Get all the reportExecutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportExecutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportExecutions");
        return reportExecutionRepository.findAllBy(pageable).map(reportExecutionMapper::toDto);
    }

    /**
     * Returns the number of reportExecutions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reportExecutionRepository.count();
    }

    /**
     * Get one reportExecution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReportExecutionDTO> findOne(String id) {
        log.debug("Request to get ReportExecution : {}", id);
        return reportExecutionRepository.findById(id).map(reportExecutionMapper::toDto);
    }

    /**
     * Delete the reportExecution by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ReportExecution : {}", id);
        return reportExecutionRepository.deleteById(id);
    }
}
