package dev.clinic.simple.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExameDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExameDTO.class);
        ExameDTO exameDTO1 = new ExameDTO();
        exameDTO1.setId(1L);
        ExameDTO exameDTO2 = new ExameDTO();
        assertThat(exameDTO1).isNotEqualTo(exameDTO2);
        exameDTO2.setId(exameDTO1.getId());
        assertThat(exameDTO1).isEqualTo(exameDTO2);
        exameDTO2.setId(2L);
        assertThat(exameDTO1).isNotEqualTo(exameDTO2);
        exameDTO1.setId(null);
        assertThat(exameDTO1).isNotEqualTo(exameDTO2);
    }
}
