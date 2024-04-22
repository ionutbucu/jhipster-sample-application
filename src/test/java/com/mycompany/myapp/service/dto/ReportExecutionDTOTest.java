package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportExecutionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportExecutionDTO.class);
        ReportExecutionDTO reportExecutionDTO1 = new ReportExecutionDTO();
        reportExecutionDTO1.setRid("id1");
        ReportExecutionDTO reportExecutionDTO2 = new ReportExecutionDTO();
        assertThat(reportExecutionDTO1).isNotEqualTo(reportExecutionDTO2);
        reportExecutionDTO2.setRid(reportExecutionDTO1.getRid());
        assertThat(reportExecutionDTO1).isEqualTo(reportExecutionDTO2);
        reportExecutionDTO2.setRid("id2");
        assertThat(reportExecutionDTO1).isNotEqualTo(reportExecutionDTO2);
        reportExecutionDTO1.setRid(null);
        assertThat(reportExecutionDTO1).isNotEqualTo(reportExecutionDTO2);
    }
}
