package dev.clinic.simple.service.impl;

import dev.clinic.simple.domain.EnderecoPaciente;
import dev.clinic.simple.repository.EnderecoPacienteRepository;
import dev.clinic.simple.service.EnderecoPacienteService;
import dev.clinic.simple.service.dto.EnderecoPacienteDTO;
import dev.clinic.simple.service.mapper.EnderecoPacienteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EnderecoPaciente}.
 */
@Service
@Transactional
public class EnderecoPacienteServiceImpl implements EnderecoPacienteService {

    private final Logger log = LoggerFactory.getLogger(EnderecoPacienteServiceImpl.class);

    private final EnderecoPacienteRepository enderecoPacienteRepository;

    private final EnderecoPacienteMapper enderecoPacienteMapper;

    public EnderecoPacienteServiceImpl(
        EnderecoPacienteRepository enderecoPacienteRepository,
        EnderecoPacienteMapper enderecoPacienteMapper
    ) {
        this.enderecoPacienteRepository = enderecoPacienteRepository;
        this.enderecoPacienteMapper = enderecoPacienteMapper;
    }

    @Override
    public EnderecoPacienteDTO save(EnderecoPacienteDTO enderecoPacienteDTO) {
        log.debug("Request to save EnderecoPaciente : {}", enderecoPacienteDTO);
        EnderecoPaciente enderecoPaciente = enderecoPacienteMapper.toEntity(enderecoPacienteDTO);
        enderecoPaciente = enderecoPacienteRepository.save(enderecoPaciente);
        return enderecoPacienteMapper.toDto(enderecoPaciente);
    }

    @Override
    public Optional<EnderecoPacienteDTO> partialUpdate(EnderecoPacienteDTO enderecoPacienteDTO) {
        log.debug("Request to partially update EnderecoPaciente : {}", enderecoPacienteDTO);

        return enderecoPacienteRepository
            .findById(enderecoPacienteDTO.getId())
            .map(
                existingEnderecoPaciente -> {
                    enderecoPacienteMapper.partialUpdate(existingEnderecoPaciente, enderecoPacienteDTO);
                    return existingEnderecoPaciente;
                }
            )
            .map(enderecoPacienteRepository::save)
            .map(enderecoPacienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnderecoPacienteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EnderecoPacientes");
        return enderecoPacienteRepository.findAll(pageable).map(enderecoPacienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnderecoPacienteDTO> findOne(Long id) {
        log.debug("Request to get EnderecoPaciente : {}", id);
        return enderecoPacienteRepository.findById(id).map(enderecoPacienteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EnderecoPaciente : {}", id);
        enderecoPacienteRepository.deleteById(id);
    }
}
