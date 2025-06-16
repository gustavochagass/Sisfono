package com.SisfonoGroup.Sisfono.controller;

import com.SisfonoGroup.Sisfono.dto.FichaPacienteCreateDTO;
import com.SisfonoGroup.Sisfono.dto.FichaPacienteDTO;
import com.SisfonoGroup.Sisfono.service.FichaPacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fichas-paciente")
@RequiredArgsConstructor
public class FichaPacienteController {

    private final FichaPacienteService fichaPacienteService;

    @PostMapping
    public ResponseEntity<FichaPacienteDTO> createFicha(@Valid @RequestBody FichaPacienteCreateDTO dto) {
        FichaPacienteDTO savedFicha = fichaPacienteService.saveOrUpdateFichaPaciente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFicha);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FichaPacienteDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fichaPacienteService.getFichaPacienteById(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<FichaPacienteDTO> getByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(fichaPacienteService.getFichaPacienteByCpfPaciente(cpf));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<FichaPacienteDTO> getByPacienteId(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(fichaPacienteService.getFichaPacienteByPacienteId(pacienteId));
    }

    @GetMapping
    public ResponseEntity<List<FichaPacienteDTO>> getAll() {
        return ResponseEntity.ok(fichaPacienteService.getAllFichasPacientes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FichaPacienteDTO> updateFicha(@PathVariable Long id, @Valid @RequestBody FichaPacienteCreateDTO dto) {
        return ResponseEntity.ok(fichaPacienteService.updateFichaPaciente(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fichaPacienteService.deleteFichaPaciente(id);
        return ResponseEntity.noContent().build();
    }
}
