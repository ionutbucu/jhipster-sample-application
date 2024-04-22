package com.mycompany.myapp.domain;

import java.util.UUID;

public class ReportDistributionTestSamples {

    public static ReportDistribution getReportDistributionSample1() {
        return new ReportDistribution().rid("rid1").email("email1").description("description1");
    }

    public static ReportDistribution getReportDistributionSample2() {
        return new ReportDistribution().rid("rid2").email("email2").description("description2");
    }

    public static ReportDistribution getReportDistributionRandomSampleGenerator() {
        return new ReportDistribution()
            .rid(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
