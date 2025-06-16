package com.SisfonoGroup.Sisfono.repository;


import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FonoaudiologoRepository extends JpaRepository<Fonoaudiologo, Long> {
    Fonoaudiologo findByCpf(String cpf);
    Fonoaudiologo findByEmail(String email);
}
