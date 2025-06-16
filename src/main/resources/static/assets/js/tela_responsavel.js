document.addEventListener("DOMContentLoaded", () => {
    const searchFichaBtn = document.getElementById("searchFichaBtn");
    const cpfInput = document.getElementById("cpfInput");
    const fichaDisplayArea = document.getElementById("fichaDisplayArea");
    const fichaContent = document.getElementById("fichaContent");
    const printFichaBtn = document.getElementById("printFichaBtn");

    searchFichaBtn.addEventListener("click", async () => {
        const cpf = cpfInput.value.trim();
        if (cpf) {
            await searchAndDisplayFicha(cpf, localStorage.getItem("authToken"));
        } else {
            fichaDisplayArea.style.display = 'none';
            alert("Por favor, digite o CPF do paciente para pesquisar.");
        }
    });

    printFichaBtn.addEventListener("click", () => {
        window.print();
    });
});

async function searchAndDisplayFicha(cpf, authToken) {
    const fichaDisplayArea = document.getElementById("fichaDisplayArea");
    const fichaContent = document.getElementById("fichaContent");
    fichaContent.innerHTML = `<p class="loading-message">Buscando ficha do paciente...</p>`;
    fichaDisplayArea.style.display = 'block';

    try {
        const cpfLimpo = cpf.replace(/\D/g, "");
        const headers = {
            "Content-Type": "application/json",
        };

        if (authToken) {
            headers["Authorization"] = `Bearer ${authToken}`;
        }

        const response = await fetch(`/api/fichas-paciente/cpf/${cpfLimpo}`, {
            method: "GET",
            headers,
        });

        if (!response.ok) {
            if (response.status === 404) {
                fichaContent.innerHTML = `<p class="no-ficha-message">Nenhuma ficha encontrada para este CPF.</p>`;
            } else if (response.status === 401 || response.status === 403) {
                alert("Sessão expirada ou acesso negado. Faça login novamente.");
                window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz
            } else {
                const errorText = await response.text();
                fichaContent.innerHTML = `<p class="error-message">Erro: ${response.status} - ${errorText.substring(0, 100)}...</p>`;
                console.error("Erro ao carregar ficha:", errorText);
            }
            return;
        }

        const ficha = await response.json();
        displayFichaDetails(ficha);
    } catch (error) {
        console.error("Erro ao buscar ficha:", error);
        fichaContent.innerHTML = `<p class="error-message">Erro: ${error.message}</p>`;
    }
}

function displayFichaDetails(ficha) {
    const fichaContent = document.getElementById("fichaContent");
    fichaContent.innerHTML = `
        <div class="ficha-item"><p><strong>Nome do Paciente:</strong> ${ficha.nomePaciente || 'Não informado'}</p></div>
        <div class="ficha-item"><p><strong>CPF do Paciente:</strong> ${formatCpf(ficha.cpfPaciente) || 'Não informado'}</p></div>
        <div class="ficha-item"><p><strong>CID:</strong> ${ficha.cid || 'Não informado'}</p></div>
        <div class="ficha-item"><p><strong>Dias da Semana:</strong> ${ficha.diasSemana ? formatDay(ficha.diasSemana) : 'Não informado'}</p></div>
        <div class="ficha-item"><p><strong>Horário de Atendimento:</strong> ${ficha.horarioAtendimento ? formatTime(ficha.horarioAtendimento) : 'Não informado'}</p></div>
        <div class="ficha-item"><p><strong>Relatório:</strong> ${ficha.relatorioConteudo || 'Não informado'}</p></div>
        <div class="ficha-item"><p><strong>Exercício para Casa:</strong> ${ficha.exercicioParaCasa || 'Não informado'}</p></div>
    `;
}

function formatPhone(phone) {
    if (!phone) return "";
    if (phone.length === 11) {
        return `(${phone.substring(0, 2)}) ${phone.substring(2, 7)}-${phone.substring(7)}`;
    } else if (phone.length === 10) {
        return `(${phone.substring(0, 2)}) ${phone.substring(2, 6)}-${phone.substring(6)}`;
    }
    return phone;
}

function formatDay(day) {
    const days = {
        MONDAY: "Segunda-feira",
        TUESDAY: "Terça-feira",
        WEDNESDAY: "Quarta-feira",
        THURSDAY: "Quinta-feira",
        FRIDAY: "Sexta-feira",
        SATURDAY: "Sábado",
        SUNDAY: "Domingo",
    };
    return days[day] || day;
}

function formatTime(time) {
    if (!time) return "";
    return time.substring(0, 5);
}

function formatCpf(cpf) {
    if (!cpf) return "";
    if (cpf.length === 11) {
        return `${cpf.substring(0, 3)}.${cpf.substring(3, 6)}.${cpf.substring(6, 9)}-${cpf.substring(9)}`;
    }
    return cpf;
}

function formatDate(dateStr) {
    if (!dateStr) return "";
    const date = new Date(dateStr + 'T00:00:00');
    if (isNaN(date.getTime())) {
        const [year, month, day] = dateStr.split('-');
        return `${day}/${month}/${year}`;
    }
    return date.toLocaleDateString('pt-BR');
}