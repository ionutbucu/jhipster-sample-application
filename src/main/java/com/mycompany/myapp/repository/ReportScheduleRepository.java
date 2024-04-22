package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ReportSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportScheduleRepository extends ReactiveCrudRepository<ReportSchedule, String>, ReportScheduleRepositoryInternal {
    @Query("SELECT * FROM report_schedule entity WHERE entity.report_rid = :id")
    Flux<ReportSchedule> findByReport(String id);

    @Query("SELECT * FROM report_schedule entity WHERE entity.report_rid IS NULL")
    Flux<ReportSchedule> findAllWhereReportIsNull();

    @Override
    <S extends ReportSchedule> Mono<S> save(S entity);

    @Override
    Flux<ReportSchedule> findAll();

    @Override
    Mono<ReportSchedule> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface ReportScheduleRepositoryInternal {
    <S extends ReportSchedule> Mono<S> save(S entity);

    Flux<ReportSchedule> findAllBy(Pageable pageable);

    Flux<ReportSchedule> findAll();

    Mono<ReportSchedule> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReportSchedule> findAllBy(Pageable pageable, Criteria criteria);
}
