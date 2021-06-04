package dev.clinic.simple.service.mapper;

import dev.clinic.simple.domain.*;
import dev.clinic.simple.service.dto.ContatoPacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContatoPaciente} and its DTO {@link ContatoPacienteDTO}.
 */
@Mapper(componentModel = "spring", uses = { PacienteMapper.class })
public interface ContatoPacienteMapper extends EntityMapper<ContatoPacienteDTO, ContatoPaciente> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "id")
    ContatoPacienteDTO toDto(ContatoPaciente s);
}
