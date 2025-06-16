package com.SisfonoGroup.Sisfono.service;

import com.SisfonoGroup.Sisfono.entities.Fonoaudiologo;
import com.SisfonoGroup.Sisfono.repository.FonoaudiologoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class FonoaudiologoDetailsService implements UserDetailsService {

    private final FonoaudiologoRepository fonoaudiologoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Este método será chamado pelo Spring Security para carregar o usuário
        // 'username' pode ser CPF ou E-mail, dependendo de como você configura o login
        Fonoaudiologo fonoaudiologo;
        if (username.contains("@")) {
            fonoaudiologo = fonoaudiologoRepository.findByEmail(username);
        } else {
            fonoaudiologo = fonoaudiologoRepository.findByCpf(username);
        }

        if (fonoaudiologo == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        // Constrói o objeto UserDetails que o Spring Security entende
        // A role deve ser prefixada com "ROLE_" aqui também
        return User.builder()
                .username(username)
                .password(fonoaudiologo.getSenha()) // Senha criptografada do banco de dados
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + fonoaudiologo.getRole()))) // ATENÇÃO AQUI: Busca a role da entidade
                .build();
    }
}