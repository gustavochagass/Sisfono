package com.SisfonoGroup.Sisfono.service;

import com.SisfonoGroup.Sisfono.dto.FichaPacienteCreateDTO;
import com.SisfonoGroup.Sisfono.dto.FichaPacienteDTO;
import com.SisfonoGroup.Sisfono.entities.FichaPaciente;
import com.SisfonoGroup.Sisfono.entities.Paciente;
import com.SisfonoGroup.Sisfono.exceptions.NotFoundException;
import com.SisfonoGroup.Sisfono.mapper.FichaPacienteMapper;
import com.SisfonoGroup.Sisfono.repository.FichaPacienteRepository;
import com.SisfonoGroup.Sisfono.repository.PacienteRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class FichaPacienteService {

    private final FichaPacienteRepository fichaPacienteRepository;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public FichaPacienteDTO saveOrUpdateFichaPaciente(FichaPacienteCreateDTO dto) {
        // Encontra o paciente pelo ID fornecido no DTO
        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado com o ID: " + dto.pacienteId()));

        // Tenta encontrar uma ficha existente associada ao CPF
        Optional<FichaPaciente> existingFichaOptional = fichaPacienteRepository.findByCpfPaciente(dto.cpfPaciente());

        FichaPaciente fichaPaciente;

        if (existingFichaOptional.isPresent()) {
            fichaPaciente = existingFichaOptional.get();
            // Verifica se a ficha existente pertence ao paciente que está sendo atualizado
            if (!fichaPaciente.getPaciente().getId().equals(paciente.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já vinculado a outro paciente.");
            }
            // Atualiza os campos da ficha existente com os novos dados do DTO
            updateFichaFields(fichaPaciente, dto);
        } else {
            // Se não houver ficha existente, cria uma nova
            fichaPaciente = FichaPacienteMapper.toEntity(dto);
        }

        // Associa a ficha ao paciente encontrado
        fichaPaciente.setPaciente(paciente);
        FichaPaciente savedFicha = fichaPacienteRepository.save(fichaPaciente);

        return FichaPacienteMapper.toDTO(savedFicha);
    }

    @Transactional(readOnly = true)
    public FichaPacienteDTO getFichaPacienteById(Long id) {
        return fichaPacienteRepository.findById(id)
                .map(FichaPacienteMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ficha não encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public FichaPacienteDTO getFichaPacienteByCpfPaciente(String cpfPaciente) {
        return fichaPacienteRepository.findByCpfPaciente(cpfPaciente)
                .map(FichaPacienteMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ficha não encontrada com o CPF: " + cpfPaciente));
    }

    @Transactional(readOnly = true)
    public FichaPacienteDTO getFichaPacienteByPacienteId(Long pacienteId) {
        return fichaPacienteRepository.findByPacienteId(pacienteId)
                .map(FichaPacienteMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ficha não encontrada para o paciente com ID: " + pacienteId));
    }

    @Transactional(readOnly = true)
    public List<FichaPacienteDTO> getAllFichasPacientes() {
        return FichaPacienteMapper.toDTOList(fichaPacienteRepository.findAll());
    }

    @Transactional
    public FichaPacienteDTO updateFichaPaciente(Long id, FichaPacienteCreateDTO dto) {
        FichaPaciente ficha = fichaPacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ficha não encontrada com o ID: " + id));

        // Busca o paciente pelo ID fornecido no DTO
        Paciente newPaciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado com o ID: " + dto.pacienteId()));

        // Se o paciente associado à ficha mudar, atualiza
        if (!ficha.getPaciente().getId().equals(newPaciente.getId())) {
            ficha.setPaciente(newPaciente);
        }

        updateFichaFields(ficha, dto);
        return FichaPacienteMapper.toDTO(fichaPacienteRepository.save(ficha));
    }

    @Transactional
    public void deleteFichaPaciente(Long id) {
        if (!fichaPacienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ficha não encontrada com o ID: " + id);
        }
        fichaPacienteRepository.deleteById(id);
    }

    private void updateFichaFields(FichaPaciente ficha, FichaPacienteCreateDTO dto) {
        ficha.setNomePaciente(dto.nomePaciente());
        ficha.setIdade(dto.idade());
        ficha.setCid(dto.cid());
        ficha.setDiasSemana(dto.diasSemana());
        ficha.setCpfPaciente(dto.cpfPaciente());
        ficha.setHorarioAtendimento(dto.horarioAtendimento());
        ficha.setTurnoAtendimento(dto.turnoAtendimento());
        ficha.setDataNascimento(dto.dataNascimento());
        ficha.setRelatorioConteudo(dto.relatorioConteudo());
        ficha.setExercicioParaCasa(dto.exercicioParaCasa());
    }
}