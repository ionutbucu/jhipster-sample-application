package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportColumnMapping;
import com.mycompany.myapp.repository.rowmapper.ReportColumnMappingRowMapper;
import com.mycompany.myapp.repository.rowmapper.ReportRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the ReportColumnMapping entity.
 */
@SuppressWarnings("unused")
class ReportColumnMappingRepositoryInternalImpl
    extends SimpleR2dbcRepository<ReportColumnMapping, String>
    implements ReportColumnMappingRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ReportRowMapper reportMapper;
    private final ReportColumnMappingRowMapper reportcolumnmappingMapper;

    private static final Table entityTable = Table.aliased("report_column_mapping", EntityManager.ENTITY_ALIAS);
    private static final Table reportTable = Table.aliased("report", "report");

    public ReportColumnMappingRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ReportRowMapper reportMapper,
        ReportColumnMappingRowMapper reportcolumnmappingMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ReportColumnMapping.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.reportMapper = reportMapper;
        this.reportcolumnmappingMapper = reportcolumnmappingMapper;
    }

    @Override
    public Flux<ReportColumnMapping> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ReportColumnMapping> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ReportColumnMappingSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ReportSqlHelper.getColumns(reportTable, "report"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(reportTable)
            .on(Column.create("report_rid", entityTable))
            .equals(Column.create("rid", reportTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ReportColumnMapping.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ReportColumnMapping> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ReportColumnMapping> findById(String id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("rid"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ReportColumnMapping process(Row row, RowMetadata metadata) {
        ReportColumnMapping entity = reportcolumnmappingMapper.apply(row, "e");
        entity.setReport(reportMapper.apply(row, "report"));
        return entity;
    }

    @Override
    public <S extends ReportColumnMapping> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
