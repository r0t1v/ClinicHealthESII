package dev.clinic.simple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PacienteMapperTest {

    private PacienteMapper pacienteMapper;

    @BeforeEach
    public void setUp() {
        pacienteMapper = new PacienteMapperImpl();
    }
}
