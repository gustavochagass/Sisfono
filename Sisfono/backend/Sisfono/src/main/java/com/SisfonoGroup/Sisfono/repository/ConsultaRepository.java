package com.SisfonoGroup.Sisfono.repository;

import com.SisfonoGroup.Sisfono.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPacienteId(Long pacienteId);
    List<Consulta> findByFonoaudiologoId(Long fonoId);
}
