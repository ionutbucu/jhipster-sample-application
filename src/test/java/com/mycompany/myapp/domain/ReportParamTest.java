package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReportParamTestSamples.*;
import static com.mycompany.myapp.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportParamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportParam.class);
        ReportParam reportParam1 = getReportParamSample1();
        ReportParam reportParam2 = new ReportParam();
        assertThat(reportParam1).isNotEqualTo(reportParam2);

        reportParam2.setRid(reportParam1.getRid());
        assertThat(reportParam1).isEqualTo(reportParam2);

        reportParam2 = getReportParamSample2();
        assertThat(reportParam1).isNotEqualTo(reportParam2);
    }

    @Test
    void reportTest() throws Exception {
        ReportParam reportParam = getReportParamRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportParam.setReport(reportBack);
        assertThat(reportParam.getReport()).isEqualTo(reportBack);

        reportParam.report(null);
        assertThat(reportParam.getReport()).isNull();
    }
}
