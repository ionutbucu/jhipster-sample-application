package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ReportExecution.
 */
@Table("report_execution")
@JsonIgnoreProperties(value = { "new", "id" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportExecution implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("rid")
    private String rid;

    @NotNull(message = "must not be null")
    @Column("date")
    private Instant date;

    @Size(max = 256)
    @Column("error")
    private String error;

    @Column("url")
    private String url;

    @Column("jhi_user")
    private String user;

    @Column("additional_info")
    private String additionalInfo;

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

    public ReportExecution rid(String rid) {
        this.setRid(rid);
        return this;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Instant getDate() {
        return this.date;
    }

    public ReportExecution date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getError() {
        return this.error;
    }

    public ReportExecution error(String error) {
        this.setError(error);
        return this;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUrl() {
        return this.url;
    }

    public ReportExecution url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return this.user;
    }

    public ReportExecution user(String user) {
        this.setUser(user);
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    public ReportExecution additionalInfo(String additionalInfo) {
        this.setAdditionalInfo(additionalInfo);
        return this;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
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

    public ReportExecution setIsPersisted() {
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

    public ReportExecution report(Report report) {
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
        if (!(o instanceof ReportExecution)) {
            return false;
        }
        return getRid() != null && getRid().equals(((ReportExecution) o).getRid());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportExecution{" +
            "rid=" + getRid() +
            ", date='" + getDate() + "'" +
            ", error='" + getError() + "'" +
            ", url='" + getUrl() + "'" +
            ", user='" + getUser() + "'" +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            "}";
    }
}
