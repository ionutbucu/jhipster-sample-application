package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportExecution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ReportExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportExecutionRepository extends ReactiveCrudRepository<ReportExecution, String>, ReportExecutionRepositoryInternal {
    Flux<ReportExecution> findAllBy(Pageable pageable);

    @Query("SELECT * FROM report_execution entity WHERE entity.report_rid = :id")
    Flux<ReportExecution> findByReport(String id);

    @Query("SELECT * FROM report_execution entity WHERE entity.report_rid IS NULL")
    Flux<ReportExecution> findAllWhereReportIsNull();

    @Override
    <S extends ReportExecution> Mono<S> save(S entity);

    @Override
    Flux<ReportExecution> findAll();

    @Override
    Mono<ReportExecution> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface ReportExecutionRepositoryInternal {
    <S extends ReportExecution> Mono<S> save(S entity);

    Flux<ReportExecution> findAllBy(Pageable pageable);

    Flux<ReportExecution> findAll();

    Mono<ReportExecution> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReportExecution> findAllBy(Pageable pageable, Criteria criteria);
}
