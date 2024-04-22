package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ReportParam} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportParamDTO implements Serializable {

    private String rid;

    private String name;

    private String type;

    private String value;

    private String conversionRule;

    private ReportDTO report;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getConversionRule() {
        return conversionRule;
    }

    public void setConversionRule(String conversionRule) {
        this.conversionRule = conversionRule;
    }

    public ReportDTO getReport() {
        return report;
    }

    public void setReport(ReportDTO report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportParamDTO)) {
            return false;
        }

        ReportParamDTO reportParamDTO = (ReportParamDTO) o;
        if (this.rid == null) {
            return false;
        }
        return Objects.equals(this.rid, reportParamDTO.rid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rid);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportParamDTO{" +
            "rid='" + getRid() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", value='" + getValue() + "'" +
            ", conversionRule='" + getConversionRule() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
