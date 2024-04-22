package com.mycompany.myapp.domain;

import java.util.UUID;

public class ReportMetadataTestSamples {

    public static ReportMetadata getReportMetadataSample1() {
        return new ReportMetadata().rid("rid1");
    }

    public static ReportMetadata getReportMetadataSample2() {
        return new ReportMetadata().rid("rid2");
    }

    public static ReportMetadata getReportMetadataRandomSampleGenerator() {
        return new ReportMetadata().rid(UUID.randomUUID().toString());
    }
}
