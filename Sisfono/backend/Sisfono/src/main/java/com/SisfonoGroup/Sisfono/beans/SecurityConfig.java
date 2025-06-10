package com.SisfonoGroup.Sisfono.beans;

import com.SisfonoGroup.Sisfono.jwt.JwtTokenFilter;
import com.SisfonoGroup.Sisfono.jwt.JwtTokenProvider; // Importe o JwtTokenProvider
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor; // Importe o Lombok para injeção via construtor

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Adicione esta anotação para injetar o JwtTokenProvider via construtor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider; // Injete o JwtTokenProvider

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/fonoaudiologos", "/api/fonoaudiologos/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pacientes/login").permitAll()
                        // CORREÇÃO: Altere de .authenticated() para .hasRole("FONOAUDIOLOGO")
                        // Se o fonoaudiólogo que cadastra tem a role "FONOAUDIOLOGO", esta é a forma correta.
                        .requestMatchers(HttpMethod.POST, "/api/pacientes").hasRole("FONOAUDIOLOGO")
                        .requestMatchers(HttpMethod.GET, "/api/pacientes/**").hasAnyRole("FONOAUDIOLOGO", "PACIENTE") // Adicione as regras que faltavam
                        .requestMatchers(HttpMethod.POST, "/api/consultas").hasRole("FONOAUDIOLOGO") // Adicione as regras que faltavam
                        .requestMatchers(HttpMethod.PATCH, "/api/consultas/{id}/status").hasRole("FONOAUDIOLOGO") // Adicione as regras que faltavam
                        .requestMatchers(HttpMethod.GET, "/api/consultas/**").hasAnyRole("FONOAUDIOLOGO", "PACIENTE") // Adicione as regras que faltavam
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // Injeção do filtro

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("null"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}