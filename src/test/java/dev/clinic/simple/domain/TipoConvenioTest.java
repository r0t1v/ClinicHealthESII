package dev.clinic.simple.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoConvenioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoConvenio.class);
        TipoConvenio tipoConvenio1 = new TipoConvenio();
        tipoConvenio1.setId(1L);
        TipoConvenio tipoConvenio2 = new TipoConvenio();
        tipoConvenio2.setId(tipoConvenio1.getId());
        assertThat(tipoConvenio1).isEqualTo(tipoConvenio2);
        tipoConvenio2.setId(2L);
        assertThat(tipoConvenio1).isNotEqualTo(tipoConvenio2);
        tipoConvenio1.setId(null);
        assertThat(tipoConvenio1).isNotEqualTo(tipoConvenio2);
    }
}
