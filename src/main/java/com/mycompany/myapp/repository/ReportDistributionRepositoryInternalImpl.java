package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportDistribution;
import com.mycompany.myapp.repository.rowmapper.ReportDistributionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ReportDistribution entity.
 */
@SuppressWarnings("unused")
class ReportDistributionRepositoryInternalImpl
    extends SimpleR2dbcRepository<ReportDistribution, String>
    implements ReportDistributionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ReportRowMapper reportMapper;
    private final ReportDistributionRowMapper reportdistributionMapper;

    private static final Table entityTable = Table.aliased("report_distribution", EntityManager.ENTITY_ALIAS);
    private static final Table reportTable = Table.aliased("report", "report");

    public ReportDistributionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ReportRowMapper reportMapper,
        ReportDistributionRowMapper reportdistributionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ReportDistribution.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.reportMapper = reportMapper;
        this.reportdistributionMapper = reportdistributionMapper;
    }

    @Override
    public Flux<ReportDistribution> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ReportDistribution> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ReportDistributionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ReportSqlHelper.getColumns(reportTable, "report"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(reportTable)
            .on(Column.create("report_rid", entityTable))
            .equals(Column.create("rid", reportTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ReportDistribution.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ReportDistribution> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ReportDistribution> findById(String id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("rid"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ReportDistribution process(Row row, RowMetadata metadata) {
        ReportDistribution entity = reportdistributionMapper.apply(row, "e");
        entity.setReport(reportMapper.apply(row, "report"));
        return entity;
    }

    @Override
    public <S extends ReportDistribution> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
