package com.SisfonoGroup.Sisfono.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // Mapeia a URL raiz "/" para a sua tela de login
    @GetMapping("/")
    public String showLoginPage() {
        return "index"; // CORRIGIDO: Retorna "index" porque o arquivo HTML se chama index.html
    }

    // Mapeia a URL "/login" para a sua tela de login
    @GetMapping("/login")
    public String loginPage() {
        return "index"; // CORRIGIDO: Retorna "index"
    }

    // Mapeamentos para suas outras telas HTML (o resto permanece igual)
    @GetMapping("/agendamento")
    public String agendamentoPage() {
        return "tela_agendamento";
    }

    @GetMapping("/cadastro")
    public String cadastroPage() {
        return "tela_cadastro";
    }

    @GetMapping("/fonoPrincipal")
    public String fonoPrincipalPage() {
        return "tela_fonoPrincipal";
    }

    @GetMapping("/area_responsavel")
    public String areaResponsavelPage() {
        return "tela_area_responsavel";
    }

    @GetMapping("/cadastroPaciente")
    public String cadastroPacientePage() {
        return "tela_cadastroPaciente";
    }

    @GetMapping("/calendarioCompleto")
    public String calendarioCompletoPage() {
        return "tela_calendarioCompleto";
    }

    @GetMapping("/ficha")
    public String fichaPage() {
        return "tela_ficha";
    }

    @GetMapping("/pacientes")
    public String pacientesPage() {
        return "pacientes";
    }

    @GetMapping("/detalhes_paciente")
    public String detalhesPacientePage() {
        return "detalhes_paciente";
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index"; // Este já estava correto se a URL /index for acessada
    }
}