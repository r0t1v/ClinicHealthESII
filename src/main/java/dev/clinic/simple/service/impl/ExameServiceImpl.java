package dev.clinic.simple.service.impl;

import dev.clinic.simple.domain.Exame;
import dev.clinic.simple.repository.ExameRepository;
import dev.clinic.simple.service.ExameService;
import dev.clinic.simple.service.dto.ExameDTO;
import dev.clinic.simple.service.mapper.ExameMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Exame}.
 */
@Service
@Transactional
public class ExameServiceImpl implements ExameService {

    private final Logger log = LoggerFactory.getLogger(ExameServiceImpl.class);

    private final ExameRepository exameRepository;

    private final ExameMapper exameMapper;

    public ExameServiceImpl(ExameRepository exameRepository, ExameMapper exameMapper) {
        this.exameRepository = exameRepository;
        this.exameMapper = exameMapper;
    }

    @Override
    public ExameDTO save(ExameDTO exameDTO) {
        log.debug("Request to save Exame : {}", exameDTO);
        Exame exame = exameMapper.toEntity(exameDTO);
        exame = exameRepository.save(exame);
        return exameMapper.toDto(exame);
    }

    @Override
    public Optional<ExameDTO> partialUpdate(ExameDTO exameDTO) {
        log.debug("Request to partially update Exame : {}", exameDTO);

        return exameRepository
            .findById(exameDTO.getId())
            .map(
                existingExame -> {
                    exameMapper.partialUpdate(existingExame, exameDTO);
                    return existingExame;
                }
            )
            .map(exameRepository::save)
            .map(exameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExameDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Exames");
        return exameRepository.findAll(pageable).map(exameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExameDTO> findOne(Long id) {
        log.debug("Request to get Exame : {}", id);
        return exameRepository.findById(id).map(exameMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exame : {}", id);
        exameRepository.deleteById(id);
    }
}
