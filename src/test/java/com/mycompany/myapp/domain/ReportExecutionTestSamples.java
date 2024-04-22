package com.mycompany.myapp.domain;

import java.util.UUID;

public class ReportExecutionTestSamples {

    public static ReportExecution getReportExecutionSample1() {
        return new ReportExecution().rid("rid1").error("error1").url("url1").user("user1").additionalInfo("additionalInfo1");
    }

    public static ReportExecution getReportExecutionSample2() {
        return new ReportExecution().rid("rid2").error("error2").url("url2").user("user2").additionalInfo("additionalInfo2");
    }

    public static ReportExecution getReportExecutionRandomSampleGenerator() {
        return new ReportExecution()
            .rid(UUID.randomUUID().toString())
            .error(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .user(UUID.randomUUID().toString())
            .additionalInfo(UUID.randomUUID().toString());
    }
}
