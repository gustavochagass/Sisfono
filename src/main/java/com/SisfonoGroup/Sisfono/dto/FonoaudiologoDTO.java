package com.SisfonoGroup.Sisfono.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FonoaudiologoDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String crf;
    private String role; // NOVO CAMPO: Role para o DTO de retorno
}