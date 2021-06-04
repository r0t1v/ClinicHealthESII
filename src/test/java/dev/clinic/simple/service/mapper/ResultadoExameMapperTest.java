package dev.clinic.simple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultadoExameMapperTest {

    private ResultadoExameMapper resultadoExameMapper;

    @BeforeEach
    public void setUp() {
        resultadoExameMapper = new ResultadoExameMapperImpl();
    }
}
