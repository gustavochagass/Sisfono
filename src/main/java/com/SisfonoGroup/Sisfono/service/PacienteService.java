package com.SisfonoGroup.Sisfono.service;


import com.SisfonoGroup.Sisfono.dto.PacienteCreateDTO;
import com.SisfonoGroup.Sisfono.dto.PacienteDTO;
import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import com.SisfonoGroup.Sisfono.entities.Paciente;
import com.SisfonoGroup.Sisfono.exceptions.NotFoundException;
import com.SisfonoGroup.Sisfono.mapper.PacienteMapper;
import com.SisfonoGroup.Sisfono.repository.FichaPacienteRepository;
import com.SisfonoGroup.Sisfono.repository.PacienteRepository;
import com.SisfonoGroup.Sisfono.repository.FonoaudiologoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;
    private final FonoaudiologoRepository fonoRepo;
    private final FichaPacienteRepository fichaPacienteRepository;
    private final PacienteMapper mapper;

    public PacienteDTO salvar(PacienteCreateDTO dto) {
        if (repository.findByCpf(dto.getCpf()) != null) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        Fonoaudiologo fono = fonoRepo.findById(dto.getFonoaudiologoId())
                .orElseThrow(() -> new NoSuchElementException("Fonoaudiólogo não encontrado com o ID: " + dto.getFonoaudiologoId()));

        Paciente paciente = mapper.toEntity(dto, fono);
        return mapper.toDTO(repository.save(paciente));
    }

    public List<PacienteDTO> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public PacienteDTO buscarPorId(Long id) {
        Paciente paciente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado com o ID: " + id));
        return mapper.toDTO(paciente);
    }

    public PacienteDTO buscarPorCpf(String cpf) {
        Paciente paciente = repository.findByCpf(cpf);
        if (paciente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado com o CPF: " + cpf);
        }
        return mapper.toDTO(paciente);
    }

    public Paciente findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    @Transactional
    public void deletePacienteAndFicha(Long pacienteId) {
        Paciente paciente = repository.findById(pacienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado com o ID: " + pacienteId));

        fichaPacienteRepository.findByPacienteId(pacienteId).ifPresent(fichaPacienteRepository::delete);

        repository.delete(paciente);
    }
}
