package dev.clinic.simple.service.impl;

import dev.clinic.simple.domain.Paciente;
import dev.clinic.simple.repository.PacienteRepository;
import dev.clinic.simple.service.PacienteService;
import dev.clinic.simple.service.dto.PacienteDTO;
import dev.clinic.simple.service.mapper.PacienteMapper;
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
 * Service Implementation for managing {@link Paciente}.
 */
@Service
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private final Logger log = LoggerFactory.getLogger(PacienteServiceImpl.class);

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    public PacienteServiceImpl(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    @Override
    public PacienteDTO save(PacienteDTO pacienteDTO) {
        log.debug("Request to save Paciente : {}", pacienteDTO);
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        paciente = pacienteRepository.save(paciente);
        return pacienteMapper.toDto(paciente);
    }

    @Override
    public Optional<PacienteDTO> partialUpdate(PacienteDTO pacienteDTO) {
        log.debug("Request to partially update Paciente : {}", pacienteDTO);

        return pacienteRepository
            .findById(pacienteDTO.getId())
            .map(
                existingPaciente -> {
                    pacienteMapper.partialUpdate(existingPaciente, pacienteDTO);
                    return existingPaciente;
                }
            )
            .map(pacienteRepository::save)
            .map(pacienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pacientes");
        return pacienteRepository.findAll(pageable).map(pacienteMapper::toDto);
    }

    /**
     *  Get all the pacientes where Cpf is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PacienteDTO> findAllWhereCpfIsNull() {
        log.debug("Request to get all pacientes where Cpf is null");
        return StreamSupport
            .stream(pacienteRepository.findAll().spliterator(), false)
            .filter(paciente -> paciente.getCpf() == null)
            .map(pacienteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PacienteDTO> findOne(Long id) {
        log.debug("Request to get Paciente : {}", id);
        return pacienteRepository.findById(id).map(pacienteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Paciente : {}", id);
        pacienteRepository.deleteById(id);
    }
}
