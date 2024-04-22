package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ReportExecutionAsserts.*;
import static com.mycompany.myapp.domain.ReportExecutionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportExecutionMapperTest {

    private ReportExecutionMapper reportExecutionMapper;

    @BeforeEach
    void setUp() {
        reportExecutionMapper = new ReportExecutionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportExecutionSample1();
        var actual = reportExecutionMapper.toEntity(reportExecutionMapper.toDto(expected));
        assertReportExecutionAllPropertiesEquals(expected, actual);
    }
}
