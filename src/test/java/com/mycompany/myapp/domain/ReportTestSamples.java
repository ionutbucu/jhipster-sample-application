package com.mycompany.myapp.domain;

import java.util.UUID;

public class ReportTestSamples {

    public static Report getReportSample1() {
        return new Report()
            .rid("rid1")
            .cid("cid1")
            .name("name1")
            .description("description1")
            .query("query1")
            .fileName("fileName1")
            .licenseHolder("licenseHolder1")
            .owner("owner1");
    }

    public static Report getReportSample2() {
        return new Report()
            .rid("rid2")
            .cid("cid2")
            .name("name2")
            .description("description2")
            .query("query2")
            .fileName("fileName2")
            .licenseHolder("licenseHolder2")
            .owner("owner2");
    }

    public static Report getReportRandomSampleGenerator() {
        return new Report()
            .rid(UUID.randomUUID().toString())
            .cid(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .query(UUID.randomUUID().toString())
            .fileName(UUID.randomUUID().toString())
            .licenseHolder(UUID.randomUUID().toString())
            .owner(UUID.randomUUID().toString());
    }
}
