document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector(".form-cadastro");

  const showAlert = (msg) => alert(msg);

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const cpf = form.cpf.value.replace(/\D+/g, "");

    const data = {
      nome: form.nome.value,
      cpf,
      crf: form.crf.value,
      email: form.email.value,
      telefone: form.telefone.value,
      senha: form.senha.value,
    };

    try {
      // CORREÇÃO AQUI: Mudando de localhost para caminho relativo
      const res = await fetch("/api/fonoaudiologos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(data),
      });

      const resBody = await res.json();

      if (res.ok) {
        showAlert("Cadastro realizado com sucesso! Agora faça login.");
        // CORREÇÃO AQUI: Redirecionando para a URL mapeada pelo ViewController
        window.location.href = "/"; // Ou "/login", dependendo da sua preferência para a página de login
      } else if (
        res.status === 409 ||
        resBody.message?.includes("CPF já cadastrado")
      ) {
        showAlert("CPF já cadastrado.");
      } else {
        showAlert(
          `Erro ao cadastrar: ${resBody.message || JSON.stringify(resBody)}`
        );
      }
    } catch (err) {
      console.error("Erro na requisição:", err);
      showAlert("Erro na conexão com o servidor.");
    }
  });
});