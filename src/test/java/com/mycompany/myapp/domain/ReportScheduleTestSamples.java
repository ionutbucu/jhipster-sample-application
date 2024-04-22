package com.mycompany.myapp.domain;

import java.util.UUID;

public class ReportScheduleTestSamples {

    public static ReportSchedule getReportScheduleSample1() {
        return new ReportSchedule().rid("rid1").cron("cron1");
    }

    public static ReportSchedule getReportScheduleSample2() {
        return new ReportSchedule().rid("rid2").cron("cron2");
    }

    public static ReportSchedule getReportScheduleRandomSampleGenerator() {
        return new ReportSchedule().rid(UUID.randomUUID().toString()).cron(UUID.randomUUID().toString());
    }
}
