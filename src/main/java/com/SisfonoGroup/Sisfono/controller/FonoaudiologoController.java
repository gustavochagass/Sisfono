package com.SisfonoGroup.Sisfono.controller;

import com.SisfonoGroup.Sisfono.dto.FonoaudiologoCreateDTO;
import com.SisfonoGroup.Sisfono.dto.FonoaudiologoDTO;
import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import com.SisfonoGroup.Sisfono.jwt.JwtTokenProvider;
import com.SisfonoGroup.Sisfono.service.FonoaudiologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/fonoaudiologos")
@RequiredArgsConstructor
public class FonoaudiologoController {

    private final FonoaudiologoService service;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider; // Certifique-se de que este esteja injetado e funcionando

    @GetMapping
    public ResponseEntity<List<FonoaudiologoDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<FonoaudiologoDTO> salvar(@RequestBody FonoaudiologoCreateDTO createDTO) {
        FonoaudiologoDTO salvo = service.salvar(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String cpf = body.get("cpf");
        String email = body.get("email");
        String senha = body.get("senha");

        Fonoaudiologo fono = null;

        if (cpf != null && !cpf.isEmpty()) {
            fono = service.findByCpf(cpf);
        } else if (email != null && !email.isEmpty()) {
            fono = service.findByEmail(email);
        }

        if (fono == null || !passwordEncoder.matches(senha, fono.getSenha())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Credenciais inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        String subject = (fono.getCpf() != null && !fono.getCpf().isEmpty()) ? fono.getCpf() : fono.getEmail();
        String token = jwtTokenProvider.generateToken(subject, "FONOAUDIOLOGO");
        Map<String, Object> response = new HashMap<>();
        response.put("fonoaudiologo", new FonoaudiologoDTO(
                fono.getId(), fono.getNome(), fono.getCpf(),
                fono.getTelefone(), fono.getEmail(), fono.getCrf(),
                fono.getRole())); // ADICIONADO: fono.getRole()
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedFonoaudiologo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Não autenticado"));
        }

        String username = authentication.getName();

        Fonoaudiologo fono = null;
        if (username.contains("@")) { // Se for um email
            fono = service.findByEmail(username);
        } else { // Se for um CPF
            fono = service.findByCpf(username);
        }

        if (fono == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Fonoaudiólogo não encontrado."));
        }

        FonoaudiologoDTO fonoDTO = new FonoaudiologoDTO(
                fono.getId(), fono.getNome(), fono.getCpf(),
                fono.getTelefone(), fono.getEmail(), fono.getCrf(),
                fono.getRole()); // ADICIONADO: fono.getRole()

        return ResponseEntity.ok(fonoDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout bem-sucedido."));
    }
}