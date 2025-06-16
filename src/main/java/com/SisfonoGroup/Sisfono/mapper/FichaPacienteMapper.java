package com.SisfonoGroup.Sisfono.mapper;

import com.SisfonoGroup.Sisfono.dto.FichaPacienteCreateDTO;
import com.SisfonoGroup.Sisfono.dto.FichaPacienteDTO;
import com.SisfonoGroup.Sisfono.entities.FichaPaciente;
import com.SisfonoGroup.Sisfono.entities.Paciente;

import java.util.List;
import java.util.stream.Collectors;

public class FichaPacienteMapper {

    public static FichaPaciente toEntity(FichaPacienteCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        FichaPaciente fichaPaciente = new FichaPaciente();
        fichaPaciente.setNomePaciente(dto.nomePaciente());
        fichaPaciente.setIdade(dto.idade());
        fichaPaciente.setCid(dto.cid());
        fichaPaciente.setDiasSemana(dto.diasSemana());
        fichaPaciente.setCpfPaciente(dto.cpfPaciente());
        fichaPaciente.setHorarioAtendimento(dto.horarioAtendimento());
        fichaPaciente.setTurnoAtendimento(dto.turnoAtendimento());
        fichaPaciente.setDataNascimento(dto.dataNascimento());
        fichaPaciente.setRelatorioConteudo(dto.relatorioConteudo());
        fichaPaciente.setExercicioParaCasa(dto.exercicioParaCasa());

        if (dto.pacienteId() != null) {
            Paciente paciente = new Paciente();
            paciente.setId(dto.pacienteId());
            fichaPaciente.setPaciente(paciente);
        }
        return fichaPaciente;
    }

    public static FichaPacienteDTO toDTO(FichaPaciente fichaPaciente) {
        if (fichaPaciente == null) {
            return null;
        }
        Long pacienteId = (fichaPaciente.getPaciente() != null) ? fichaPaciente.getPaciente().getId() : null;
        return new FichaPacienteDTO(
                fichaPaciente.getId(),
                fichaPaciente.getNomePaciente(),
                fichaPaciente.getIdade(),
                fichaPaciente.getCid(),
                fichaPaciente.getDiasSemana(),
                fichaPaciente.getCpfPaciente(),
                fichaPaciente.getHorarioAtendimento(),
                fichaPaciente.getTurnoAtendimento(),
                fichaPaciente.getDataNascimento(),
                fichaPaciente.getRelatorioConteudo(),
                fichaPaciente.getExercicioParaCasa(),
                pacienteId
        );
    }

    public static List<FichaPacienteDTO> toDTOList(List<FichaPaciente> fichasPaciente) {
        if (fichasPaciente == null) {
            return null;
        }
        return fichasPaciente.stream()
                .map(FichaPacienteMapper::toDTO)
                .collect(Collectors.toList());
    }
}