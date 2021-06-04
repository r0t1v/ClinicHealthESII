package dev.clinic.simple.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContatoPacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContatoPaciente.class);
        ContatoPaciente contatoPaciente1 = new ContatoPaciente();
        contatoPaciente1.setId(1L);
        ContatoPaciente contatoPaciente2 = new ContatoPaciente();
        contatoPaciente2.setId(contatoPaciente1.getId());
        assertThat(contatoPaciente1).isEqualTo(contatoPaciente2);
        contatoPaciente2.setId(2L);
        assertThat(contatoPaciente1).isNotEqualTo(contatoPaciente2);
        contatoPaciente1.setId(null);
        assertThat(contatoPaciente1).isNotEqualTo(contatoPaciente2);
    }
}
