package dev.clinic.simple.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.clinic.simple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoConvenioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoConvenioDTO.class);
        TipoConvenioDTO tipoConvenioDTO1 = new TipoConvenioDTO();
        tipoConvenioDTO1.setId(1L);
        TipoConvenioDTO tipoConvenioDTO2 = new TipoConvenioDTO();
        assertThat(tipoConvenioDTO1).isNotEqualTo(tipoConvenioDTO2);
        tipoConvenioDTO2.setId(tipoConvenioDTO1.getId());
        assertThat(tipoConvenioDTO1).isEqualTo(tipoConvenioDTO2);
        tipoConvenioDTO2.setId(2L);
        assertThat(tipoConvenioDTO1).isNotEqualTo(tipoConvenioDTO2);
        tipoConvenioDTO1.setId(null);
        assertThat(tipoConvenioDTO1).isNotEqualTo(tipoConvenioDTO2);
    }
}
