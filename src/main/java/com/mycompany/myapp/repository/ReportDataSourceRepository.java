package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportDataSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ReportDataSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportDataSourceRepository extends ReactiveCrudRepository<ReportDataSource, String>, ReportDataSourceRepositoryInternal {
    @Query("SELECT * FROM report_data_source entity WHERE entity.rid not in (select report_rid from report)")
    Flux<ReportDataSource> findAllWhereReportIsNull();

    @Override
    <S extends ReportDataSource> Mono<S> save(S entity);

    @Override
    Flux<ReportDataSource> findAll();

    @Override
    Mono<ReportDataSource> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface ReportDataSourceRepositoryInternal {
    <S extends ReportDataSource> Mono<S> save(S entity);

    Flux<ReportDataSource> findAllBy(Pageable pageable);

    Flux<ReportDataSource> findAll();

    Mono<ReportDataSource> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReportDataSource> findAllBy(Pageable pageable, Criteria criteria);
}
