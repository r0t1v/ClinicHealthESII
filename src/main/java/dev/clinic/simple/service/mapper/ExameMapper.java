package dev.clinic.simple.service.mapper;

import dev.clinic.simple.domain.*;
import dev.clinic.simple.service.dto.ExameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exame} and its DTO {@link ExameDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicoMapper.class, ResultadoExameMapper.class, ContaClinicaMapper.class })
public interface ExameMapper extends EntityMapper<ExameDTO, Exame> {
    @Mapping(target = "nomemedico", source = "nomemedico", qualifiedByName = "id")
    @Mapping(target = "tipoexame", source = "tipoexame", qualifiedByName = "id")
    @Mapping(target = "contaClinica", source = "contaClinica", qualifiedByName = "id")
    ExameDTO toDto(Exame s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExameDTO toDtoId(Exame exame);
}
