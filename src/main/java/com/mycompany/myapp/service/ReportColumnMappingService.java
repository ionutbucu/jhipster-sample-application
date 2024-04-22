package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.ReportColumnMappingRepository;
import com.mycompany.myapp.service.dto.ReportColumnMappingDTO;
import com.mycompany.myapp.service.mapper.ReportColumnMappingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ReportColumnMapping}.
 */
@Service
@Transactional
public class ReportColumnMappingService {

    private final Logger log = LoggerFactory.getLogger(ReportColumnMappingService.class);

    private final ReportColumnMappingRepository reportColumnMappingRepository;

    private final ReportColumnMappingMapper reportColumnMappingMapper;

    public ReportColumnMappingService(
        ReportColumnMappingRepository reportColumnMappingRepository,
        ReportColumnMappingMapper reportColumnMappingMapper
    ) {
        this.reportColumnMappingRepository = reportColumnMappingRepository;
        this.reportColumnMappingMapper = reportColumnMappingMapper;
    }

    /**
     * Save a reportColumnMapping.
     *
     * @param reportColumnMappingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportColumnMappingDTO> save(ReportColumnMappingDTO reportColumnMappingDTO) {
        log.debug("Request to save ReportColumnMapping : {}", reportColumnMappingDTO);
        return reportColumnMappingRepository
            .save(reportColumnMappingMapper.toEntity(reportColumnMappingDTO))
            .map(reportColumnMappingMapper::toDto);
    }

    /**
     * Update a reportColumnMapping.
     *
     * @param reportColumnMappingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportColumnMappingDTO> update(ReportColumnMappingDTO reportColumnMappingDTO) {
        log.debug("Request to update ReportColumnMapping : {}", reportColumnMappingDTO);
        return reportColumnMappingRepository
            .save(reportColumnMappingMapper.toEntity(reportColumnMappingDTO).setIsPersisted())
            .map(reportColumnMappingMapper::toDto);
    }

    /**
     * Partially update a reportColumnMapping.
     *
     * @param reportColumnMappingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReportColumnMappingDTO> partialUpdate(ReportColumnMappingDTO reportColumnMappingDTO) {
        log.debug("Request to partially update ReportColumnMapping : {}", reportColumnMappingDTO);

        return reportColumnMappingRepository
            .findById(reportColumnMappingDTO.getRid())
            .map(existingReportColumnMapping -> {
                reportColumnMappingMapper.partialUpdate(existingReportColumnMapping, reportColumnMappingDTO);

                return existingReportColumnMapping;
            })
            .flatMap(reportColumnMappingRepository::save)
            .map(reportColumnMappingMapper::toDto);
    }

    /**
     * Get all the reportColumnMappings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportColumnMappingDTO> findAll() {
        log.debug("Request to get all ReportColumnMappings");
        return reportColumnMappingRepository.findAll().map(reportColumnMappingMapper::toDto);
    }

    /**
     * Returns the number of reportColumnMappings available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reportColumnMappingRepository.count();
    }

    /**
     * Get one reportColumnMapping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReportColumnMappingDTO> findOne(String id) {
        log.debug("Request to get ReportColumnMapping : {}", id);
        return reportColumnMappingRepository.findById(id).map(reportColumnMappingMapper::toDto);
    }

    /**
     * Delete the reportColumnMapping by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ReportColumnMapping : {}", id);
        return reportColumnMappingRepository.deleteById(id);
    }
}
