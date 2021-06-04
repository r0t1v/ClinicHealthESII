package dev.clinic.simple.service.impl;

import dev.clinic.simple.domain.ResultadoExame;
import dev.clinic.simple.repository.ResultadoExameRepository;
import dev.clinic.simple.service.ResultadoExameService;
import dev.clinic.simple.service.dto.ResultadoExameDTO;
import dev.clinic.simple.service.mapper.ResultadoExameMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ResultadoExame}.
 */
@Service
@Transactional
public class ResultadoExameServiceImpl implements ResultadoExameService {

    private final Logger log = LoggerFactory.getLogger(ResultadoExameServiceImpl.class);

    private final ResultadoExameRepository resultadoExameRepository;

    private final ResultadoExameMapper resultadoExameMapper;

    public ResultadoExameServiceImpl(ResultadoExameRepository resultadoExameRepository, ResultadoExameMapper resultadoExameMapper) {
        this.resultadoExameRepository = resultadoExameRepository;
        this.resultadoExameMapper = resultadoExameMapper;
    }

    @Override
    public ResultadoExameDTO save(ResultadoExameDTO resultadoExameDTO) {
        log.debug("Request to save ResultadoExame : {}", resultadoExameDTO);
        ResultadoExame resultadoExame = resultadoExameMapper.toEntity(resultadoExameDTO);
        resultadoExame = resultadoExameRepository.save(resultadoExame);
        return resultadoExameMapper.toDto(resultadoExame);
    }

    @Override
    public Optional<ResultadoExameDTO> partialUpdate(ResultadoExameDTO resultadoExameDTO) {
        log.debug("Request to partially update ResultadoExame : {}", resultadoExameDTO);

        return resultadoExameRepository
            .findById(resultadoExameDTO.getId())
            .map(
                existingResultadoExame -> {
                    resultadoExameMapper.partialUpdate(existingResultadoExame, resultadoExameDTO);
                    return existingResultadoExame;
                }
            )
            .map(resultadoExameRepository::save)
            .map(resultadoExameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResultadoExameDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResultadoExames");
        return resultadoExameRepository.findAll(pageable).map(resultadoExameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResultadoExameDTO> findOne(Long id) {
        log.debug("Request to get ResultadoExame : {}", id);
        return resultadoExameRepository.findById(id).map(resultadoExameMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ResultadoExame : {}", id);
        resultadoExameRepository.deleteById(id);
    }
}
