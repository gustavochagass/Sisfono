package com.SisfonoGroup.Sisfono.controller;

import com.SisfonoGroup.Sisfono.dto.ConsultaCreateDTO;
import com.SisfonoGroup.Sisfono.dto.ConsultaDTO;
import com.SisfonoGroup.Sisfono.enums.StatusConsulta;
import com.SisfonoGroup.Sisfono.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ConsultaDTO> criarConsulta(@Valid @RequestBody ConsultaCreateDTO dto) {
        ConsultaDTO novaConsulta = consultaService.criarConsulta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConsulta);
    }

    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> listarConsultas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id, @RequestParam StatusConsulta status) {
        consultaService.alterarStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
