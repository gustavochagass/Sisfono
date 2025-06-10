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

    @Column(name = "crf", length = 6, nullable = false)
    private String crf;

    @Column(name = "senha", length = 60, nullable = false)
    private String senha;

    @OneToMany(mappedBy = "fonoaudiologo", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("fonoaudiologo")
    private List<Paciente> pacientes;

    @OneToMany(mappedBy = "fonoaudiologo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Consulta> consultas;

}