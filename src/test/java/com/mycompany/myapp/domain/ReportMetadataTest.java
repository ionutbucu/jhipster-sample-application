package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReportMetadataTestSamples.*;
import static com.mycompany.myapp.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportMetadataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportMetadata.class);
        ReportMetadata reportMetadata1 = getReportMetadataSample1();
        ReportMetadata reportMetadata2 = new ReportMetadata();
        assertThat(reportMetadata1).isNotEqualTo(reportMetadata2);

        reportMetadata2.setRid(reportMetadata1.getRid());
        assertThat(reportMetadata1).isEqualTo(reportMetadata2);

        reportMetadata2 = getReportMetadataSample2();
        assertThat(reportMetadata1).isNotEqualTo(reportMetadata2);
    }

    @Test
    void reportTest() throws Exception {
        ReportMetadata reportMetadata = getReportMetadataRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportMetadata.setReport(reportBack);
        assertThat(reportMetadata.getReport()).isEqualTo(reportBack);
        assertThat(reportBack.getMetadata()).isEqualTo(reportMetadata);

        reportMetadata.report(null);
        assertThat(reportMetadata.getReport()).isNull();
        assertThat(reportBack.getMetadata()).isNull();
    }
}
