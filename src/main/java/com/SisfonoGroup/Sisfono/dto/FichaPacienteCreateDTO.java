package com.SisfonoGroup.Sisfono.dto;

import com.SisfonoGroup.Sisfono.enums.Turno;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalTime;

public record FichaPacienteCreateDTO(
        @NotBlank(message = "O nome do paciente é obrigatório.")
        String nomePaciente,
        Integer idade,
        String cid,
        String diasSemana,
        @NotBlank(message = "O CPF do paciente é obrigatório.")
        @Length(min = 11, max = 11, message = "O CPF deve ter 11 dígitos.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter apenas números.")
        String cpfPaciente,
        LocalTime horarioAtendimento,
        Turno turnoAtendimento,
        @PastOrPresent(message = "A data de nascimento não pode ser no futuro.")
        LocalDate dataNascimento,
        String relatorioConteudo,
        String exercicioParaCasa,
        @NotNull(message = "O ID do paciente é obrigatório.")
        Long pacienteId
) {}