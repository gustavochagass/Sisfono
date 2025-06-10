package com.SisfonoGroup.Sisfono.repository;

import com.SisfonoGroup.Sisfono.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByCpf(String cpf);
}

