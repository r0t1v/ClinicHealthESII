package dev.clinic.simple.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContatoPacienteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContatoPacienteDTO.class);
        ContatoPacienteDTO contatoPacienteDTO1 = new ContatoPacienteDTO();
        contatoPacienteDTO1.setId(1L);
        ContatoPacienteDTO contatoPacienteDTO2 = new ContatoPacienteDTO();
        assertThat(contatoPacienteDTO1).isNotEqualTo(contatoPacienteDTO2);
        contatoPacienteDTO2.setId(contatoPacienteDTO1.getId());
        assertThat(contatoPacienteDTO1).isEqualTo(contatoPacienteDTO2);
        contatoPacienteDTO2.setId(2L);
        assertThat(contatoPacienteDTO1).isNotEqualTo(contatoPacienteDTO2);
        contatoPacienteDTO1.setId(null);
        assertThat(contatoPacienteDTO1).isNotEqualTo(contatoPacienteDTO2);
    }
}
