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






document.addEventListener("DOMContentLoaded", async function () {
  // Recupera dados do usuário
  const userData = JSON.parse(localStorage.getItem("userData"));

  // Se não tiver dados, redireciona para login
  if (!userData) {
    window.location.href = "tela_login.html";
    return;
  }

  // Atualiza a interface
  const greetingElement = document.getElementById("user-greeting");
  greetingElement.textContent = `Fonoaudióloga(o) ${userData.nome}`;

  // Atualiza com dados frescos do servidor (opcional)
  try {
    const response = await fetch("/api/fonoaudiologos/me");
    if (response.ok) {
      const freshData = await response.json();
      greetingElement.textContent = `Fonoaudióloga(o) ${freshData.nome}`;
      localStorage.setItem("userData", JSON.stringify(freshData));
    }
  } catch (error) {
    console.error("Erro ao atualizar dados do usuário:", error);
  }

});






function logout() {
    // Limpa dados de autenticação
    localStorage.removeItem('userData');
    
    // Chamada opcional ao backend para invalidar sessão
    fetch('/api/fonoaudiologos/logout', { method: 'POST' });
    
    // Redireciona para login
    window.location.href = 'tela_login.html';
}