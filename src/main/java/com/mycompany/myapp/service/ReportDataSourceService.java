package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.ReportDataSourceRepository;
import com.mycompany.myapp.service.dto.ReportDataSourceDTO;
import com.mycompany.myapp.service.mapper.ReportDataSourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ReportDataSource}.
 */
@Service
@Transactional
public class ReportDataSourceService {

    private final Logger log = LoggerFactory.getLogger(ReportDataSourceService.class);

    private final ReportDataSourceRepository reportDataSourceRepository;

    private final ReportDataSourceMapper reportDataSourceMapper;

    public ReportDataSourceService(ReportDataSourceRepository reportDataSourceRepository, ReportDataSourceMapper reportDataSourceMapper) {
        this.reportDataSourceRepository = reportDataSourceRepository;
        this.reportDataSourceMapper = reportDataSourceMapper;
    }

    /**
     * Save a reportDataSource.
     *
     * @param reportDataSourceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportDataSourceDTO> save(ReportDataSourceDTO reportDataSourceDTO) {
        log.debug("Request to save ReportDataSource : {}", reportDataSourceDTO);
        return reportDataSourceRepository.save(reportDataSourceMapper.toEntity(reportDataSourceDTO)).map(reportDataSourceMapper::toDto);
    }

    /**
     * Update a reportDataSource.
     *
     * @param reportDataSourceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportDataSourceDTO> update(ReportDataSourceDTO reportDataSourceDTO) {
        log.debug("Request to update ReportDataSource : {}", reportDataSourceDTO);
        return reportDataSourceRepository
            .save(reportDataSourceMapper.toEntity(reportDataSourceDTO).setIsPersisted())
            .map(reportDataSourceMapper::toDto);
    }

    /**
     * Partially update a reportDataSource.
     *
     * @param reportDataSourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReportDataSourceDTO> partialUpdate(ReportDataSourceDTO reportDataSourceDTO) {
        log.debug("Request to partially update ReportDataSource : {}", reportDataSourceDTO);

        return reportDataSourceRepository
            .findById(reportDataSourceDTO.getRid())
            .map(existingReportDataSource -> {
                reportDataSourceMapper.partialUpdate(existingReportDataSource, reportDataSourceDTO);

                return existingReportDataSource;
            })
            .flatMap(reportDataSourceRepository::save)
            .map(reportDataSourceMapper::toDto);
    }

    /**
     * Get all the reportDataSources.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportDataSourceDTO> findAll() {
        log.debug("Request to get all ReportDataSources");
        return reportDataSourceRepository.findAll().map(reportDataSourceMapper::toDto);
    }

    /**
     *  Get all the reportDataSources where Report is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportDataSourceDTO> findAllWhereReportIsNull() {
        log.debug("Request to get all reportDataSources where Report is null");
        return reportDataSourceRepository.findAllWhereReportIsNull().map(reportDataSourceMapper::toDto);
    }

    /**
     * Returns the number of reportDataSources available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reportDataSourceRepository.count();
    }

    /**
     * Get one reportDataSource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReportDataSourceDTO> findOne(String id) {
        log.debug("Request to get ReportDataSource : {}", id);
        return reportDataSourceRepository.findById(id).map(reportDataSourceMapper::toDto);
    }

    /**
     * Delete the reportDataSource by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ReportDataSource : {}", id);
        return reportDataSourceRepository.deleteById(id);
    }
}
