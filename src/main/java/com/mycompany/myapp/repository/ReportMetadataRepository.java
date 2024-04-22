package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportMetadata;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ReportMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportMetadataRepository extends ReactiveCrudRepository<ReportMetadata, String>, ReportMetadataRepositoryInternal {
    @Query("SELECT * FROM report_metadata entity WHERE entity.rid not in (select report_rid from report)")
    Flux<ReportMetadata> findAllWhereReportIsNull();

    @Override
    <S extends ReportMetadata> Mono<S> save(S entity);

    @Override
    Flux<ReportMetadata> findAll();

    @Override
    Mono<ReportMetadata> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface ReportMetadataRepositoryInternal {
    <S extends ReportMetadata> Mono<S> save(S entity);

    Flux<ReportMetadata> findAllBy(Pageable pageable);

    Flux<ReportMetadata> findAll();

    Mono<ReportMetadata> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReportMetadata> findAllBy(Pageable pageable, Criteria criteria);
}
