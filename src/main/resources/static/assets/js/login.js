// login.js

// Função auxiliar para setar cookie (removido HttpOnly, adicionado para debug/garantia)
function setAuthCookie(token, fonoaudiologoData) {
  const expirationDate = new Date();
  // Defina a expiração do cookie para ser a mesma do token ou um tempo razoável.
  // Exemplo: Expira em 1 hora (3600 segundos)
  expirationDate.setTime(expirationDate.getTime() + (3600 * 1000)); 

  // CORREÇÃO AQUI: Removido 'HttpOnly' (não pode ser setado por JS)
  // E a sintaxe de template literal que estava errada já está corrigida
  document.cookie = `authToken=${token}; Expires=${expirationDate.toUTCString()}; Path=/; Secure; SameSite=Lax`;
  
  // ADICIONADO AQUI: Salva o token também no localStorage para que outros scripts possam lê-lo
  localStorage.setItem("authToken", token); // <<<<<<< ESSA É A LINHA QUE FALTAVA

  localStorage.setItem("userData", JSON.stringify(fonoaudiologoData));
}


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
        "/api/fonoaudiologos/login",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(body),
        }
      );

      if (response.ok) {
        const responseData = await response.json();
        const token = responseData.token;
        const fonoaudiologoData = responseData.fonoaudiologo;

        alert(`Bem-vindo(a), ${fonoaudiologoData.nome}!`);

        // Salva o token no cookie e NO localStorage
        setAuthCookie(token, fonoaudiologoData);
        
        // Adicionado: Atraso antes do redirecionamento
        setTimeout(() => {
          window.location.href = "/fonoPrincipal"; 
        }, 100); // 100 milissegundos de atraso

      } else {
        const contentType = response.headers.get("content-type");
        let errorMessage = "Erro desconhecido durante o login";

        try {
          if (contentType && contentType.includes("application/json")) {
            const errorData = await response.json();
            errorMessage = errorData.message || "Credenciais inválidas";
          } else {
            errorMessage = await response.text();
          }
        } catch (textError) {
          errorMessage = "Não foi possível ler a mensagem de erro";
        }

        alert(errorMessage);
      }
    } catch (error) {
      console.error("Erro ao realizar login:", error);
      alert(
        "Erro de conexão com o servidor. Verifique se o backend está rodando."
      );
    }
  });
});