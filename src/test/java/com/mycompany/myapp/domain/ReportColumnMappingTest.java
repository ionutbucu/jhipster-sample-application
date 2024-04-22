package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReportColumnMappingTestSamples.*;
import static com.mycompany.myapp.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportColumnMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportColumnMapping.class);
        ReportColumnMapping reportColumnMapping1 = getReportColumnMappingSample1();
        ReportColumnMapping reportColumnMapping2 = new ReportColumnMapping();
        assertThat(reportColumnMapping1).isNotEqualTo(reportColumnMapping2);

        reportColumnMapping2.setRid(reportColumnMapping1.getRid());
        assertThat(reportColumnMapping1).isEqualTo(reportColumnMapping2);

        reportColumnMapping2 = getReportColumnMappingSample2();
        assertThat(reportColumnMapping1).isNotEqualTo(reportColumnMapping2);
    }

    @Test
    void reportTest() throws Exception {
        ReportColumnMapping reportColumnMapping = getReportColumnMappingRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportColumnMapping.setReport(reportBack);
        assertThat(reportColumnMapping.getReport()).isEqualTo(reportBack);

        reportColumnMapping.report(null);
        assertThat(reportColumnMapping.getReport()).isNull();
    }
}
