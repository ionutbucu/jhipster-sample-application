package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ReportMetadata} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportMetadataDTO implements Serializable {

    private String rid;

    @Lob
    private String metadata;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportMetadataDTO)) {
            return false;
        }

        ReportMetadataDTO reportMetadataDTO = (ReportMetadataDTO) o;
        if (this.rid == null) {
            return false;
        }
        return Objects.equals(this.rid, reportMetadataDTO.rid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rid);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportMetadataDTO{" +
            "rid='" + getRid() + "'" +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
