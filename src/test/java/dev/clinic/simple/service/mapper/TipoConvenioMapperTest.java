package dev.clinic.simple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoConvenioMapperTest {

    private TipoConvenioMapper tipoConvenioMapper;

    @BeforeEach
    public void setUp() {
        tipoConvenioMapper = new TipoConvenioMapperImpl();
    }
}
