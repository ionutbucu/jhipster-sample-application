package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReportScheduleTestSamples.*;
import static com.mycompany.myapp.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportScheduleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportSchedule.class);
        ReportSchedule reportSchedule1 = getReportScheduleSample1();
        ReportSchedule reportSchedule2 = new ReportSchedule();
        assertThat(reportSchedule1).isNotEqualTo(reportSchedule2);

        reportSchedule2.setRid(reportSchedule1.getRid());
        assertThat(reportSchedule1).isEqualTo(reportSchedule2);

        reportSchedule2 = getReportScheduleSample2();
        assertThat(reportSchedule1).isNotEqualTo(reportSchedule2);
    }

    @Test
    void reportTest() throws Exception {
        ReportSchedule reportSchedule = getReportScheduleRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportSchedule.setReport(reportBack);
        assertThat(reportSchedule.getReport()).isEqualTo(reportBack);

        reportSchedule.report(null);
        assertThat(reportSchedule.getReport()).isNull();
    }
}
