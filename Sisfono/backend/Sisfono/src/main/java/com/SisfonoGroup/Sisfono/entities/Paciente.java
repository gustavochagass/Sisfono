package com.SisfonoGroup.Sisfono.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate; // Importa LocalDate para datas
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "tb_paciente")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_paciente", nullable = false, length = 100)
    private String nomePaciente;

    @Column(name = "nome_responsavel", nullable = false, length = 100)
    private String nomeResponsavel;

    @Column(name = "cartao_sus", nullable = false, length = 20)
    private String cartaoSus;

    @Column(name = "rg", nullable = false, length = 10)
    private String rg;

    @Column(name = "cpf", nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(name = "telefone", nullable = false, length = 11)
    private String telefone;

    @Column(name = "dias_semana", nullable = false)
    private String diasSemana;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "horario", nullable = false)
    private LocalTime horario;

    @ManyToOne
    @JoinColumn(name = "fono_id", nullable = false)
    private Fonoaudiologo fonoaudiologo;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Consulta> consultas;
}