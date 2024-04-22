package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportDistribution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ReportDistribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportDistributionRepository
    extends ReactiveCrudRepository<ReportDistribution, String>, ReportDistributionRepositoryInternal {
    @Query("SELECT * FROM report_distribution entity WHERE entity.report_rid = :id")
    Flux<ReportDistribution> findByReport(String id);

    @Query("SELECT * FROM report_distribution entity WHERE entity.report_rid IS NULL")
    Flux<ReportDistribution> findAllWhereReportIsNull();

    @Override
    <S extends ReportDistribution> Mono<S> save(S entity);

    @Override
    Flux<ReportDistribution> findAll();

    @Override
    Mono<ReportDistribution> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface ReportDistributionRepositoryInternal {
    <S extends ReportDistribution> Mono<S> save(S entity);

    Flux<ReportDistribution> findAllBy(Pageable pageable);

    Flux<ReportDistribution> findAll();

    Mono<ReportDistribution> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReportDistribution> findAllBy(Pageable pageable, Criteria criteria);
}
