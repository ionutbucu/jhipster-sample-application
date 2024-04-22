package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportScheduleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportScheduleDTO.class);
        ReportScheduleDTO reportScheduleDTO1 = new ReportScheduleDTO();
        reportScheduleDTO1.setRid("id1");
        ReportScheduleDTO reportScheduleDTO2 = new ReportScheduleDTO();
        assertThat(reportScheduleDTO1).isNotEqualTo(reportScheduleDTO2);
        reportScheduleDTO2.setRid(reportScheduleDTO1.getRid());
        assertThat(reportScheduleDTO1).isEqualTo(reportScheduleDTO2);
        reportScheduleDTO2.setRid("id2");
        assertThat(reportScheduleDTO1).isNotEqualTo(reportScheduleDTO2);
        reportScheduleDTO1.setRid(null);
        assertThat(reportScheduleDTO1).isNotEqualTo(reportScheduleDTO2);
    }
}
