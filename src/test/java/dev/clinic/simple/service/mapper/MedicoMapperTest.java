package dev.clinic.simple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicoMapperTest {

    private MedicoMapper medicoMapper;

    @BeforeEach
    public void setUp() {
        medicoMapper = new MedicoMapperImpl();
    }
}
