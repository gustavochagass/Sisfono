package com.SisfonoGroup.Sisfono.dto;

import com.SisfonoGroup.Sisfono.enums.StatusConsulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {
    private Long id;
    private LocalDate dataConsulta;
    private LocalTime horaConsulta;
    private String observacoes;
    private Long pacienteId;
    private Long fonoaudiologoId;
    private StatusConsulta status;
}
