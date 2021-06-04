package dev.clinic.simple.service.impl;

import dev.clinic.simple.domain.ContaClinica;
import dev.clinic.simple.repository.ContaClinicaRepository;
import dev.clinic.simple.service.ContaClinicaService;
import dev.clinic.simple.service.dto.ContaClinicaDTO;
import dev.clinic.simple.service.mapper.ContaClinicaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContaClinica}.
 */
@Service
@Transactional
public class ContaClinicaServiceImpl implements ContaClinicaService {

    private final Logger log = LoggerFactory.getLogger(ContaClinicaServiceImpl.class);

    private final ContaClinicaRepository contaClinicaRepository;

    private final ContaClinicaMapper contaClinicaMapper;

    public ContaClinicaServiceImpl(ContaClinicaRepository contaClinicaRepository, ContaClinicaMapper contaClinicaMapper) {
        this.contaClinicaRepository = contaClinicaRepository;
        this.contaClinicaMapper = contaClinicaMapper;
    }

    @Override
    public ContaClinicaDTO save(ContaClinicaDTO contaClinicaDTO) {
        log.debug("Request to save ContaClinica : {}", contaClinicaDTO);
        ContaClinica contaClinica = contaClinicaMapper.toEntity(contaClinicaDTO);
        contaClinica = contaClinicaRepository.save(contaClinica);
        return contaClinicaMapper.toDto(contaClinica);
    }

    @Override
    public Optional<ContaClinicaDTO> partialUpdate(ContaClinicaDTO contaClinicaDTO) {
        log.debug("Request to partially update ContaClinica : {}", contaClinicaDTO);

        return contaClinicaRepository
            .findById(contaClinicaDTO.getId())
            .map(
                existingContaClinica -> {
                    contaClinicaMapper.partialUpdate(existingContaClinica, contaClinicaDTO);
                    return existingContaClinica;
                }
            )
            .map(contaClinicaRepository::save)
            .map(contaClinicaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContaClinicaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContaClinicas");
        return contaClinicaRepository.findAll(pageable).map(contaClinicaMapper::toDto);
    }

    /**
     *  Get all the contaClinicas where Cpf is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContaClinicaDTO> findAllWhereCpfIsNull() {
        log.debug("Request to get all contaClinicas where Cpf is null");
        return StreamSupport
            .stream(contaClinicaRepository.findAll().spliterator(), false)
            .filter(contaClinica -> contaClinica.getCpf() == null)
            .map(contaClinicaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContaClinicaDTO> findOne(Long id) {
        log.debug("Request to get ContaClinica : {}", id);
        return contaClinicaRepository.findById(id).map(contaClinicaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContaClinica : {}", id);
        contaClinicaRepository.deleteById(id);
    }
}
