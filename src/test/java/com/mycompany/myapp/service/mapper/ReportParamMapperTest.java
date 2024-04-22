package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ReportParamAsserts.*;
import static com.mycompany.myapp.domain.ReportParamTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportParamMapperTest {

    private ReportParamMapper reportParamMapper;

    @BeforeEach
    void setUp() {
        reportParamMapper = new ReportParamMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportParamSample1();
        var actual = reportParamMapper.toEntity(reportParamMapper.toDto(expected));
        assertReportParamAllPropertiesEquals(expected, actual);
    }
}
