package dev.clinic.simple.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultadoExameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultadoExame.class);
        ResultadoExame resultadoExame1 = new ResultadoExame();
        resultadoExame1.setId(1L);
        ResultadoExame resultadoExame2 = new ResultadoExame();
        resultadoExame2.setId(resultadoExame1.getId());
        assertThat(resultadoExame1).isEqualTo(resultadoExame2);
        resultadoExame2.setId(2L);
        assertThat(resultadoExame1).isNotEqualTo(resultadoExame2);
        resultadoExame1.setId(null);
        assertThat(resultadoExame1).isNotEqualTo(resultadoExame2);
    }
}
