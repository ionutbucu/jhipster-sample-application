package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ReportExecution} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportExecutionDTO implements Serializable {

    private String rid;

    @NotNull(message = "must not be null")
    private Instant date;

    @Size(max = 256)
    private String error;

    private String url;

    private String user;

    private String additionalInfo;

    private ReportDTO report;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
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
        if (!(o instanceof ReportExecutionDTO)) {
            return false;
        }

        ReportExecutionDTO reportExecutionDTO = (ReportExecutionDTO) o;
        if (this.rid == null) {
            return false;
        }
        return Objects.equals(this.rid, reportExecutionDTO.rid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rid);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportExecutionDTO{" +
            "rid='" + getRid() + "'" +
            ", date='" + getDate() + "'" +
            ", error='" + getError() + "'" +
            ", url='" + getUrl() + "'" +
            ", user='" + getUser() + "'" +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
