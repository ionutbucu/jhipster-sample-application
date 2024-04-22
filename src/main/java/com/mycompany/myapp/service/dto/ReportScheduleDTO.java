package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ReportSchedule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportScheduleDTO implements Serializable {

    private String rid;

    @NotNull(message = "must not be null")
    @Size(max = 20)
    private String cron;

    private ReportDTO report;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
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
        if (!(o instanceof ReportScheduleDTO)) {
            return false;
        }

        ReportScheduleDTO reportScheduleDTO = (ReportScheduleDTO) o;
        if (this.rid == null) {
            return false;
        }
        return Objects.equals(this.rid, reportScheduleDTO.rid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rid);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportScheduleDTO{" +
            "rid='" + getRid() + "'" +
            ", cron='" + getCron() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
