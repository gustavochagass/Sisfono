package com.SisfonoGroup.Sisfono.mapper;

import com.SisfonoGroup.Sisfono.dto.PacienteCreateDTO;
import com.SisfonoGroup.Sisfono.dto.PacienteDTO;
import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import com.SisfonoGroup.Sisfono.entities.Paciente;
import org.springframework.stereotype.Component;

@Component // Transforma a classe em um componente Spring
public class PacienteMapper {

    // Converte PacienteCreateDTO em entidade Paciente
    public Paciente toEntity(PacienteCreateDTO dto, Fonoaudiologo fono) {
        Paciente paciente = new Paciente();
        // Não defina o ID aqui, pois ele será gerado pelo banco de dados
        paciente.setNomePaciente(dto.getNomePaciente());
        paciente.setNomeResponsavel(dto.getNomeResponsavel());
        paciente.setCartaoSus(dto.getCartaoSus());
        paciente.setRg(dto.getRg());
        paciente.setCpf(dto.getCpf());
        paciente.setTelefone(dto.getTelefone());
        paciente.setDiasSemana(dto.getDiasSemana());
        paciente.setDataInicio(dto.getDataInicio());
        paciente.setDataFim(dto.getDataFim());
        paciente.setHorario(dto.getHorario());
        paciente.setFonoaudiologo(fono); // Fonoaudiologo já resolvido pelo service
        return paciente;
    }

    // Converte entidade Paciente em PacienteDTO
    public PacienteDTO toDTO(Paciente paciente) {
        if (paciente == null) {
            return null;
        }
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNomePaciente(paciente.getNomePaciente());
        dto.setNomeResponsavel(paciente.getNomeResponsavel()); // Mapear o campo
        dto.setCartaoSus(paciente.getCartaoSus());           // Mapear o campo
        dto.setRg(paciente.getRg());                         // Mapear o campo
        dto.setCpf(paciente.getCpf());
        dto.setTelefone(paciente.getTelefone());
        dto.setDiasSemana(paciente.getDiasSemana());         // Mapear o campo
        dto.setDataInicio(paciente.getDataInicio());
        dto.setDataFim(paciente.getDataFim());
        dto.setHorario(paciente.getHorario());
        if (paciente.getFonoaudiologo() != null) {
            dto.setFonoaudiologoId(paciente.getFonoaudiologo().getId());
        }
        return dto;
    }
}
