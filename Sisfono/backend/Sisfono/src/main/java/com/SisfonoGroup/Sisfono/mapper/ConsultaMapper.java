package com.SisfonoGroup.Sisfono.mapper;

import com.SisfonoGroup.Sisfono.dto.ConsultaCreateDTO;
import com.SisfonoGroup.Sisfono.dto.ConsultaDTO;
import com.SisfonoGroup.Sisfono.entities.Consulta;
import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import com.SisfonoGroup.Sisfono.entities.Paciente;
import com.SisfonoGroup.Sisfono.enums.StatusConsulta;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMapper {

    public ConsultaDTO toDTO(Consulta consulta) {
        return new ConsultaDTO(
                consulta.getId(),
                consulta.getDataConsulta(),
                consulta.getHoraConsulta(),
                consulta.getObservacoes(),
                consulta.getPaciente().getId(),
                consulta.getFonoaudiologo().getId(),
                consulta.getStatus()
        );
    }

    public Consulta toEntity(ConsultaCreateDTO dto, Paciente paciente, Fonoaudiologo fonoaudiologo) {
        Consulta consulta = new Consulta();
        consulta.setDataConsulta(dto.getDataConsulta());
        consulta.setHoraConsulta(dto.getHoraConsulta());
        consulta.setObservacoes(dto.getObservacoes());
        consulta.setPaciente(paciente);
        consulta.setFonoaudiologo(fonoaudiologo);
        consulta.setStatus(StatusConsulta.AGENDADA);
        return consulta;
    }
}
