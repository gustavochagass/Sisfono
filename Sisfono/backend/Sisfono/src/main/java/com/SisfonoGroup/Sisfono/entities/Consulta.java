package com.SisfonoGroup.Sisfono.entities;

import com.SisfonoGroup.Sisfono.enums.StatusConsulta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "TB_CONSULTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_consulta", nullable = false)
    private LocalDate dataConsulta;

    @Column(name = "hora_consulta", nullable = false)
    private LocalTime horaConsulta;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    @JsonIgnoreProperties("consultas")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "fonoaudiologo_id", nullable = false)
    @JsonIgnoreProperties("consultas")
    private Fonoaudiologo fonoaudiologo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusConsulta status;
}