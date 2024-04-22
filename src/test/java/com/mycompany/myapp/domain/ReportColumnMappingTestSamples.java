package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ReportColumnMappingTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReportColumnMapping getReportColumnMappingSample1() {
        return new ReportColumnMapping()
            .rid("rid1")
            .sourceColumnName("sourceColumnName1")
            .sourceColumnIndex(1)
            .columnTitle("columnTitle1")
            .lang("lang1");
    }

    public static ReportColumnMapping getReportColumnMappingSample2() {
        return new ReportColumnMapping()
            .rid("rid2")
            .sourceColumnName("sourceColumnName2")
            .sourceColumnIndex(2)
            .columnTitle("columnTitle2")
            .lang("lang2");
    }

    public static ReportColumnMapping getReportColumnMappingRandomSampleGenerator() {
        return new ReportColumnMapping()
            .rid(UUID.randomUUID().toString())
            .sourceColumnName(UUID.randomUUID().toString())
            .sourceColumnIndex(intCount.incrementAndGet())
            .columnTitle(UUID.randomUUID().toString())
            .lang(UUID.randomUUID().toString());
    }
}
