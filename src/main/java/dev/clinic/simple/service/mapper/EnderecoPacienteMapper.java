package dev.clinic.simple.service.mapper;

import dev.clinic.simple.domain.*;
import dev.clinic.simple.service.dto.EnderecoPacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EnderecoPaciente} and its DTO {@link EnderecoPacienteDTO}.
 */
@Mapper(componentModel = "spring", uses = { PacienteMapper.class })
public interface EnderecoPacienteMapper extends EntityMapper<EnderecoPacienteDTO, EnderecoPaciente> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "id")
    EnderecoPacienteDTO toDto(EnderecoPaciente s);
}
