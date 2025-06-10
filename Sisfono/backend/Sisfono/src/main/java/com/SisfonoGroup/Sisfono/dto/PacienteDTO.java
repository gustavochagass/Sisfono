package com.SisfonoGroup.Sisfono.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    private Long id;
    private String nomePaciente;
    private String nomeResponsavel; // Adicionado
    private String cartaoSus;      // Adicionado
    private String rg;             // Adicionado
    private String cpf;
    private String telefone;
    private String diasSemana;     // Adicionado
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalTime horario;
    private Long fonoaudiologoId;
}