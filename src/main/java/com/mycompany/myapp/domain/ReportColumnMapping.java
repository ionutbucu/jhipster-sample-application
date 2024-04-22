package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ReportColumnMapping.
 */
@Table("report_column_mapping")
@JsonIgnoreProperties(value = { "new", "id" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportColumnMapping implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("rid")
    private String rid;

    @Column("source_column_name")
    private String sourceColumnName;

    @Column("source_column_index")
    private Integer sourceColumnIndex;

    @Column("column_title")
    private String columnTitle;

    @Column("lang")
    private String lang;

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

    public ReportColumnMapping rid(String rid) {
        this.setRid(rid);
        return this;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getSourceColumnName() {
        return this.sourceColumnName;
    }

    public ReportColumnMapping sourceColumnName(String sourceColumnName) {
        this.setSourceColumnName(sourceColumnName);
        return this;
    }

    public void setSourceColumnName(String sourceColumnName) {
        this.sourceColumnName = sourceColumnName;
    }

    public Integer getSourceColumnIndex() {
        return this.sourceColumnIndex;
    }

    public ReportColumnMapping sourceColumnIndex(Integer sourceColumnIndex) {
        this.setSourceColumnIndex(sourceColumnIndex);
        return this;
    }

    public void setSourceColumnIndex(Integer sourceColumnIndex) {
        this.sourceColumnIndex = sourceColumnIndex;
    }

    public String getColumnTitle() {
        return this.columnTitle;
    }

    public ReportColumnMapping columnTitle(String columnTitle) {
        this.setColumnTitle(columnTitle);
        return this;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public String getLang() {
        return this.lang;
    }

    public ReportColumnMapping lang(String lang) {
        this.setLang(lang);
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
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

    public ReportColumnMapping setIsPersisted() {
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

    public ReportColumnMapping report(Report report) {
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
        if (!(o instanceof ReportColumnMapping)) {
            return false;
        }
        return getRid() != null && getRid().equals(((ReportColumnMapping) o).getRid());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportColumnMapping{" +
            "rid=" + getRid() +
            ", sourceColumnName='" + getSourceColumnName() + "'" +
            ", sourceColumnIndex=" + getSourceColumnIndex() +
            ", columnTitle='" + getColumnTitle() + "'" +
            ", lang='" + getLang() + "'" +
            "}";
    }
}
