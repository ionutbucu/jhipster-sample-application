package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReportColumnMappingTestSamples.*;
import static com.mycompany.myapp.domain.ReportDataSourceTestSamples.*;
import static com.mycompany.myapp.domain.ReportDistributionTestSamples.*;
import static com.mycompany.myapp.domain.ReportExecutionTestSamples.*;
import static com.mycompany.myapp.domain.ReportMetadataTestSamples.*;
import static com.mycompany.myapp.domain.ReportParamTestSamples.*;
import static com.mycompany.myapp.domain.ReportScheduleTestSamples.*;
import static com.mycompany.myapp.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Report.class);
        Report report1 = getReportSample1();
        Report report2 = new Report();
        assertThat(report1).isNotEqualTo(report2);

        report2.setRid(report1.getRid());
        assertThat(report1).isEqualTo(report2);

        report2 = getReportSample2();
        assertThat(report1).isNotEqualTo(report2);
    }

    @Test
    void datasourceTest() throws Exception {
        Report report = getReportRandomSampleGenerator();
        ReportDataSource reportDataSourceBack = getReportDataSourceRandomSampleGenerator();

        report.setDatasource(reportDataSourceBack);
        assertThat(report.getDatasource()).isEqualTo(reportDataSourceBack);

        report.datasource(null);
        assertThat(report.getDatasource()).isNull();
    }

    @Test
    void metadataTest() throws Exception {
        Report report = getReportRandomSampleGenerator();
        ReportMetadata reportMetadataBack = getReportMetadataRandomSampleGenerator();

        report.setMetadata(reportMetadataBack);
        assertThat(report.getMetadata()).isEqualTo(reportMetadataBack);

        report.metadata(null);
        assertThat(report.getMetadata()).isNull();
    }

    @Test
    void schedulesTest() throws Exception {
        Report report = getReportRandomSampleGenerator();
        ReportSchedule reportScheduleBack = getReportScheduleRandomSampleGenerator();

        report.addSchedules(reportScheduleBack);
        assertThat(report.getSchedules()).containsOnly(reportScheduleBack);
        assertThat(reportScheduleBack.getReport()).isEqualTo(report);

        report.removeSchedules(reportScheduleBack);
        assertThat(report.getSchedules()).doesNotContain(reportScheduleBack);
        assertThat(reportScheduleBack.getReport()).isNull();

        report.schedules(new HashSet<>(Set.of(reportScheduleBack)));
        assertThat(report.getSchedules()).containsOnly(reportScheduleBack);
        assertThat(reportScheduleBack.getReport()).isEqualTo(report);

        report.setSchedules(new HashSet<>());
        assertThat(report.getSchedules()).doesNotContain(reportScheduleBack);
        assertThat(reportScheduleBack.getReport()).isNull();
    }

    @Test
    void distributionsTest() throws Exception {
        Report report = getReportRandomSampleGenerator();
        ReportDistribution reportDistributionBack = getReportDistributionRandomSampleGenerator();

        report.addDistributions(reportDistributionBack);
        assertThat(report.getDistributions()).containsOnly(reportDistributionBack);
        assertThat(reportDistributionBack.getReport()).isEqualTo(report);

        report.removeDistributions(reportDistributionBack);
        assertThat(report.getDistributions()).doesNotContain(reportDistributionBack);
        assertThat(reportDistributionBack.getReport()).isNull();

        report.distributions(new HashSet<>(Set.of(reportDistributionBack)));
        assertThat(report.getDistributions()).containsOnly(reportDistributionBack);
        assertThat(reportDistributionBack.getReport()).isEqualTo(report);

        report.setDistributions(new HashSet<>());
        assertThat(report.getDistributions()).doesNotContain(reportDistributionBack);
        assertThat(reportDistributionBack.getReport()).isNull();
    }

    @Test
    void executionsTest() throws Exception {
        Report report = getReportRandomSampleGenerator();
        ReportExecution reportExecutionBack = getReportExecutionRandomSampleGenerator();

        report.addExecutions(reportExecutionBack);
        assertThat(report.getExecutions()).containsOnly(reportExecutionBack);
        assertThat(reportExecutionBack.getReport()).isEqualTo(report);

        report.removeExecutions(reportExecutionBack);
        assertThat(report.getExecutions()).doesNotContain(reportExecutionBack);
        assertThat(reportExecutionBack.getReport()).isNull();

        report.executions(new HashSet<>(Set.of(reportExecutionBack)));
        assertThat(report.getExecutions()).containsOnly(reportExecutionBack);
        assertThat(reportExecutionBack.getReport()).isEqualTo(report);

        report.setExecutions(new HashSet<>());
        assertThat(report.getExecutions()).doesNotContain(reportExecutionBack);
        assertThat(reportExecutionBack.getReport()).isNull();
    }

    @Test
    void parametersTest() throws Exception {
        Report report = getReportRandomSampleGenerator();
        ReportParam reportParamBack = getReportParamRandomSampleGenerator();

        report.addParameters(reportParamBack);
        assertThat(report.getParameters()).containsOnly(reportParamBack);
        assertThat(reportParamBack.getReport()).isEqualTo(report);

        report.removeParameters(reportParamBack);
        assertThat(report.getParameters()).doesNotContain(reportParamBack);
        assertThat(reportParamBack.getReport()).isNull();

        report.parameters(new HashSet<>(Set.of(reportParamBack)));
        assertThat(report.getParameters()).containsOnly(reportParamBack);
        assertThat(reportParamBack.getReport()).isEqualTo(report);

        report.setParameters(new HashSet<>());
        assertThat(report.getParameters()).doesNotContain(reportParamBack);
        assertThat(reportParamBack.getReport()).isNull();
    }

    @Test
    void columnsTest() throws Exception {
        Report report = getReportRandomSampleGenerator();
        ReportColumnMapping reportColumnMappingBack = getReportColumnMappingRandomSampleGenerator();

        report.addColumns(reportColumnMappingBack);
        assertThat(report.getColumns()).containsOnly(reportColumnMappingBack);
        assertThat(reportColumnMappingBack.getReport()).isEqualTo(report);

        report.removeColumns(reportColumnMappingBack);
        assertThat(report.getColumns()).doesNotContain(reportColumnMappingBack);
        assertThat(reportColumnMappingBack.getReport()).isNull();

        report.columns(new HashSet<>(Set.of(reportColumnMappingBack)));
        assertThat(report.getColumns()).containsOnly(reportColumnMappingBack);
        assertThat(reportColumnMappingBack.getReport()).isEqualTo(report);

        report.setColumns(new HashSet<>());
        assertThat(report.getColumns()).doesNotContain(reportColumnMappingBack);
        assertThat(reportColumnMappingBack.getReport()).isNull();
    }
}
