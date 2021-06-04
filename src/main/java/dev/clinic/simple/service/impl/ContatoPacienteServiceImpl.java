package dev.clinic.simple.service.impl;

import dev.clinic.simple.domain.ContatoPaciente;
import dev.clinic.simple.repository.ContatoPacienteRepository;
import dev.clinic.simple.service.ContatoPacienteService;
import dev.clinic.simple.service.dto.ContatoPacienteDTO;
import dev.clinic.simple.service.mapper.ContatoPacienteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContatoPaciente}.
 */
@Service
@Transactional
public class ContatoPacienteServiceImpl implements ContatoPacienteService {

    private final Logger log = LoggerFactory.getLogger(ContatoPacienteServiceImpl.class);

    private final ContatoPacienteRepository contatoPacienteRepository;

    private final ContatoPacienteMapper contatoPacienteMapper;

    public ContatoPacienteServiceImpl(ContatoPacienteRepository contatoPacienteRepository, ContatoPacienteMapper contatoPacienteMapper) {
        this.contatoPacienteRepository = contatoPacienteRepository;
        this.contatoPacienteMapper = contatoPacienteMapper;
    }

    @Override
    public ContatoPacienteDTO save(ContatoPacienteDTO contatoPacienteDTO) {
        log.debug("Request to save ContatoPaciente : {}", contatoPacienteDTO);
        ContatoPaciente contatoPaciente = contatoPacienteMapper.toEntity(contatoPacienteDTO);
        contatoPaciente = contatoPacienteRepository.save(contatoPaciente);
        return contatoPacienteMapper.toDto(contatoPaciente);
    }

    @Override
    public Optional<ContatoPacienteDTO> partialUpdate(ContatoPacienteDTO contatoPacienteDTO) {
        log.debug("Request to partially update ContatoPaciente : {}", contatoPacienteDTO);

        return contatoPacienteRepository
            .findById(contatoPacienteDTO.getId())
            .map(
                existingContatoPaciente -> {
                    contatoPacienteMapper.partialUpdate(existingContatoPaciente, contatoPacienteDTO);
                    return existingContatoPaciente;
                }
            )
            .map(contatoPacienteRepository::save)
            .map(contatoPacienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContatoPacienteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContatoPacientes");
        return contatoPacienteRepository.findAll(pageable).map(contatoPacienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContatoPacienteDTO> findOne(Long id) {
        log.debug("Request to get ContatoPaciente : {}", id);
        return contatoPacienteRepository.findById(id).map(contatoPacienteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContatoPaciente : {}", id);
        contatoPacienteRepository.deleteById(id);
    }
}
