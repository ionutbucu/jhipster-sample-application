package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDistributionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDistributionDTO.class);
        ReportDistributionDTO reportDistributionDTO1 = new ReportDistributionDTO();
        reportDistributionDTO1.setRid("id1");
        ReportDistributionDTO reportDistributionDTO2 = new ReportDistributionDTO();
        assertThat(reportDistributionDTO1).isNotEqualTo(reportDistributionDTO2);
        reportDistributionDTO2.setRid(reportDistributionDTO1.getRid());
        assertThat(reportDistributionDTO1).isEqualTo(reportDistributionDTO2);
        reportDistributionDTO2.setRid("id2");
        assertThat(reportDistributionDTO1).isNotEqualTo(reportDistributionDTO2);
        reportDistributionDTO1.setRid(null);
        assertThat(reportDistributionDTO1).isNotEqualTo(reportDistributionDTO2);
    }
}
