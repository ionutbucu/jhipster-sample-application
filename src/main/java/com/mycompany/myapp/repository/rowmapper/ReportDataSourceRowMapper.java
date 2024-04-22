package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ReportDataSource;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReportDataSource}, with proper type conversions.
 */
@Service
public class ReportDataSourceRowMapper implements BiFunction<Row, String, ReportDataSource> {

    private final ColumnConverter converter;

    public ReportDataSourceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReportDataSource} stored in the database.
     */
    @Override
    public ReportDataSource apply(Row row, String prefix) {
        ReportDataSource entity = new ReportDataSource();
        entity.setRid(converter.fromRow(row, prefix + "_rid", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        entity.setUser(converter.fromRow(row, prefix + "_jhi_user", String.class));
        entity.setPassword(converter.fromRow(row, prefix + "_password", String.class));
        return entity;
    }
}
