package dev.clinic.simple.service.mapper;

import dev.clinic.simple.domain.*;
import dev.clinic.simple.service.dto.MedicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medico} and its DTO {@link MedicoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MedicoMapper extends EntityMapper<MedicoDTO, Medico> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicoDTO toDtoId(Medico medico);
}
