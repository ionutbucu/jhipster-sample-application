package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ReportColumnMappingAsserts.*;
import static com.mycompany.myapp.domain.ReportColumnMappingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportColumnMappingMapperTest {

    private ReportColumnMappingMapper reportColumnMappingMapper;

    @BeforeEach
    void setUp() {
        reportColumnMappingMapper = new ReportColumnMappingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportColumnMappingSample1();
        var actual = reportColumnMappingMapper.toEntity(reportColumnMappingMapper.toDto(expected));
        assertReportColumnMappingAllPropertiesEquals(expected, actual);
    }
}
