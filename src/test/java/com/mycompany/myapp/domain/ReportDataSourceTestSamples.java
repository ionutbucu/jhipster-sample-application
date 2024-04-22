package com.mycompany.myapp.domain;

import java.util.UUID;

public class ReportDataSourceTestSamples {

    public static ReportDataSource getReportDataSourceSample1() {
        return new ReportDataSource().rid("rid1").type("type1").url("url1").user("user1").password("password1");
    }

    public static ReportDataSource getReportDataSourceSample2() {
        return new ReportDataSource().rid("rid2").type("type2").url("url2").user("user2").password("password2");
    }

    public static ReportDataSource getReportDataSourceRandomSampleGenerator() {
        return new ReportDataSource()
            .rid(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .user(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString());
    }
}
