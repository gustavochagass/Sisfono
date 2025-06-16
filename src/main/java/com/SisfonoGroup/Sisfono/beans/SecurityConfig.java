package com.SisfonoGroup.Sisfono.beans;

import com.SisfonoGroup.Sisfono.jwt.JwtTokenFilter;
import com.SisfonoGroup.Sisfono.jwt.JwtTokenProvider;
import com.SisfonoGroup.Sisfono.service.FonoaudiologoDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final FonoaudiologoDetailsService fonoaudiologoDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                // REMOVIDO: .formLogin(...) para evitar conflito com autenticação JWT
                .authorizeHttpRequests(authz -> authz
                    // PERMITIR ACESSO PÚBLICO ÀS PÁGINAS HTML E RECURSOS ESTÁTICOS
                    .requestMatchers(HttpMethod.GET, "/", "/login", // URL raiz e /login (do ViewController)
                            "/*.html", // Todos os .html (para segurança geral)
                            "/tela_login.html", "/tela_cadastro.html", "/tela_agendamento.html",
                            "/tela_area_responsavel.html", "/tela_cadastroPaciente.html",
                            "/tela_calendarioCompleto.html", "/tela_ficha.html",
                            "/tela_fonoPrincipal.html", "/tela_pacientes.html",
                            "/detalhes_paciente.html", "/index.html",
                            // URLs mapeadas pelo ViewController (sem .html)
                            "/agendamento", "/cadastro", "/detalhes_paciente", "/index", "/pacientes",
                            "/area_responsavel", "/cadastroPaciente", "/calendarioCompleto", "/ficha",
                            "/assets/**", // Recursos estáticos (CSS, JS, Imagens)
                            "/favicon.ico" // Ícone do site
                            ).permitAll()
                    // Endpoints de API públicos (cadastro de fono, login de fono/paciente)
                    .requestMatchers(HttpMethod.POST, "/api/fonoaudiologos", "/api/fonoaudiologos/login", "/api/pacientes/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/fichas-paciente/**").permitAll() // Se essa API é acessível sem auth

                    // REGRAS DE ACESSO PROTEGIDO COM BASE EM ROLES (FUNÇÕES)
                    .requestMatchers(HttpMethod.GET, "/fonoPrincipal").hasRole("FONOAUDIOLOGO") // AGORA PROTEGIDO
                    .requestMatchers(HttpMethod.POST, "/api/pacientes").hasRole("FONOAUDIOLOGO")
                    .requestMatchers(HttpMethod.GET, "/api/pacientes/**").hasAnyRole("FONOAUDIOLOGO", "PACIENTE")
                    .requestMatchers(HttpMethod.DELETE, "/api/pacientes/**").hasRole("FONOAUDIOLOGO")
                    .requestMatchers(HttpMethod.POST, "/api/consultas").hasRole("FONOAUDIOLOGO")
                    .requestMatchers(HttpMethod.PATCH, "/api/consultas/{id}/status").hasRole("FONOAUDIOLOGO")
                    .requestMatchers(HttpMethod.GET, "/api/consultas/**").hasAnyRole("FONOAUDIOLOGO", "PACIENTE")
                    .requestMatchers(HttpMethod.GET, "/api/fonoaudiologos/me").hasRole("FONOAUDIOLOGO")
                    .requestMatchers(HttpMethod.POST, "/api/fonoaudiologos/logout").hasRole("FONOAUDIOLOGO")
                    .requestMatchers(HttpMethod.POST, "/api/fichas-paciente").hasRole("FONOAUDIOLOGO")
                    .requestMatchers(HttpMethod.PUT, "/api/fichas-paciente/**").hasRole("FONOAUDIOLOGO")
                    .requestMatchers(HttpMethod.DELETE, "/api/fichas-paciente/**").hasRole("FONOAUDIOLOGO")

                    // Todas as outras requisições requerem autenticação
                    .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // Seu filtro JWT

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Auth-Token", "Access-Control-Allow-Origin"));
        configuration.setExposedHeaders(List.of("X-Auth-Token", "Access-Control-Allow-Origin"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}