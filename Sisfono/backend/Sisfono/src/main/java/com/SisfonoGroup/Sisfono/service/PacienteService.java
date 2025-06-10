package com.SisfonoGroup.Sisfono.service;


import com.SisfonoGroup.Sisfono.dto.PacienteCreateDTO;
import com.SisfonoGroup.Sisfono.dto.PacienteDTO;
import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import com.SisfonoGroup.Sisfono.entities.Paciente;
import com.SisfonoGroup.Sisfono.mapper.PacienteMapper;
import com.SisfonoGroup.Sisfono.repository.PacienteRepository;
import com.SisfonoGroup.Sisfono.repository.FonoaudiologoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;
    private final FonoaudiologoRepository fonoRepo;
    private final PacienteMapper mapper; // PacienteMapper agora é um componente Spring

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

    public Paciente findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}

