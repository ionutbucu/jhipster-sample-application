package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDTO.class);
        ReportDTO reportDTO1 = new ReportDTO();
        reportDTO1.setRid("id1");
        ReportDTO reportDTO2 = new ReportDTO();
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
        reportDTO2.setRid(reportDTO1.getRid());
        assertThat(reportDTO1).isEqualTo(reportDTO2);
        reportDTO2.setRid("id2");
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
        reportDTO1.setRid(null);
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
    }
}
