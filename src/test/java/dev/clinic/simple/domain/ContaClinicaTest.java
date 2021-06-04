package dev.clinic.simple.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaClinicaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaClinica.class);
        ContaClinica contaClinica1 = new ContaClinica();
        contaClinica1.setId(1L);
        ContaClinica contaClinica2 = new ContaClinica();
        contaClinica2.setId(contaClinica1.getId());
        assertThat(contaClinica1).isEqualTo(contaClinica2);
        contaClinica2.setId(2L);
        assertThat(contaClinica1).isNotEqualTo(contaClinica2);
        contaClinica1.setId(null);
        assertThat(contaClinica1).isNotEqualTo(contaClinica2);
    }
}
