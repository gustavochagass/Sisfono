document.addEventListener("DOMContentLoaded", async function () {
  // Recupera dados do usuário
  const userData = JSON.parse(localStorage.getItem("userData"));

  // Se não tiver dados, redireciona para login
  if (!userData) {
    window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz
    return;
  }

  // Atualiza a interface
  const greetingElement = document.getElementById("user-greeting");
  greetingElement.textContent = `Fonoaudióloga(o) ${userData.nome}`;

  // Adiciona o token de autenticação ao cabeçalho da requisição
  const authToken = localStorage.getItem("authToken");

  // Atualiza com dados frescos do servidor (opcional)
  try {
    // CORREÇÃO AQUI: Mudando de localhost para caminho relativo
    const response = await fetch("/api/fonoaudiologos/me", {
      headers: {
        Authorization: `Bearer ${authToken}`, // Inclui o token no cabeçalho
      },
    });
    if (response.ok) {
      const freshData = await response.json();
      greetingElement.textContent = `Fonoaudióloga(o) ${freshData.nome}`;
      localStorage.setItem("userData", JSON.stringify(freshData));
    } else {
      // Tratar erros, por exemplo, se o token for inválido ou expirado
      console.error(
        "Falha ao buscar dados atualizados do usuário:",
        response.status,
        response.statusText
      );
      // Opcional: redirecionar para login se a autenticação falhar
      window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz
    }
  } catch (error) {
    console.error("Erro ao atualizar dados do usuário:", error);
  }
});

// Função de submenu do cadastro
function toggleSubmenu() {
  const submenu = document.querySelector("#cadastro-item .submenu");
  submenu.style.display = submenu.style.display === "block" ? "none" : "block";
}

// Inicialização do Calendário Mini
document.addEventListener("DOMContentLoaded", function () {
  const calendarEl = document.getElementById("calendario-mini");
  const calendar = new FullCalendar.Calendar(calendarEl, {
    locale: "pt-br",
    initialView: "dayGridMonth",
    headerToolbar: {
      left: "",
      center: "title",
      right: "",
    },
    events: [
      { title: "João da Silva", start: "2025-05-20" },
      { title: "Maria Oliveira", start: "2025-05-22" },
    ],
  });
  calendar.render();
});

function logout() {
  // Limpa dados de autenticação
  localStorage.removeItem("userData");
  localStorage.removeItem("authToken"); // Remova também o token ao fazer logout

  // Chamada opcional ao backend para invalidar sessão
  // Se o logout no backend exigir autenticação, inclua o token aqui
  // CORREÇÃO AQUI: Mudando de localhost para caminho relativo
  fetch("/api/fonoaudiologos/logout", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("authToken")}`, // Se o logout exigir o token
    },
  })
    .then((response) => {
      // Tratar a resposta do logout do backend, se necessário
      console.log("Logout no backend realizado:", response.status);
    })
    .catch((error) => {
      console.error("Erro ao fazer logout no backend:", error);
    });

  // Redireciona para login
  window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz
}