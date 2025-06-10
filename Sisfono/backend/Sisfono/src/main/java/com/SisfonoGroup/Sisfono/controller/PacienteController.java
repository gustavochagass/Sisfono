package com.SisfonoGroup.Sisfono.controller;

import com.SisfonoGroup.Sisfono.dto.PacienteCreateDTO;
import com.SisfonoGroup.Sisfono.dto.PacienteDTO;
import com.SisfonoGroup.Sisfono.entities.Paciente;
import com.SisfonoGroup.Sisfono.mapper.PacienteMapper;
import com.SisfonoGroup.Sisfono.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;
    private final PacienteMapper mapper;

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> salvar(@Valid @RequestBody PacienteCreateDTO createDTO) {
        if (createDTO.getDataInicio() != null &&
                createDTO.getDataFim() != null &&
                createDTO.getDataInicio().isAfter(createDTO.getDataFim())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Data de início não pode ser posterior à data de fim"
            );
        }

        // As exceções de IllegalArgumentException e NoSuchElementException serão tratadas pelo GlobalExceptionHandler
        PacienteDTO salvo = service.salvar(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String cpf = body.get("cpf");

        if (cpf == null || cpf.isEmpty()) {
            return ResponseEntity.badRequest().body("CPF é obrigatório.");
        }

        Paciente paciente = service.findByCpf(cpf);
        if (paciente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado.");
        }

        return ResponseEntity.ok(mapper.toDTO(paciente));
    }
}
