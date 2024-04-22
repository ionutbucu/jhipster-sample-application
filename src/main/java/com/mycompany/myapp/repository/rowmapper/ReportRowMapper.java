package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.domain.enumeration.QueryType;
import com.mycompany.myapp.domain.enumeration.ReportType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Report}, with proper type conversions.
 */
@Service
public class ReportRowMapper implements BiFunction<Row, String, Report> {

    private final ColumnConverter converter;

    public ReportRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Report} stored in the database.
     */
    @Override
    public Report apply(Row row, String prefix) {
        Report entity = new Report();
        entity.setRid(converter.fromRow(row, prefix + "_rid", String.class));
        entity.setCid(converter.fromRow(row, prefix + "_cid", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setQuery(converter.fromRow(row, prefix + "_query", String.class));
        entity.setQueryType(converter.fromRow(row, prefix + "_query_type", QueryType.class));
        entity.setFileName(converter.fromRow(row, prefix + "_file_name", String.class));
        entity.setReportType(converter.fromRow(row, prefix + "_report_type", ReportType.class));
        entity.setLicenseHolder(converter.fromRow(row, prefix + "_license_holder", String.class));
        entity.setOwner(converter.fromRow(row, prefix + "_owner", String.class));
        entity.setDatasourceId(converter.fromRow(row, prefix + "_datasource_rid", String.class));
        entity.setMetadataId(converter.fromRow(row, prefix + "_metadata_rid", String.class));
        return entity;
    }
}
