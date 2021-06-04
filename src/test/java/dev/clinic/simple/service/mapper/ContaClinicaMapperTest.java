package dev.clinic.simple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaClinicaMapperTest {

    private ContaClinicaMapper contaClinicaMapper;

    @BeforeEach
    public void setUp() {
        contaClinicaMapper = new ContaClinicaMapperImpl();
    }
}
