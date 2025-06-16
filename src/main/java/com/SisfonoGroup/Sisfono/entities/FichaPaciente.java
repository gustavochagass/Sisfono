package com.SisfonoGroup.Sisfono.entities;

import com.SisfonoGroup.Sisfono.enums.Turno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "TB_FICHA_PACIENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_paciente", nullable = false)
    private String nomePaciente;

    @Column(name = "idade")
    private Integer idade;

    @Column(name = "cid", length = 0)
    private String cid;

    @Column(name = "dias_semana")
    private String diasSemana;

    @Column(name = "cpf_paciente", length = 11, nullable = false, unique = true)
    private String cpfPaciente;

    @Column(name = "horario_atendimento")
    private LocalTime horarioAtendimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "turno_atendimento")
    private Turno turnoAtendimento;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "relatorio_conteudo", columnDefinition = "TEXT")
    private String relatorioConteudo;

    @Column(name = "exercicio_para_casa", columnDefinition = "TEXT")
    private String exercicioParaCasa;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}