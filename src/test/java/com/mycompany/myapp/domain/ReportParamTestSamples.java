package com.mycompany.myapp.domain;

import java.util.UUID;

public class ReportParamTestSamples {

    public static ReportParam getReportParamSample1() {
        return new ReportParam().rid("rid1").name("name1").type("type1").value("value1").conversionRule("conversionRule1");
    }

    public static ReportParam getReportParamSample2() {
        return new ReportParam().rid("rid2").name("name2").type("type2").value("value2").conversionRule("conversionRule2");
    }

    public static ReportParam getReportParamRandomSampleGenerator() {
        return new ReportParam()
            .rid(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString())
            .conversionRule(UUID.randomUUID().toString());
    }
}
