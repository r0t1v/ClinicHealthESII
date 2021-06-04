package dev.clinic.simple.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PagamentoExameDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PagamentoExameDTO.class);
        PagamentoExameDTO pagamentoExameDTO1 = new PagamentoExameDTO();
        pagamentoExameDTO1.setId(1L);
        PagamentoExameDTO pagamentoExameDTO2 = new PagamentoExameDTO();
        assertThat(pagamentoExameDTO1).isNotEqualTo(pagamentoExameDTO2);
        pagamentoExameDTO2.setId(pagamentoExameDTO1.getId());
        assertThat(pagamentoExameDTO1).isEqualTo(pagamentoExameDTO2);
        pagamentoExameDTO2.setId(2L);
        assertThat(pagamentoExameDTO1).isNotEqualTo(pagamentoExameDTO2);
        pagamentoExameDTO1.setId(null);
        assertThat(pagamentoExameDTO1).isNotEqualTo(pagamentoExameDTO2);
    }
}
