package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ReportColumnMapping;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReportColumnMapping}, with proper type conversions.
 */
@Service
public class ReportColumnMappingRowMapper implements BiFunction<Row, String, ReportColumnMapping> {

    private final ColumnConverter converter;

    public ReportColumnMappingRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReportColumnMapping} stored in the database.
     */
    @Override
    public ReportColumnMapping apply(Row row, String prefix) {
        ReportColumnMapping entity = new ReportColumnMapping();
        entity.setRid(converter.fromRow(row, prefix + "_rid", String.class));
        entity.setSourceColumnName(converter.fromRow(row, prefix + "_source_column_name", String.class));
        entity.setSourceColumnIndex(converter.fromRow(row, prefix + "_source_column_index", Integer.class));
        entity.setColumnTitle(converter.fromRow(row, prefix + "_column_title", String.class));
        entity.setLang(converter.fromRow(row, prefix + "_lang", String.class));
        entity.setReportId(converter.fromRow(row, prefix + "_report_rid", String.class));
        return entity;
    }
}
