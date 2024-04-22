package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ReportMetadata;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ReportMetadata}, with proper type conversions.
 */
@Service
public class ReportMetadataRowMapper implements BiFunction<Row, String, ReportMetadata> {

    private final ColumnConverter converter;

    public ReportMetadataRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ReportMetadata} stored in the database.
     */
    @Override
    public ReportMetadata apply(Row row, String prefix) {
        ReportMetadata entity = new ReportMetadata();
        entity.setRid(converter.fromRow(row, prefix + "_rid", String.class));
        entity.setMetadata(converter.fromRow(row, prefix + "_metadata", String.class));
        return entity;
    }
}
