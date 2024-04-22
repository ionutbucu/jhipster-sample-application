package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReportDistributionTestSamples.*;
import static com.mycompany.myapp.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDistributionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDistribution.class);
        ReportDistribution reportDistribution1 = getReportDistributionSample1();
        ReportDistribution reportDistribution2 = new ReportDistribution();
        assertThat(reportDistribution1).isNotEqualTo(reportDistribution2);

        reportDistribution2.setRid(reportDistribution1.getRid());
        assertThat(reportDistribution1).isEqualTo(reportDistribution2);

        reportDistribution2 = getReportDistributionSample2();
        assertThat(reportDistribution1).isNotEqualTo(reportDistribution2);
    }

    @Test
    void reportTest() throws Exception {
        ReportDistribution reportDistribution = getReportDistributionRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportDistribution.setReport(reportBack);
        assertThat(reportDistribution.getReport()).isEqualTo(reportBack);

        reportDistribution.report(null);
        assertThat(reportDistribution.getReport()).isNull();
    }
}
