package dev.clinic.simple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExameMapperTest {

    private ExameMapper exameMapper;

    @BeforeEach
    public void setUp() {
        exameMapper = new ExameMapperImpl();
    }
}
