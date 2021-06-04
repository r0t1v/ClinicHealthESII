package dev.clinic.simple.service.mapper;

import dev.clinic.simple.domain.*;
import dev.clinic.simple.service.dto.PagamentoExameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PagamentoExame} and its DTO {@link PagamentoExameDTO}.
 */
@Mapper(componentModel = "spring", uses = { ExameMapper.class })
public interface PagamentoExameMapper extends EntityMapper<PagamentoExameDTO, PagamentoExame> {
    @Mapping(target = "exame", source = "exame", qualifiedByName = "id")
    PagamentoExameDTO toDto(PagamentoExame s);
}
