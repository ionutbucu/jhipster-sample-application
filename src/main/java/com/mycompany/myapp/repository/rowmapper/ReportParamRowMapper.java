package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ReportParam;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReportParam}, with proper type conversions.
 */
@Service
public class ReportParamRowMapper implements BiFunction<Row, String, ReportParam> {

    private final ColumnConverter converter;

    public ReportParamRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReportParam} stored in the database.
     */
    @Override
    public ReportParam apply(Row row, String prefix) {
        ReportParam entity = new ReportParam();
        entity.setRid(converter.fromRow(row, prefix + "_rid", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", String.class));
        entity.setConversionRule(converter.fromRow(row, prefix + "_conversion_rule", String.class));
        entity.setReportId(converter.fromRow(row, prefix + "_report_rid", String.class));
        return entity;
    }
}
