package com.SisfonoGroup.Sisfono.service;

import com.SisfonoGroup.Sisfono.dto.ConsultaCreateDTO;
import com.SisfonoGroup.Sisfono.dto.ConsultaDTO;
import com.SisfonoGroup.Sisfono.entities.Consulta;
import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import com.SisfonoGroup.Sisfono.entities.Paciente;
import com.SisfonoGroup.Sisfono.enums.StatusConsulta;
import com.SisfonoGroup.Sisfono.exceptions.NotFoundException;
import com.SisfonoGroup.Sisfono.mapper.ConsultaMapper;
import com.SisfonoGroup.Sisfono.repository.ConsultaRepository;
import com.SisfonoGroup.Sisfono.repository.FonoaudiologoRepository;
import com.SisfonoGroup.Sisfono.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final FonoaudiologoRepository fonoaudiologoRepository;
    private final ConsultaMapper consultaMapper;

    public ConsultaDTO criarConsulta(ConsultaCreateDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente não encontrado"));

        Fonoaudiologo fono = fonoaudiologoRepository.findById(dto.getFonoaudiologoId())
                .orElseThrow(() -> new NotFoundException("Fonoaudiólogo não encontrado"));

        Consulta consulta = consultaMapper.toEntity(dto, paciente, fono);
        consultaRepository.save(consulta);

        return consultaMapper.toDTO(consulta);
    }

    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll()
                .stream()
                .map(consultaMapper::toDTO)
                .toList();
    }

    public void alterarStatus(Long id, StatusConsulta novoStatus) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        consulta.setStatus(novoStatus);
        consultaRepository.save(consulta);
    }
}
