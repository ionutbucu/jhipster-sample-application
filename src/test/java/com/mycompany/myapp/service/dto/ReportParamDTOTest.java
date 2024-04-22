package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportParamDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportParamDTO.class);
        ReportParamDTO reportParamDTO1 = new ReportParamDTO();
        reportParamDTO1.setRid("id1");
        ReportParamDTO reportParamDTO2 = new ReportParamDTO();
        assertThat(reportParamDTO1).isNotEqualTo(reportParamDTO2);
        reportParamDTO2.setRid(reportParamDTO1.getRid());
        assertThat(reportParamDTO1).isEqualTo(reportParamDTO2);
        reportParamDTO2.setRid("id2");
        assertThat(reportParamDTO1).isNotEqualTo(reportParamDTO2);
        reportParamDTO1.setRid(null);
        assertThat(reportParamDTO1).isNotEqualTo(reportParamDTO2);
    }
}
