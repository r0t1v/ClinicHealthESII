package dev.clinic.simple.service.mapper;

import dev.clinic.simple.domain.*;
import dev.clinic.simple.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paciente} and its DTO {@link PacienteDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContaClinicaMapper.class })
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "id")
    PacienteDTO toDto(Paciente s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoId(Paciente paciente);
}
