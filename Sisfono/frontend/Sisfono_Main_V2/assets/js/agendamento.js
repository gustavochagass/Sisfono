// Função para exibir o modal com uma mensagem
function mostrarModal(mensagem) {
  const modal = document.getElementById("modal"); // Obtém o elemento do modal pelo ID
  const modalMsg = document.getElementById("modal-message"); // Obtém o elemento da mensagem dentro do modal
  modalMsg.textContent = mensagem; // Define o texto da mensagem no modal
  modal.classList.remove("hidden"); // Remove a classe "hidden" para mostrar o modal
}

// Função para esconder o modal
function fecharModal() {
  const modal = document.getElementById("modal"); // Obtém o elemento do modal pelo ID
  modal.classList.add("hidden"); // Adiciona a classe "hidden" para ocultar o modal
}

// Executa quando o DOM da página estiver completamente carregado
document.addEventListener("DOMContentLoaded", async () => {
  
  // BLOCO 1: Carregamento da lista de fonoaudiólogos
  try {
    const select = document.getElementById("select-profissional"); // Obtém o select onde os profissionais serão listados
    const response = await fetch("http://localhost:8080/api/fonoaudiologos"); // Faz uma requisição GET à API

    if (!response.ok) {
      throw new Error("Erro ao buscar fonoaudiólogos"); // Se a resposta não for OK, lança erro
    }

    const lista = await response.json(); // Converte a resposta em JSON
    lista.forEach((fono) => {
      const option = document.createElement("option"); // Cria um novo elemento <option>
      option.value = fono.id; // Define o valor do option como o ID do fonoaudiólogo
      option.textContent = fono.nome; // Define o texto visível como o nome do fonoaudiólogo
      select.appendChild(option); // Adiciona a opção ao select
    });
  } catch (error) {
    console.error("Erro ao carregar fonoaudiólogos:", error); // Exibe o erro no console
    mostrarModal("Erro ao carregar lista de profissionais"); // Mostra o erro em um modal
  }

  // BLOCO 2: Configuração do envio do formulário
  const form = document.querySelector(".form-agendamento"); // Seleciona o formulário pela classe
  form.addEventListener("submit", async (event) => {
    event.preventDefault(); // Impede o comportamento padrão do formulário (recarregar página)

    // Verifica se um profissional foi selecionado
    if (!form.fonoaudiologo.value) {
      mostrarModal("Selecione um profissional"); // Exibe aviso se não for selecionado
      return; // Encerra a execução se não houver profissional selecionado
    }

    try {
      // Cria um objeto com os dados do formulário
      const data = {
        nomePaciente: form.nomePaciente.value.trim(), // Remove espaços extras do início/fim
        nomeResponsavel: form.nomeResponsavel.value.trim(),
        cartaoSus: form.cartaoSus.value.trim(),
        rg: form.rg.value.trim(),
        cpf: form.cpf.value.trim(),
        telefone: form.telefone.value.trim(),
        diaSemana: form.diaSemana.value,
        horario: form.horario.value,
        fonoaudiologo: {
          id: form.fonoaudiologo.value // ID do profissional selecionado
        }
      };

      // Envia os dados para a API
      const salvarResponse = await fetch("http://localhost:8080/api/consultas", {
        method: "POST", // Método HTTP
        headers: {
          "Content-Type": "application/json", // Define que está enviando JSON
        },
        body: JSON.stringify(data), // Converte o objeto para JSON
      });

      const responseData = await salvarResponse.json(); // Converte a resposta da API em JSON

      if (salvarResponse.ok) {
        mostrarModal("Agendamento salvo com sucesso!"); // Exibe sucesso no modal
        form.reset(); // Limpa os campos do formulário
      } else {
        const errorMsg = responseData.message || "Erro ao salvar agendamento"; // Usa mensagem vinda da API ou uma genérica
        mostrarModal(errorMsg); // Exibe erro no modal
      }
    } catch (error) {
      console.error("Erro no envio:", error); // Exibe erro no console
      mostrarModal("Falha na comunicação com o servidor"); // Exibe erro de comunicação no modal
    }
  });
});
