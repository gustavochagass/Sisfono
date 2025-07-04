package com.SisfonoGroup.Sisfono.mapper;

import com.SisfonoGroup.Sisfono.dto.FonoaudiologoCreateDTO;
import com.SisfonoGroup.Sisfono.dto.FonoaudiologoDTO;
import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import org.springframework.stereotype.Component;

@Component
public class FonoaudiologoMapper {

    public FonoaudiologoDTO toDTO(Fonoaudiologo entity) {
        return new FonoaudiologoDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getTelefone(),
                entity.getEmail(),
                entity.getCrf(),
                entity.getRole() // ADICIONADO: Pega a role da entidade para o DTO
        );
    }

    public Fonoaudiologo toEntity(FonoaudiologoCreateDTO dto) {
        Fonoaudiologo entity = new Fonoaudiologo();
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setTelefone(dto.getTelefone());
        entity.setEmail(dto.getEmail());
        entity.setCrf(dto.getCrf());
        entity.setSenha(dto.getSenha());
        entity.setRole(dto.getRole()); // ADICIONADO: Seta a role do DTO na entidade
        return entity;
    }
}