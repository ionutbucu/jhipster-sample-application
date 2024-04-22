package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportColumnMappingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportColumnMappingDTO.class);
        ReportColumnMappingDTO reportColumnMappingDTO1 = new ReportColumnMappingDTO();
        reportColumnMappingDTO1.setRid("id1");
        ReportColumnMappingDTO reportColumnMappingDTO2 = new ReportColumnMappingDTO();
        assertThat(reportColumnMappingDTO1).isNotEqualTo(reportColumnMappingDTO2);
        reportColumnMappingDTO2.setRid(reportColumnMappingDTO1.getRid());
        assertThat(reportColumnMappingDTO1).isEqualTo(reportColumnMappingDTO2);
        reportColumnMappingDTO2.setRid("id2");
        assertThat(reportColumnMappingDTO1).isNotEqualTo(reportColumnMappingDTO2);
        reportColumnMappingDTO1.setRid(null);
        assertThat(reportColumnMappingDTO1).isNotEqualTo(reportColumnMappingDTO2);
    }
}
