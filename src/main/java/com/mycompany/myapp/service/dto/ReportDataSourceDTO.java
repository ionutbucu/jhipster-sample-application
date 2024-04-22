package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ReportDataSource} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDataSourceDTO implements Serializable {

    private String rid;

    private String type;

    private String url;

    private String user;

    private String password;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDataSourceDTO)) {
            return false;
        }

        ReportDataSourceDTO reportDataSourceDTO = (ReportDataSourceDTO) o;
        if (this.rid == null) {
            return false;
        }
        return Objects.equals(this.rid, reportDataSourceDTO.rid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rid);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDataSourceDTO{" +
            "rid='" + getRid() + "'" +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", user='" + getUser() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
