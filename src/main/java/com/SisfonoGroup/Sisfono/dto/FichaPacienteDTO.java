package com.SisfonoGroup.Sisfono.dto;


import com.SisfonoGroup.Sisfono.enums.Turno;

import java.time.LocalDate;
import java.time.LocalTime;

public record FichaPacienteDTO(
        Long id,
        String nomePaciente,
        Integer idade,
        String cid,
        String diasSemana,
        String cpfPaciente,
        LocalTime horarioAtendimento,
        Turno turnoAtendimento,
        LocalDate dataNascimento,
        String relatorioConteudo,
        String exercicioParaCasa,
        Long pacienteId
) {}