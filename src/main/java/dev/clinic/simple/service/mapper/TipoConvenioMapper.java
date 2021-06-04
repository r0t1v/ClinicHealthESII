package dev.clinic.simple.service.mapper;

import dev.clinic.simple.domain.*;
import dev.clinic.simple.service.dto.TipoConvenioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoConvenio} and its DTO {@link TipoConvenioDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContaClinicaMapper.class })
public interface TipoConvenioMapper extends EntityMapper<TipoConvenioDTO, TipoConvenio> {
    @Mapping(target = "contaClinica", source = "contaClinica", qualifiedByName = "id")
    TipoConvenioDTO toDto(TipoConvenio s);
}
