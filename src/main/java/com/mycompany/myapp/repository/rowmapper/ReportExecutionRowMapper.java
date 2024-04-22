package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ReportExecution;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReportExecution}, with proper type conversions.
 */
@Service
public class ReportExecutionRowMapper implements BiFunction<Row, String, ReportExecution> {

    private final ColumnConverter converter;

    public ReportExecutionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReportExecution} stored in the database.
     */
    @Override
    public ReportExecution apply(Row row, String prefix) {
        ReportExecution entity = new ReportExecution();
        entity.setRid(converter.fromRow(row, prefix + "_rid", String.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", Instant.class));
        entity.setError(converter.fromRow(row, prefix + "_error", String.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        entity.setUser(converter.fromRow(row, prefix + "_jhi_user", String.class));
        entity.setAdditionalInfo(converter.fromRow(row, prefix + "_additional_info", String.class));
        entity.setReportId(converter.fromRow(row, prefix + "_report_rid", String.class));
        return entity;
    }
}
