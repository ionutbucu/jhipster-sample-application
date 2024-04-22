package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ReportColumnMapping} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportColumnMappingDTO implements Serializable {

    private String rid;

    private String sourceColumnName;

    private Integer sourceColumnIndex;

    private String columnTitle;

    private String lang;

    private ReportDTO report;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getSourceColumnName() {
        return sourceColumnName;
    }

    public void setSourceColumnName(String sourceColumnName) {
        this.sourceColumnName = sourceColumnName;
    }

    public Integer getSourceColumnIndex() {
        return sourceColumnIndex;
    }

    public void setSourceColumnIndex(Integer sourceColumnIndex) {
        this.sourceColumnIndex = sourceColumnIndex;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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
        if (!(o instanceof ReportColumnMappingDTO)) {
            return false;
        }

        ReportColumnMappingDTO reportColumnMappingDTO = (ReportColumnMappingDTO) o;
        if (this.rid == null) {
            return false;
        }
        return Objects.equals(this.rid, reportColumnMappingDTO.rid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rid);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportColumnMappingDTO{" +
            "rid='" + getRid() + "'" +
            ", sourceColumnName='" + getSourceColumnName() + "'" +
            ", sourceColumnIndex=" + getSourceColumnIndex() +
            ", columnTitle='" + getColumnTitle() + "'" +
            ", lang='" + getLang() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
