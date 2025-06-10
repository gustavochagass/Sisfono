package com.SisfonoGroup.Sisfono.controller;

import com.SisfonoGroup.Sisfono.dto.FonoaudiologoCreateDTO;
import com.SisfonoGroup.Sisfono.dto.FonoaudiologoDTO;
import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import com.SisfonoGroup.Sisfono.jwt.JwtTokenProvider;
import com.SisfonoGroup.Sisfono.service.FonoaudiologoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fonoaudiologos")
@RequiredArgsConstructor
public class FonoaudiologoController {

    private final FonoaudiologoService service;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping
    public ResponseEntity<List<FonoaudiologoDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<FonoaudiologoDTO> salvar(@Valid @RequestBody FonoaudiologoCreateDTO createDTO) {
        FonoaudiologoDTO salvo = service.salvar(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String cpf = body.get("cpf");
        String email = body.get("email");
        String senha = body.get("senha");

        Fonoaudiologo fono = null;

        if (cpf != null) {
            fono = service.findByCpf(cpf);
        } else if (email != null) {
            fono = service.findByEmail(email);
        }

        if (fono == null || !passwordEncoder.matches(senha, fono.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }

        // Adicionar a geração do token JWT
        String token = jwtTokenProvider.generateToken(fono.getCpf() != null ? fono.getCpf() : fono.getEmail(), "FONOAUDIOLOGO");

        Map<String, Object> response = new HashMap<>();
        response.put("fonoaudiologo", new FonoaudiologoDTO(
                fono.getId(), fono.getNome(), fono.getCpf(),
                fono.getTelefone(), fono.getEmail(), fono.getCrf()));
        response.put("token", token); // Incluir o token na resposta

        return ResponseEntity.ok(response);
    }

}
