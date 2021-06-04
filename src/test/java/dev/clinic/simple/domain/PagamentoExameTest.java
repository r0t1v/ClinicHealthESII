package dev.clinic.simple.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PagamentoExameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PagamentoExame.class);
        PagamentoExame pagamentoExame1 = new PagamentoExame();
        pagamentoExame1.setId(1L);
        PagamentoExame pagamentoExame2 = new PagamentoExame();
        pagamentoExame2.setId(pagamentoExame1.getId());
        assertThat(pagamentoExame1).isEqualTo(pagamentoExame2);
        pagamentoExame2.setId(2L);
        assertThat(pagamentoExame1).isNotEqualTo(pagamentoExame2);
        pagamentoExame1.setId(null);
        assertThat(pagamentoExame1).isNotEqualTo(pagamentoExame2);
    }
}
