// cadastro.js (Sua versão atual, sem alterações necessárias para este problema)
document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector(".form-cadastro");

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const cpfFormatado = form.cpf.value.replace(/\D+/g, "");

    const dados = {
      nome: form.nome.value,
      cpf: cpfFormatado,
      crf: form.crf.value,
      email: form.email.value,
      telefone: form.telefone.value,
      senha: form.senha.value,
    };

    try {
      const response = await fetch("http://localhost:8080/api/fonoaudiologos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // Pode ser removido se não houver cookies/sessão envolvidos
        body: JSON.stringify(dados),
      });

      if (response.ok) {
        alert("Cadastro realizado com sucesso! Agora faça login.");
        window.location.href = "tela_login.html"; // Redireciona para a página de login
      } else {
        // Melhor tratamento de erro para exibir mensagens do backend
        const errorData = await response.json();
        alert(`Erro ao cadastrar: ${errorData.message || JSON.stringify(errorData)}`);
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
      alert("Erro na conexão com o servidor.");
    }
  });
});