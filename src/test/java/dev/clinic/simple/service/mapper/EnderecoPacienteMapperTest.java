package dev.clinic.simple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnderecoPacienteMapperTest {

    private EnderecoPacienteMapper enderecoPacienteMapper;

    @BeforeEach
    public void setUp() {
        enderecoPacienteMapper = new EnderecoPacienteMapperImpl();
    }
}
