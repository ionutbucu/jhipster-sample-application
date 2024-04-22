package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ReportDistribution;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReportDistribution}, with proper type conversions.
 */
@Service
public class ReportDistributionRowMapper implements BiFunction<Row, String, ReportDistribution> {

    private final ColumnConverter converter;

    public ReportDistributionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReportDistribution} stored in the database.
     */
    @Override
    public ReportDistribution apply(Row row, String prefix) {
        ReportDistribution entity = new ReportDistribution();
        entity.setRid(converter.fromRow(row, prefix + "_rid", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setReportId(converter.fromRow(row, prefix + "_report_rid", String.class));
        return entity;
    }
}
