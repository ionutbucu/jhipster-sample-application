package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.ReportDistributionRepository;
import com.mycompany.myapp.service.dto.ReportDistributionDTO;
import com.mycompany.myapp.service.mapper.ReportDistributionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ReportDistribution}.
 */
@Service
@Transactional
public class ReportDistributionService {

    private final Logger log = LoggerFactory.getLogger(ReportDistributionService.class);

    private final ReportDistributionRepository reportDistributionRepository;

    private final ReportDistributionMapper reportDistributionMapper;

    public ReportDistributionService(
        ReportDistributionRepository reportDistributionRepository,
        ReportDistributionMapper reportDistributionMapper
    ) {
        this.reportDistributionRepository = reportDistributionRepository;
        this.reportDistributionMapper = reportDistributionMapper;
    }

    /**
     * Save a reportDistribution.
     *
     * @param reportDistributionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportDistributionDTO> save(ReportDistributionDTO reportDistributionDTO) {
        log.debug("Request to save ReportDistribution : {}", reportDistributionDTO);
        return reportDistributionRepository
            .save(reportDistributionMapper.toEntity(reportDistributionDTO))
            .map(reportDistributionMapper::toDto);
    }

    /**
     * Update a reportDistribution.
     *
     * @param reportDistributionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportDistributionDTO> update(ReportDistributionDTO reportDistributionDTO) {
        log.debug("Request to update ReportDistribution : {}", reportDistributionDTO);
        return reportDistributionRepository
            .save(reportDistributionMapper.toEntity(reportDistributionDTO).setIsPersisted())
            .map(reportDistributionMapper::toDto);
    }

    /**
     * Partially update a reportDistribution.
     *
     * @param reportDistributionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReportDistributionDTO> partialUpdate(ReportDistributionDTO reportDistributionDTO) {
        log.debug("Request to partially update ReportDistribution : {}", reportDistributionDTO);

        return reportDistributionRepository
            .findById(reportDistributionDTO.getRid())
            .map(existingReportDistribution -> {
                reportDistributionMapper.partialUpdate(existingReportDistribution, reportDistributionDTO);

                return existingReportDistribution;
            })
            .flatMap(reportDistributionRepository::save)
            .map(reportDistributionMapper::toDto);
    }

    /**
     * Get all the reportDistributions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportDistributionDTO> findAll() {
        log.debug("Request to get all ReportDistributions");
        return reportDistributionRepository.findAll().map(reportDistributionMapper::toDto);
    }

    /**
     * Returns the number of reportDistributions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reportDistributionRepository.count();
    }

    /**
     * Get one reportDistribution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReportDistributionDTO> findOne(String id) {
        log.debug("Request to get ReportDistribution : {}", id);
        return reportDistributionRepository.findById(id).map(reportDistributionMapper::toDto);
    }

    /**
     * Delete the reportDistribution by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ReportDistribution : {}", id);
        return reportDistributionRepository.deleteById(id);
    }
}
