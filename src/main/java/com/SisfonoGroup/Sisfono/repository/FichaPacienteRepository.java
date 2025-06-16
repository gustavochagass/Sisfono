package com.SisfonoGroup.Sisfono.repository;


import com.SisfonoGroup.Sisfono.entities.FichaPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FichaPacienteRepository extends JpaRepository<FichaPaciente, Long> {
     Optional<FichaPaciente> findByCpfPaciente(String cpfPaciente);
     Optional<FichaPaciente> findByPacienteId(Long pacienteId);
}