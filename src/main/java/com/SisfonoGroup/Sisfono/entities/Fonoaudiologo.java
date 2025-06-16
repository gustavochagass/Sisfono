package com.SisfonoGroup.Sisfono.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "TB_FONOAUDIOLOGO")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Fonoaudiologo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Column(name = "cpf", length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(name = "telefone", length = 11, nullable = false)
    private String telefone;

    @Column(name = "email", length = 60, nullable = false)
    private String email;

    @Column(name = "crf", length = 15, nullable = false) // CORREÇÃO: Aumentado para 15
    private String crf;

    @Column(name = "senha", length = 60, nullable = false)
    private String senha;

    @Column(name = "role", length = 20, nullable = false) // NOVO CAMPO: Role (ex: FONOAUDIOLOGO, ADMIN)
    private String role; // Ex: "FONOAUDIOLOGO", "PACIENTE_RESPONSAVEL"

    @OneToMany(mappedBy = "fonoaudiologo", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("fonoaudiologo")
    private List<Paciente> pacientes;
}