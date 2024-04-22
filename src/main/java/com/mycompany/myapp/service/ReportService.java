package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.ReportRepository;
import com.mycompany.myapp.service.dto.ReportDTO;
import com.mycompany.myapp.service.mapper.ReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Report}.
 */
@Service
@Transactional
public class ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    public ReportService(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    /**
     * Save a report.
     *
     * @param reportDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportDTO> save(ReportDTO reportDTO) {
        log.debug("Request to save Report : {}", reportDTO);
        return reportRepository.save(reportMapper.toEntity(reportDTO)).map(reportMapper::toDto);
    }

    /**
     * Update a report.
     *
     * @param reportDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportDTO> update(ReportDTO reportDTO) {
        log.debug("Request to update Report : {}", reportDTO);
        return reportRepository.save(reportMapper.toEntity(reportDTO).setIsPersisted()).map(reportMapper::toDto);
    }

    /**
     * Partially update a report.
     *
     * @param reportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReportDTO> partialUpdate(ReportDTO reportDTO) {
        log.debug("Request to partially update Report : {}", reportDTO);

        return reportRepository
            .findById(reportDTO.getRid())
            .map(existingReport -> {
                reportMapper.partialUpdate(existingReport, reportDTO);

                return existingReport;
            })
            .flatMap(reportRepository::save)
            .map(reportMapper::toDto);
    }

    /**
     * Get all the reports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reports");
        return reportRepository.findAllBy(pageable).map(reportMapper::toDto);
    }

    /**
     * Returns the number of reports available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reportRepository.count();
    }

    /**
     * Get one report by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReportDTO> findOne(String id) {
        log.debug("Request to get Report : {}", id);
        return reportRepository.findById(id).map(reportMapper::toDto);
    }

    /**
     * Delete the report by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Report : {}", id);
        return reportRepository.deleteById(id);
    }
}
