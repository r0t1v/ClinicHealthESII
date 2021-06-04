package dev.clinic.simple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContatoPacienteMapperTest {

    private ContatoPacienteMapper contatoPacienteMapper;

    @BeforeEach
    public void setUp() {
        contatoPacienteMapper = new ContatoPacienteMapperImpl();
    }
}
