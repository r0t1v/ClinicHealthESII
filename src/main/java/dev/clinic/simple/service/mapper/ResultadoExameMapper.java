package dev.clinic.simple.service.mapper;

import dev.clinic.simple.domain.*;
import dev.clinic.simple.service.dto.ResultadoExameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResultadoExame} and its DTO {@link ResultadoExameDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ResultadoExameMapper extends EntityMapper<ResultadoExameDTO, ResultadoExame> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResultadoExameDTO toDtoId(ResultadoExame resultadoExame);
}
