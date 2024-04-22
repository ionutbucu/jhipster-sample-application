package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ReportSchedule.
 */
@Table("report_schedule")
@JsonIgnoreProperties(value = { "new", "id" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportSchedule implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("rid")
    private String rid;

    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Column("cron")
    private String cron;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(
        value = { "datasource", "metadata", "schedules", "distributions", "executions", "parameters", "columns" },
        allowSetters = true
    )
    private Report report;

    @Column("report_rid")
    private String reportId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getRid() {
        return this.rid;
    }

    public ReportSchedule rid(String rid) {
        this.setRid(rid);
        return this;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCron() {
        return this.cron;
    }

    public ReportSchedule cron(String cron) {
        this.setCron(cron);
        return this;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public String getId() {
        return this.rid;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ReportSchedule setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
        this.reportId = report != null ? report.getRid() : null;
    }

    public ReportSchedule report(Report report) {
        this.setReport(report);
        return this;
    }

    public String getReportId() {
        return this.reportId;
    }

    public void setReportId(String report) {
        this.reportId = report;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportSchedule)) {
            return false;
        }
        return getRid() != null && getRid().equals(((ReportSchedule) o).getRid());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportSchedule{" +
            "rid=" + getRid() +
            ", cron='" + getCron() + "'" +
            "}";
    }
}
