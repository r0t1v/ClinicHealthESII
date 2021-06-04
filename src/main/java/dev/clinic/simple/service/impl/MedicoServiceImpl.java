package dev.clinic.simple.service.impl;

import dev.clinic.simple.domain.Medico;
import dev.clinic.simple.repository.MedicoRepository;
import dev.clinic.simple.service.MedicoService;
import dev.clinic.simple.service.dto.MedicoDTO;
import dev.clinic.simple.service.mapper.MedicoMapper;
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
 * Service Implementation for managing {@link Medico}.
 */
@Service
@Transactional
public class MedicoServiceImpl implements MedicoService {

    private final Logger log = LoggerFactory.getLogger(MedicoServiceImpl.class);

    private final MedicoRepository medicoRepository;

    private final MedicoMapper medicoMapper;

    public MedicoServiceImpl(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    @Override
    public MedicoDTO save(MedicoDTO medicoDTO) {
        log.debug("Request to save Medico : {}", medicoDTO);
        Medico medico = medicoMapper.toEntity(medicoDTO);
        medico = medicoRepository.save(medico);
        return medicoMapper.toDto(medico);
    }

    @Override
    public Optional<MedicoDTO> partialUpdate(MedicoDTO medicoDTO) {
        log.debug("Request to partially update Medico : {}", medicoDTO);

        return medicoRepository
            .findById(medicoDTO.getId())
            .map(
                existingMedico -> {
                    medicoMapper.partialUpdate(existingMedico, medicoDTO);
                    return existingMedico;
                }
            )
            .map(medicoRepository::save)
            .map(medicoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medicos");
        return medicoRepository.findAll(pageable).map(medicoMapper::toDto);
    }

    /**
     *  Get all the medicos where Nome is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MedicoDTO> findAllWhereNomeIsNull() {
        log.debug("Request to get all medicos where Nome is null");
        return StreamSupport
            .stream(medicoRepository.findAll().spliterator(), false)
            .filter(medico -> medico.getNome() == null)
            .map(medicoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicoDTO> findOne(Long id) {
        log.debug("Request to get Medico : {}", id);
        return medicoRepository.findById(id).map(medicoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Medico : {}", id);
        medicoRepository.deleteById(id);
    }
}
