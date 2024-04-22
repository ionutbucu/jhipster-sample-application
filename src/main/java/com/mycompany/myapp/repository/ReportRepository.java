package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Report entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportRepository extends ReactiveCrudRepository<Report, String>, ReportRepositoryInternal {
    Flux<Report> findAllBy(Pageable pageable);

    @Query("SELECT * FROM report entity WHERE entity.datasource_rid = :id")
    Flux<Report> findByDatasource(String id);

    @Query("SELECT * FROM report entity WHERE entity.datasource_rid IS NULL")
    Flux<Report> findAllWhereDatasourceIsNull();

    @Query("SELECT * FROM report entity WHERE entity.metadata_rid = :id")
    Flux<Report> findByMetadata(String id);

    @Query("SELECT * FROM report entity WHERE entity.metadata_rid IS NULL")
    Flux<Report> findAllWhereMetadataIsNull();

    @Override
    <S extends Report> Mono<S> save(S entity);

    @Override
    Flux<Report> findAll();

    @Override
    Mono<Report> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface ReportRepositoryInternal {
    <S extends Report> Mono<S> save(S entity);

    Flux<Report> findAllBy(Pageable pageable);

    Flux<Report> findAll();

    Mono<Report> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Report> findAllBy(Pageable pageable, Criteria criteria);
}
