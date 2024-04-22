package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ReportDataSourceAsserts.*;
import static com.mycompany.myapp.domain.ReportDataSourceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportDataSourceMapperTest {

    private ReportDataSourceMapper reportDataSourceMapper;

    @BeforeEach
    void setUp() {
        reportDataSourceMapper = new ReportDataSourceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportDataSourceSample1();
        var actual = reportDataSourceMapper.toEntity(reportDataSourceMapper.toDto(expected));
        assertReportDataSourceAllPropertiesEquals(expected, actual);
    }
}
