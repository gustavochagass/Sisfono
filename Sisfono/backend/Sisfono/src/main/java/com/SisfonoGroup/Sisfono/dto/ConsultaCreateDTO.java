package com.SisfonoGroup.Sisfono.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaCreateDTO {
    @NotNull(message = "Data da consulta é obrigatória")
    @FutureOrPresent(message = "Data da consulta não pode ser no passado")
    private LocalDate dataConsulta;
    @NotNull(message = "Hora da consulta é obrigatória")
    private LocalTime horaConsulta;
    private String observacoes;
    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;
    @NotNull(message = "ID do fonoaudiólogo é obrigatório")
    private Long fonoaudiologoId;
}