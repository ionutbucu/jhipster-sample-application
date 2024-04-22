package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ReportParam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportParamRepository extends ReactiveCrudRepository<ReportParam, String>, ReportParamRepositoryInternal {
    @Query("SELECT * FROM report_param entity WHERE entity.report_rid = :id")
    Flux<ReportParam> findByReport(String id);

    @Query("SELECT * FROM report_param entity WHERE entity.report_rid IS NULL")
    Flux<ReportParam> findAllWhereReportIsNull();

    @Override
    <S extends ReportParam> Mono<S> save(S entity);

    @Override
    Flux<ReportParam> findAll();

    @Override
    Mono<ReportParam> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface ReportParamRepositoryInternal {
    <S extends ReportParam> Mono<S> save(S entity);

    Flux<ReportParam> findAllBy(Pageable pageable);

    Flux<ReportParam> findAll();

    Mono<ReportParam> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReportParam> findAllBy(Pageable pageable, Criteria criteria);
}
