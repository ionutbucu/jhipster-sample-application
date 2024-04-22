package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.QueryType;
import com.mycompany.myapp.domain.enumeration.ReportType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Report} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDTO implements Serializable {

    private String rid;

    private String cid;

    @NotNull(message = "must not be null")
    @Size(min = 3)
    private String name;

    @Size(max = 512)
    private String description;

    @NotNull(message = "must not be null")
    @Size(min = 10, max = 10000)
    private String query;

    private QueryType queryType;

    @NotNull(message = "must not be null")
    @Size(min = 3)
    private String fileName;

    private ReportType reportType;

    private String licenseHolder;

    private String owner;

    private ReportDataSourceDTO datasource;

    private ReportMetadataDTO metadata;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getLicenseHolder() {
        return licenseHolder;
    }

    public void setLicenseHolder(String licenseHolder) {
        this.licenseHolder = licenseHolder;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ReportDataSourceDTO getDatasource() {
        return datasource;
    }

    public void setDatasource(ReportDataSourceDTO datasource) {
        this.datasource = datasource;
    }

    public ReportMetadataDTO getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportMetadataDTO metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDTO)) {
            return false;
        }

        ReportDTO reportDTO = (ReportDTO) o;
        if (this.rid == null) {
            return false;
        }
        return Objects.equals(this.rid, reportDTO.rid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rid);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDTO{" +
            "rid='" + getRid() + "'" +
            ", cid='" + getCid() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", query='" + getQuery() + "'" +
            ", queryType='" + getQueryType() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", reportType='" + getReportType() + "'" +
            ", licenseHolder='" + getLicenseHolder() + "'" +
            ", owner='" + getOwner() + "'" +
            ", datasource=" + getDatasource() +
            ", metadata=" + getMetadata() +
            "}";
    }
}
