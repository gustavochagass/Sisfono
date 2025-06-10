// login.js

document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector(".login-form");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const usuarioInput = document.getElementById("user");
    const senhaInput = document.getElementById("password");
    const cpfOuEmail = usuarioInput.value.trim();
    const senha = senhaInput.value.trim();
    const isEmail = cpfOuEmail.includes("@");
    const usuarioFormatado = isEmail
      ? cpfOuEmail
      : cpfOuEmail.replace(/\D+/g, "");

    const body = isEmail
      ? { email: usuarioFormatado, senha }
      : { cpf: usuarioFormatado, senha };

    try {
      const response = await fetch(
        "http://localhost:8080/api/fonoaudiologos/login",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(body),
        }
      );

      if (response.ok) {
        // --- INÍCIO DA MODIFICAÇÃO PRINCIPAL ---
        const responseData = await response.json(); // A resposta agora contém 'fonoaudiologo' e 'token'

        const token = responseData.token; // Extrai o token JWT
        const fonoaudiologoData = responseData.fonoaudiologo; // Extrai os dados do fonoaudiólogo

        alert(`Bem-vindo(a), ${fonoaudiologoData.nome}!`);

        // Armazena o token e os dados do fonoaudiólogo no localStorage
        localStorage.setItem('authToken', token); // Salva o token aqui
        localStorage.setItem('userData', JSON.stringify(fonoaudiologoData)); // Salva os dados do fono para uso futuro (ex: ID)
        // --- FIM DA MODIFICAÇÃO PRINCIPAL ---

        window.location.href = "tela_fonoPrincipal.html"; // Redireciona para a página principal após o login
      } else {
        // Trata diferentes tipos de erros da resposta do backend
        const errorData = await response.json();
        alert(errorData.message || "Usuário ou senha inválidos.");
      }
    } catch (error) {
      console.error("Erro ao realizar login:", error);
      alert("Erro de conexão com o servidor. Verifique se o backend está rodando.");
    }
  });
});