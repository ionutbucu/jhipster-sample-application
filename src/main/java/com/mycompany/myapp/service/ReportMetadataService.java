package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.ReportMetadataRepository;
import com.mycompany.myapp.service.dto.ReportMetadataDTO;
import com.mycompany.myapp.service.mapper.ReportMetadataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ReportMetadata}.
 */
@Service
@Transactional
public class ReportMetadataService {

    private final Logger log = LoggerFactory.getLogger(ReportMetadataService.class);

    private final ReportMetadataRepository reportMetadataRepository;

    private final ReportMetadataMapper reportMetadataMapper;

    public ReportMetadataService(ReportMetadataRepository reportMetadataRepository, ReportMetadataMapper reportMetadataMapper) {
        this.reportMetadataRepository = reportMetadataRepository;
        this.reportMetadataMapper = reportMetadataMapper;
    }

    /**
     * Save a reportMetadata.
     *
     * @param reportMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportMetadataDTO> save(ReportMetadataDTO reportMetadataDTO) {
        log.debug("Request to save ReportMetadata : {}", reportMetadataDTO);
        return reportMetadataRepository.save(reportMetadataMapper.toEntity(reportMetadataDTO)).map(reportMetadataMapper::toDto);
    }

    /**
     * Update a reportMetadata.
     *
     * @param reportMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportMetadataDTO> update(ReportMetadataDTO reportMetadataDTO) {
        log.debug("Request to update ReportMetadata : {}", reportMetadataDTO);
        return reportMetadataRepository
            .save(reportMetadataMapper.toEntity(reportMetadataDTO).setIsPersisted())
            .map(reportMetadataMapper::toDto);
    }

    /**
     * Partially update a reportMetadata.
     *
     * @param reportMetadataDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReportMetadataDTO> partialUpdate(ReportMetadataDTO reportMetadataDTO) {
        log.debug("Request to partially update ReportMetadata : {}", reportMetadataDTO);

        return reportMetadataRepository
            .findById(reportMetadataDTO.getRid())
            .map(existingReportMetadata -> {
                reportMetadataMapper.partialUpdate(existingReportMetadata, reportMetadataDTO);

                return existingReportMetadata;
            })
            .flatMap(reportMetadataRepository::save)
            .map(reportMetadataMapper::toDto);
    }

    /**
     * Get all the reportMetadata.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportMetadataDTO> findAll() {
        log.debug("Request to get all ReportMetadata");
        return reportMetadataRepository.findAll().map(reportMetadataMapper::toDto);
    }

    /**
     *  Get all the reportMetadata where Report is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportMetadataDTO> findAllWhereReportIsNull() {
        log.debug("Request to get all reportMetadata where Report is null");
        return reportMetadataRepository.findAllWhereReportIsNull().map(reportMetadataMapper::toDto);
    }

    /**
     * Returns the number of reportMetadata available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reportMetadataRepository.count();
    }

    /**
     * Get one reportMetadata by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReportMetadataDTO> findOne(String id) {
        log.debug("Request to get ReportMetadata : {}", id);
        return reportMetadataRepository.findById(id).map(reportMetadataMapper::toDto);
    }

    /**
     * Delete the reportMetadata by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ReportMetadata : {}", id);
        return reportMetadataRepository.deleteById(id);
    }
}
