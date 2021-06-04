package dev.clinic.simple.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnderecoPacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnderecoPaciente.class);
        EnderecoPaciente enderecoPaciente1 = new EnderecoPaciente();
        enderecoPaciente1.setId(1L);
        EnderecoPaciente enderecoPaciente2 = new EnderecoPaciente();
        enderecoPaciente2.setId(enderecoPaciente1.getId());
        assertThat(enderecoPaciente1).isEqualTo(enderecoPaciente2);
        enderecoPaciente2.setId(2L);
        assertThat(enderecoPaciente1).isNotEqualTo(enderecoPaciente2);
        enderecoPaciente1.setId(null);
        assertThat(enderecoPaciente1).isNotEqualTo(enderecoPaciente2);
    }
}
