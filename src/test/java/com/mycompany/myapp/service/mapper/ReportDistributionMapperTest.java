package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ReportDistributionAsserts.*;
import static com.mycompany.myapp.domain.ReportDistributionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportDistributionMapperTest {

    private ReportDistributionMapper reportDistributionMapper;

    @BeforeEach
    void setUp() {
        reportDistributionMapper = new ReportDistributionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportDistributionSample1();
        var actual = reportDistributionMapper.toEntity(reportDistributionMapper.toDto(expected));
        assertReportDistributionAllPropertiesEquals(expected, actual);
    }
}
