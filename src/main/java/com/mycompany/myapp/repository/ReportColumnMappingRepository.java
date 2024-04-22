package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportColumnMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ReportColumnMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportColumnMappingRepository
    extends ReactiveCrudRepository<ReportColumnMapping, String>, ReportColumnMappingRepositoryInternal {
    @Query("SELECT * FROM report_column_mapping entity WHERE entity.report_rid = :id")
    Flux<ReportColumnMapping> findByReport(String id);

    @Query("SELECT * FROM report_column_mapping entity WHERE entity.report_rid IS NULL")
    Flux<ReportColumnMapping> findAllWhereReportIsNull();

    @Override
    <S extends ReportColumnMapping> Mono<S> save(S entity);

    @Override
    Flux<ReportColumnMapping> findAll();

    @Override
    Mono<ReportColumnMapping> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface ReportColumnMappingRepositoryInternal {
    <S extends ReportColumnMapping> Mono<S> save(S entity);

    Flux<ReportColumnMapping> findAllBy(Pageable pageable);

    Flux<ReportColumnMapping> findAll();

    Mono<ReportColumnMapping> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReportColumnMapping> findAllBy(Pageable pageable, Criteria criteria);
}
