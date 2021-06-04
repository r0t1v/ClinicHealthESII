package dev.clinic.simple.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultadoExameDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultadoExameDTO.class);
        ResultadoExameDTO resultadoExameDTO1 = new ResultadoExameDTO();
        resultadoExameDTO1.setId(1L);
        ResultadoExameDTO resultadoExameDTO2 = new ResultadoExameDTO();
        assertThat(resultadoExameDTO1).isNotEqualTo(resultadoExameDTO2);
        resultadoExameDTO2.setId(resultadoExameDTO1.getId());
        assertThat(resultadoExameDTO1).isEqualTo(resultadoExameDTO2);
        resultadoExameDTO2.setId(2L);
        assertThat(resultadoExameDTO1).isNotEqualTo(resultadoExameDTO2);
        resultadoExameDTO1.setId(null);
        assertThat(resultadoExameDTO1).isNotEqualTo(resultadoExameDTO2);
    }
}
