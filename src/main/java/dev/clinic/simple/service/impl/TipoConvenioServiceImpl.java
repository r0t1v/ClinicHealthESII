package dev.clinic.simple.service.impl;

import dev.clinic.simple.domain.TipoConvenio;
import dev.clinic.simple.repository.TipoConvenioRepository;
import dev.clinic.simple.service.TipoConvenioService;
import dev.clinic.simple.service.dto.TipoConvenioDTO;
import dev.clinic.simple.service.mapper.TipoConvenioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoConvenio}.
 */
@Service
@Transactional
public class TipoConvenioServiceImpl implements TipoConvenioService {

    private final Logger log = LoggerFactory.getLogger(TipoConvenioServiceImpl.class);

    private final TipoConvenioRepository tipoConvenioRepository;

    private final TipoConvenioMapper tipoConvenioMapper;

    public TipoConvenioServiceImpl(TipoConvenioRepository tipoConvenioRepository, TipoConvenioMapper tipoConvenioMapper) {
        this.tipoConvenioRepository = tipoConvenioRepository;
        this.tipoConvenioMapper = tipoConvenioMapper;
    }

    @Override
    public TipoConvenioDTO save(TipoConvenioDTO tipoConvenioDTO) {
        log.debug("Request to save TipoConvenio : {}", tipoConvenioDTO);
        TipoConvenio tipoConvenio = tipoConvenioMapper.toEntity(tipoConvenioDTO);
        tipoConvenio = tipoConvenioRepository.save(tipoConvenio);
        return tipoConvenioMapper.toDto(tipoConvenio);
    }

    @Override
    public Optional<TipoConvenioDTO> partialUpdate(TipoConvenioDTO tipoConvenioDTO) {
        log.debug("Request to partially update TipoConvenio : {}", tipoConvenioDTO);

        return tipoConvenioRepository
            .findById(tipoConvenioDTO.getId())
            .map(
                existingTipoConvenio -> {
                    tipoConvenioMapper.partialUpdate(existingTipoConvenio, tipoConvenioDTO);
                    return existingTipoConvenio;
                }
            )
            .map(tipoConvenioRepository::save)
            .map(tipoConvenioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoConvenioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoConvenios");
        return tipoConvenioRepository.findAll(pageable).map(tipoConvenioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoConvenioDTO> findOne(Long id) {
        log.debug("Request to get TipoConvenio : {}", id);
        return tipoConvenioRepository.findById(id).map(tipoConvenioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoConvenio : {}", id);
        tipoConvenioRepository.deleteById(id);
    }
}
