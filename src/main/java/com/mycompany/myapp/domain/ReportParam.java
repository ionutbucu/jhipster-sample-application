package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ReportParam.
 */
@Table("report_param")
@JsonIgnoreProperties(value = { "new", "id" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportParam implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("rid")
    private String rid;

    @Column("name")
    private String name;

    @Column("type")
    private String type;

    @Column("value")
    private String value;

    @Column("conversion_rule")
    private String conversionRule;

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

    public ReportParam rid(String rid) {
        this.setRid(rid);
        return this;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return this.name;
    }

    public ReportParam name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public ReportParam type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public ReportParam value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getConversionRule() {
        return this.conversionRule;
    }

    public ReportParam conversionRule(String conversionRule) {
        this.setConversionRule(conversionRule);
        return this;
    }

    public void setConversionRule(String conversionRule) {
        this.conversionRule = conversionRule;
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

    public ReportParam setIsPersisted() {
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

    public ReportParam report(Report report) {
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
        if (!(o instanceof ReportParam)) {
            return false;
        }
        return getRid() != null && getRid().equals(((ReportParam) o).getRid());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportParam{" +
            "rid=" + getRid() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", value='" + getValue() + "'" +
            ", conversionRule='" + getConversionRule() + "'" +
            "}";
    }
}
