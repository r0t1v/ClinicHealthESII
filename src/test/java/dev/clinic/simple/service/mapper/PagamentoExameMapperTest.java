package dev.clinic.simple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PagamentoExameMapperTest {

    private PagamentoExameMapper pagamentoExameMapper;

    @BeforeEach
    public void setUp() {
        pagamentoExameMapper = new PagamentoExameMapperImpl();
    }
}
