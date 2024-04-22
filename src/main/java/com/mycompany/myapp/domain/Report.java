package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.QueryType;
import com.mycompany.myapp.domain.enumeration.ReportType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Report.
 */
@Table("report")
@JsonIgnoreProperties(value = { "new", "id" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Report implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("rid")
    private String rid;

    @Column("cid")
    private String cid;

    @NotNull(message = "must not be null")
    @Size(min = 3)
    @Column("name")
    private String name;

    @Size(max = 512)
    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Size(min = 10, max = 10000)
    @Column("query")
    private String query;

    @Column("query_type")
    private QueryType queryType;

    @NotNull(message = "must not be null")
    @Size(min = 3)
    @Column("file_name")
    private String fileName;

    @Column("report_type")
    private ReportType reportType;

    @Column("license_holder")
    private String licenseHolder;

    @Column("owner")
    private String owner;

    @Transient
    private boolean isPersisted;

    @Transient
    private ReportDataSource datasource;

    @Transient
    private ReportMetadata metadata;

    @Transient
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportSchedule> schedules = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportDistribution> distributions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportExecution> executions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportParam> parameters = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportColumnMapping> columns = new HashSet<>();

    @Column("datasource_rid")
    private String datasourceId;

    @Column("metadata_rid")
    private String metadataId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getRid() {
        return this.rid;
    }

    public Report rid(String rid) {
        this.setRid(rid);
        return this;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCid() {
        return this.cid;
    }

    public Report cid(String cid) {
        this.setCid(cid);
        return this;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return this.name;
    }

    public Report name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Report description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuery() {
        return this.query;
    }

    public Report query(String query) {
        this.setQuery(query);
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryType getQueryType() {
        return this.queryType;
    }

    public Report queryType(QueryType queryType) {
        this.setQueryType(queryType);
        return this;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Report fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ReportType getReportType() {
        return this.reportType;
    }

    public Report reportType(ReportType reportType) {
        this.setReportType(reportType);
        return this;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getLicenseHolder() {
        return this.licenseHolder;
    }

    public Report licenseHolder(String licenseHolder) {
        this.setLicenseHolder(licenseHolder);
        return this;
    }

    public void setLicenseHolder(String licenseHolder) {
        this.licenseHolder = licenseHolder;
    }

    public String getOwner() {
        return this.owner;
    }

    public Report owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public Report setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public ReportDataSource getDatasource() {
        return this.datasource;
    }

    public void setDatasource(ReportDataSource reportDataSource) {
        this.datasource = reportDataSource;
        this.datasourceId = reportDataSource != null ? reportDataSource.getRid() : null;
    }

    public Report datasource(ReportDataSource reportDataSource) {
        this.setDatasource(reportDataSource);
        return this;
    }

    public ReportMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ReportMetadata reportMetadata) {
        this.metadata = reportMetadata;
        this.metadataId = reportMetadata != null ? reportMetadata.getRid() : null;
    }

    public Report metadata(ReportMetadata reportMetadata) {
        this.setMetadata(reportMetadata);
        return this;
    }

    public Set<ReportSchedule> getSchedules() {
        return this.schedules;
    }

    public void setSchedules(Set<ReportSchedule> reportSchedules) {
        if (this.schedules != null) {
            this.schedules.forEach(i -> i.setReport(null));
        }
        if (reportSchedules != null) {
            reportSchedules.forEach(i -> i.setReport(this));
        }
        this.schedules = reportSchedules;
    }

    public Report schedules(Set<ReportSchedule> reportSchedules) {
        this.setSchedules(reportSchedules);
        return this;
    }

    public Report addSchedules(ReportSchedule reportSchedule) {
        this.schedules.add(reportSchedule);
        reportSchedule.setReport(this);
        return this;
    }

    public Report removeSchedules(ReportSchedule reportSchedule) {
        this.schedules.remove(reportSchedule);
        reportSchedule.setReport(null);
        return this;
    }

    public Set<ReportDistribution> getDistributions() {
        return this.distributions;
    }

    public void setDistributions(Set<ReportDistribution> reportDistributions) {
        if (this.distributions != null) {
            this.distributions.forEach(i -> i.setReport(null));
        }
        if (reportDistributions != null) {
            reportDistributions.forEach(i -> i.setReport(this));
        }
        this.distributions = reportDistributions;
    }

    public Report distributions(Set<ReportDistribution> reportDistributions) {
        this.setDistributions(reportDistributions);
        return this;
    }

    public Report addDistributions(ReportDistribution reportDistribution) {
        this.distributions.add(reportDistribution);
        reportDistribution.setReport(this);
        return this;
    }

    public Report removeDistributions(ReportDistribution reportDistribution) {
        this.distributions.remove(reportDistribution);
        reportDistribution.setReport(null);
        return this;
    }

    public Set<ReportExecution> getExecutions() {
        return this.executions;
    }

    public void setExecutions(Set<ReportExecution> reportExecutions) {
        if (this.executions != null) {
            this.executions.forEach(i -> i.setReport(null));
        }
        if (reportExecutions != null) {
            reportExecutions.forEach(i -> i.setReport(this));
        }
        this.executions = reportExecutions;
    }

    public Report executions(Set<ReportExecution> reportExecutions) {
        this.setExecutions(reportExecutions);
        return this;
    }

    public Report addExecutions(ReportExecution reportExecution) {
        this.executions.add(reportExecution);
        reportExecution.setReport(this);
        return this;
    }

    public Report removeExecutions(ReportExecution reportExecution) {
        this.executions.remove(reportExecution);
        reportExecution.setReport(null);
        return this;
    }

    public Set<ReportParam> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<ReportParam> reportParams) {
        if (this.parameters != null) {
            this.parameters.forEach(i -> i.setReport(null));
        }
        if (reportParams != null) {
            reportParams.forEach(i -> i.setReport(this));
        }
        this.parameters = reportParams;
    }

    public Report parameters(Set<ReportParam> reportParams) {
        this.setParameters(reportParams);
        return this;
    }

    public Report addParameters(ReportParam reportParam) {
        this.parameters.add(reportParam);
        reportParam.setReport(this);
        return this;
    }

    public Report removeParameters(ReportParam reportParam) {
        this.parameters.remove(reportParam);
        reportParam.setReport(null);
        return this;
    }

    public Set<ReportColumnMapping> getColumns() {
        return this.columns;
    }

    public void setColumns(Set<ReportColumnMapping> reportColumnMappings) {
        if (this.columns != null) {
            this.columns.forEach(i -> i.setReport(null));
        }
        if (reportColumnMappings != null) {
            reportColumnMappings.forEach(i -> i.setReport(this));
        }
        this.columns = reportColumnMappings;
    }

    public Report columns(Set<ReportColumnMapping> reportColumnMappings) {
        this.setColumns(reportColumnMappings);
        return this;
    }

    public Report addColumns(ReportColumnMapping reportColumnMapping) {
        this.columns.add(reportColumnMapping);
        reportColumnMapping.setReport(this);
        return this;
    }

    public Report removeColumns(ReportColumnMapping reportColumnMapping) {
        this.columns.remove(reportColumnMapping);
        reportColumnMapping.setReport(null);
        return this;
    }

    public String getDatasourceId() {
        return this.datasourceId;
    }

    public void setDatasourceId(String reportDataSource) {
        this.datasourceId = reportDataSource;
    }

    public String getMetadataId() {
        return this.metadataId;
    }

    public void setMetadataId(String reportMetadata) {
        this.metadataId = reportMetadata;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return getRid() != null && getRid().equals(((Report) o).getRid());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "rid=" + getRid() +
            ", cid='" + getCid() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", query='" + getQuery() + "'" +
            ", queryType='" + getQueryType() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", reportType='" + getReportType() + "'" +
            ", licenseHolder='" + getLicenseHolder() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
