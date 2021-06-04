package dev.clinic.simple.service.impl;

import dev.clinic.simple.domain.PagamentoExame;
import dev.clinic.simple.repository.PagamentoExameRepository;
import dev.clinic.simple.service.PagamentoExameService;
import dev.clinic.simple.service.dto.PagamentoExameDTO;
import dev.clinic.simple.service.mapper.PagamentoExameMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PagamentoExame}.
 */
@Service
@Transactional
public class PagamentoExameServiceImpl implements PagamentoExameService {

    private final Logger log = LoggerFactory.getLogger(PagamentoExameServiceImpl.class);

    private final PagamentoExameRepository pagamentoExameRepository;

    private final PagamentoExameMapper pagamentoExameMapper;

    public PagamentoExameServiceImpl(PagamentoExameRepository pagamentoExameRepository, PagamentoExameMapper pagamentoExameMapper) {
        this.pagamentoExameRepository = pagamentoExameRepository;
        this.pagamentoExameMapper = pagamentoExameMapper;
    }

    @Override
    public PagamentoExameDTO save(PagamentoExameDTO pagamentoExameDTO) {
        log.debug("Request to save PagamentoExame : {}", pagamentoExameDTO);
        PagamentoExame pagamentoExame = pagamentoExameMapper.toEntity(pagamentoExameDTO);
        pagamentoExame = pagamentoExameRepository.save(pagamentoExame);
        return pagamentoExameMapper.toDto(pagamentoExame);
    }

    @Override
    public Optional<PagamentoExameDTO> partialUpdate(PagamentoExameDTO pagamentoExameDTO) {
        log.debug("Request to partially update PagamentoExame : {}", pagamentoExameDTO);

        return pagamentoExameRepository
            .findById(pagamentoExameDTO.getId())
            .map(
                existingPagamentoExame -> {
                    pagamentoExameMapper.partialUpdate(existingPagamentoExame, pagamentoExameDTO);
                    return existingPagamentoExame;
                }
            )
            .map(pagamentoExameRepository::save)
            .map(pagamentoExameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagamentoExameDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PagamentoExames");
        return pagamentoExameRepository.findAll(pageable).map(pagamentoExameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PagamentoExameDTO> findOne(Long id) {
        log.debug("Request to get PagamentoExame : {}", id);
        return pagamentoExameRepository.findById(id).map(pagamentoExameMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PagamentoExame : {}", id);
        pagamentoExameRepository.deleteById(id);
    }
}
