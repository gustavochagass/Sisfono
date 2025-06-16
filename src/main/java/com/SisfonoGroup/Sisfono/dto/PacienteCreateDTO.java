package com.SisfonoGroup.Sisfono.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent; // Para datas de início/fim se aplicável

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteCreateDTO {
    @NotBlank(message = "Nome do paciente é obrigatório")
    @Size(max = 255, message = "Nome do paciente não pode exceder 255 caracteres")
    private String nomePaciente;
    private String nomeResponsavel;
    private String cartaoSus;
    private String rg;
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
    private String cpf;
    private String telefone;
    private String diasSemana;
    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalTime horario;
    @NotNull(message = "ID do fonoaudiólogo é obrigatório para vincular o paciente")
    private Long fonoaudiologoId;
}