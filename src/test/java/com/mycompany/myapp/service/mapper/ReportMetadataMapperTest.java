package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ReportMetadataAsserts.*;
import static com.mycompany.myapp.domain.ReportMetadataTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportMetadataMapperTest {

    private ReportMetadataMapper reportMetadataMapper;

    @BeforeEach
    void setUp() {
        reportMetadataMapper = new ReportMetadataMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportMetadataSample1();
        var actual = reportMetadataMapper.toEntity(reportMetadataMapper.toDto(expected));
        assertReportMetadataAllPropertiesEquals(expected, actual);
    }
}
