package dev.clinic.simple.service.mapper;

import dev.clinic.simple.domain.*;
import dev.clinic.simple.service.dto.ContaClinicaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContaClinica} and its DTO {@link ContaClinicaDTO}.
 */
@Mapper(componentModel = "spring", uses = { PacienteMapper.class })
public interface ContaClinicaMapper extends EntityMapper<ContaClinicaDTO, ContaClinica> {
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "id")
    ContaClinicaDTO toDto(ContaClinica s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContaClinicaDTO toDtoId(ContaClinica contaClinica);
}
