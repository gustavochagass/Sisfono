// cadastroPaciente.js

// Funções de modal (mantidas, são úteis para feedback ao usuário)
function mostrarModal(mensagem) {
  const modal = document.getElementById("modal");
  const modalMsg = document.getElementById("modal-message");
  modalMsg.textContent = mensagem;
  modal.classList.remove("hidden");
}

function fecharModal() {
  const modal = document.getElementById("modal");
  modal.classList.add("hidden");
}

// Função para remover tudo que não for número (mantida)
function somenteNumeros(valor) {
  return valor.replace(/\D/g, "");
}

// Executa quando o DOM estiver carregado
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formPaciente");
  const modalCloseButton = document.getElementById("modal-close-button");

  if (modalCloseButton) {
    modalCloseButton.addEventListener("click", fecharModal);
  }

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    // 1. Obter o token e os dados do usuário do localStorage
    const token = localStorage.getItem("authToken");
    const userDataString = localStorage.getItem("userData");

    if (!token || !userDataString) {
      mostrarModal(
        "Você não está logado. Por favor, faça login para cadastrar um paciente."
      );
      // Opcional: redirecionar para a página de login
      // window.location.href = 'tela_login.html'; // Considere usar '/login' ou '/'
      return;
    }

    let fonoLogado;
    try {
      fonoLogado = JSON.parse(userDataString); // Agora o userData é o FonoaudiologoDTO
    } catch (e) {
      console.error("Erro ao parsear userData do localStorage:", e);
      mostrarModal(
        "Erro nos dados da sua sessão. Por favor, faça login novamente."
      );
      // Opcional: redirecionar para a página de login
      // window.location.href = 'tela_login.html'; // Considere usar '/login' ou '/'
      return;
    }

    // Validação para garantir que o ID do fonoaudiólogo está disponível
    if (!fonoLogado.id) {
      mostrarModal(
        "ID do fonoaudiólogo não encontrado nos dados de sessão. Faça login novamente."
      );
      // Opcional: redirecionar para a página de login
      // window.location.href = 'tela_login.html'; // Considere usar '/login' ou '/'
      return;
    }

    // 2. Construir o objeto de dados do paciente
    const data = {
      nomePaciente: form.nomePaciente.value.trim(),
      nomeResponsavel: form.nomeResponsavel.value.trim(),
      cartaoSus: somenteNumeros(form.cartaoSus.value),
      rg: somenteNumeros(form.rg.value),
      cpf: somenteNumeros(form.cpf.value),
      telefone: somenteNumeros(form.telefone.value),
      diasSemana: form.diasSemana.value,
      dataInicio: form.dataInicio.value,
      dataFim: form.dataFim.value,
      horario: form.horario.value,
      fonoaudiologoId: fonoLogado.id, // ID do fonoaudiólogo logado obtido do localStorage
    };

    // Validação simples no frontend (opcional, mas recomendado)
    if (
      !data.nomePaciente ||
      !data.cpf ||
      !data.dataInicio ||
      !data.horario ||
      !data.fonoaudiologoId
    ) {
      mostrarModal(
        "Por favor, preencha todos os campos obrigatórios: Nome do Paciente, CPF, Data de Início, Horário."
      );
      return;
    }

    try {
      // 3. Enviar a requisição com o cabeçalho de autorização
      // CORREÇÃO AQUI: Mudando de localhost para caminho relativo
      const response = await fetch("/api/pacientes", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`, // Envia o token JWT obtido do localStorage
        },
        body: JSON.stringify(data),
      });

      // 4. Tratar a resposta do servidor
      if (response.ok) {
        mostrarModal("Paciente cadastrado com sucesso!");
        form.reset(); // Limpa o formulário após o sucesso
      } else {
        // 5. Tratamento de erro robusto para exibir mensagens do backend
        let errorMessage = "Ocorreu um erro ao cadastrar o paciente.";
        const contentType = response.headers.get("content-type");

        if (contentType && contentType.includes("application/json")) {
          const errorData = await response.json();
          // Assume que o backend retorna um objeto JSON com uma chave 'message' ou lista de erros
          if (errorData.message) {
            errorMessage = errorData.message;
          } else if (errorData.errors) {
            // Para erros de validação do Spring (MethodArgumentNotValidException)
            errorMessage = Object.values(errorData.errors).join("; ");
          } else {
            errorMessage = JSON.stringify(errorData); // Fallback para qualquer outro JSON
          }
        } else {
          // Se não for JSON (ex: erro 401 Unauthorized, 403 Forbidden, 500 sem corpo JSON)
          errorMessage = `Erro: ${response.status} - ${response.statusText}. Por favor, verifique se você está logado(a) e tem permissão.`;
        }
        mostrarModal(errorMessage);
      }
    } catch (error) {
      // Erro de rede ou falha na comunicação
      console.error("Erro no envio da requisição de cadastro de paciente:", error);
      mostrarModal(
        "Falha na comunicação com o servidor. Verifique sua conexão e se o backend está rodando."
      );
    }
  });
});