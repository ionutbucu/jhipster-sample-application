package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ReportSchedule;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReportSchedule}, with proper type conversions.
 */
@Service
public class ReportScheduleRowMapper implements BiFunction<Row, String, ReportSchedule> {

    private final ColumnConverter converter;

    public ReportScheduleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReportSchedule} stored in the database.
     */
    @Override
    public ReportSchedule apply(Row row, String prefix) {
        ReportSchedule entity = new ReportSchedule();
        entity.setRid(converter.fromRow(row, prefix + "_rid", String.class));
        entity.setCron(converter.fromRow(row, prefix + "_cron", String.class));
        entity.setReportId(converter.fromRow(row, prefix + "_report_rid", String.class));
        return entity;
    }
}
