package dev.clinic.simple.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaClinicaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaClinicaDTO.class);
        ContaClinicaDTO contaClinicaDTO1 = new ContaClinicaDTO();
        contaClinicaDTO1.setId(1L);
        ContaClinicaDTO contaClinicaDTO2 = new ContaClinicaDTO();
        assertThat(contaClinicaDTO1).isNotEqualTo(contaClinicaDTO2);
        contaClinicaDTO2.setId(contaClinicaDTO1.getId());
        assertThat(contaClinicaDTO1).isEqualTo(contaClinicaDTO2);
        contaClinicaDTO2.setId(2L);
        assertThat(contaClinicaDTO1).isNotEqualTo(contaClinicaDTO2);
        contaClinicaDTO1.setId(null);
        assertThat(contaClinicaDTO1).isNotEqualTo(contaClinicaDTO2);
    }
}
