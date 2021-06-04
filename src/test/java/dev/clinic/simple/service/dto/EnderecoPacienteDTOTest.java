package dev.clinic.simple.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnderecoPacienteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnderecoPacienteDTO.class);
        EnderecoPacienteDTO enderecoPacienteDTO1 = new EnderecoPacienteDTO();
        enderecoPacienteDTO1.setId(1L);
        EnderecoPacienteDTO enderecoPacienteDTO2 = new EnderecoPacienteDTO();
        assertThat(enderecoPacienteDTO1).isNotEqualTo(enderecoPacienteDTO2);
        enderecoPacienteDTO2.setId(enderecoPacienteDTO1.getId());
        assertThat(enderecoPacienteDTO1).isEqualTo(enderecoPacienteDTO2);
        enderecoPacienteDTO2.setId(2L);
        assertThat(enderecoPacienteDTO1).isNotEqualTo(enderecoPacienteDTO2);
        enderecoPacienteDTO1.setId(null);
        assertThat(enderecoPacienteDTO1).isNotEqualTo(enderecoPacienteDTO2);
    }
}
