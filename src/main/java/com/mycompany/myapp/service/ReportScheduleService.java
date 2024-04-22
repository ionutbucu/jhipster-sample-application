package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.ReportScheduleRepository;
import com.mycompany.myapp.service.dto.ReportScheduleDTO;
import com.mycompany.myapp.service.mapper.ReportScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ReportSchedule}.
 */
@Service
@Transactional
public class ReportScheduleService {

    private final Logger log = LoggerFactory.getLogger(ReportScheduleService.class);

    private final ReportScheduleRepository reportScheduleRepository;

    private final ReportScheduleMapper reportScheduleMapper;

    public ReportScheduleService(ReportScheduleRepository reportScheduleRepository, ReportScheduleMapper reportScheduleMapper) {
        this.reportScheduleRepository = reportScheduleRepository;
        this.reportScheduleMapper = reportScheduleMapper;
    }

    /**
     * Save a reportSchedule.
     *
     * @param reportScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportScheduleDTO> save(ReportScheduleDTO reportScheduleDTO) {
        log.debug("Request to save ReportSchedule : {}", reportScheduleDTO);
        return reportScheduleRepository.save(reportScheduleMapper.toEntity(reportScheduleDTO)).map(reportScheduleMapper::toDto);
    }

    /**
     * Update a reportSchedule.
     *
     * @param reportScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReportScheduleDTO> update(ReportScheduleDTO reportScheduleDTO) {
        log.debug("Request to update ReportSchedule : {}", reportScheduleDTO);
        return reportScheduleRepository
            .save(reportScheduleMapper.toEntity(reportScheduleDTO).setIsPersisted())
            .map(reportScheduleMapper::toDto);
    }

    /**
     * Partially update a reportSchedule.
     *
     * @param reportScheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReportScheduleDTO> partialUpdate(ReportScheduleDTO reportScheduleDTO) {
        log.debug("Request to partially update ReportSchedule : {}", reportScheduleDTO);

        return reportScheduleRepository
            .findById(reportScheduleDTO.getRid())
            .map(existingReportSchedule -> {
                reportScheduleMapper.partialUpdate(existingReportSchedule, reportScheduleDTO);

                return existingReportSchedule;
            })
            .flatMap(reportScheduleRepository::save)
            .map(reportScheduleMapper::toDto);
    }

    /**
     * Get all the reportSchedules.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReportScheduleDTO> findAll() {
        log.debug("Request to get all ReportSchedules");
        return reportScheduleRepository.findAll().map(reportScheduleMapper::toDto);
    }

    /**
     * Returns the number of reportSchedules available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reportScheduleRepository.count();
    }

    /**
     * Get one reportSchedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReportScheduleDTO> findOne(String id) {
        log.debug("Request to get ReportSchedule : {}", id);
        return reportScheduleRepository.findById(id).map(reportScheduleMapper::toDto);
    }

    /**
     * Delete the reportSchedule by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ReportSchedule : {}", id);
        return reportScheduleRepository.deleteById(id);
    }
}
