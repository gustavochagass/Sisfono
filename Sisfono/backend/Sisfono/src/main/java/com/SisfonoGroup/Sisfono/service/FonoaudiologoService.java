package com.SisfonoGroup.Sisfono.service;

import com.SisfonoGroup.Sisfono.dto.FonoaudiologoCreateDTO;
import com.SisfonoGroup.Sisfono.dto.FonoaudiologoDTO;
import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import com.SisfonoGroup.Sisfono.mapper.FonoaudiologoMapper;
import com.SisfonoGroup.Sisfono.repository.FonoaudiologoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FonoaudiologoService {

    private final FonoaudiologoRepository repository;
    private final FonoaudiologoMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public FonoaudiologoDTO salvar(FonoaudiologoCreateDTO createDTO) {
        Fonoaudiologo entity = mapper.toEntity(createDTO);

        String senhaCriptografada = passwordEncoder.encode(createDTO.getSenha());
        entity.setSenha(senhaCriptografada);

        Fonoaudiologo saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public Fonoaudiologo findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    public Fonoaudiologo findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<FonoaudiologoDTO> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }
}
