// Variável para armazenar o token JWT
let authToken = localStorage.getItem("authToken");

document.addEventListener("DOMContentLoaded", () => {
    // Verifica se o usuário está logado
    if (!authToken) {
        alert("Por favor, faça login para acessar esta página");
        window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz mapeada pelo ViewController
        return;
    }

    // Pega o ID do paciente da URL
    const urlParams = new URLSearchParams(window.location.search);
    const pacienteId = urlParams.get('id');

    if (pacienteId) {
        loadPatientAndFichaDetails(pacienteId);
    } else {
        const patientInfo = document.getElementById("patientInfo");
        patientInfo.innerHTML = `<p class="error-message">ID do paciente não encontrado na URL.</p>`;
    }
});

async function loadPatientAndFichaDetails(pacienteId) {
    const patientInfo = document.getElementById("patientInfo");
    const fichaDetailsCard = document.getElementById("fichaDetailsCard");
    const fichaInfo = document.getElementById("fichaInfo");

    // Mensagem enquanto carrega os dados
    patientInfo.innerHTML = `<p class="loading-message">Carregando detalhes do paciente...</p>`;
    fichaDetailsCard.style.display = 'none'; // Esconde ficha inicialmente

    try {
        // 1. Carregar detalhes do paciente
        const patientResponse = await fetch(`/api/pacientes/${pacienteId}`, {
            headers: {
                Authorization: `Bearer ${authToken}`,
            },
        });

        if (!patientResponse.ok) {
            if (patientResponse.status === 401 || patientResponse.status === 403) {
                alert("Sua sessão expirou ou você não tem permissão. Por favor, faça login novamente.");
                window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz mapeada pelo ViewController
                return;
            }
            throw new Error("Erro ao carregar detalhes do paciente.");
        }

        const paciente = await patientResponse.json();
        displayPatientDetails(paciente);

        // 2. Carregar ficha médica associada ao paciente pelo pacienteId
        const fichaResponse = await fetch(`/api/fichas-paciente/paciente/${paciente.id}`, {
            headers: {
                Authorization: `Bearer ${authToken}`,
            },
        });

        if (fichaResponse.ok) {
            const ficha = await fichaResponse.json();
            displayFichaDetails(ficha);
            fichaDetailsCard.style.display = 'block'; // Mostrar ficha
        } else if (fichaResponse.status === 404) {
            // Ficha não encontrada
            fichaInfo.innerHTML = `<p class="no-ficha-message">Nenhuma ficha médica cadastrada para este paciente.</p>`;
            fichaDetailsCard.style.display = 'block'; // Mostrar seção com mensagem
        } else {
            // Outros erros ao carregar ficha
            const errorText = await fichaResponse.text();
            console.error("Erro ao carregar ficha médica:", errorText);
            fichaInfo.innerHTML = `<p class="error-message">Não foi possível carregar a ficha médica: ${fichaResponse.status} - ${errorText.substring(0, 100)}...</p>`;
            fichaDetailsCard.style.display = 'block';
        }
    } catch (error) {
        console.error("Erro geral ao carregar detalhes do paciente e ficha:", error);
        patientInfo.innerHTML = `<p class="error-message">Não foi possível carregar os detalhes do paciente: ${error.message}</p>`;
        fichaDetailsCard.style.display = 'none';
    }
}

function displayPatientDetails(paciente) {
    const patientInfo = document.getElementById("patientInfo");
    patientInfo.innerHTML = `
        <p><strong>Nome:</strong> ${paciente.nomePaciente}</p>
        <p><strong>Responsável:</strong> ${paciente.nomeResponsavel || 'Não informado'}</p>
        <p><strong>CPF:</strong> ${formatCpf(paciente.cpf)}</p>
        <p><strong>RG:</strong> ${paciente.rg || 'Não informado'}</p>
        <p><strong>Cartão SUS:</strong> ${paciente.cartaoSus || 'Não informado'}</p>
        <p><strong>Telefone:</strong> ${formatPhone(paciente.telefone)}</p>
        <p><strong>Dia da Semana:</strong> ${formatDay(paciente.diasSemana)}</p>
        <p><strong>Horário:</strong> ${formatTime(paciente.horario)}</p>
        <p><strong>Período:</strong> ${paciente.dataInicio} até ${paciente.dataFim}</p>
    `;
}

function displayFichaDetails(ficha) {
    const fichaInfo = document.getElementById("fichaInfo");
    fichaInfo.innerHTML = `
        <p><strong>Nome (Ficha):</strong> ${ficha.nomePaciente || 'Não informado'}</p>
        <p><strong>Idade:</strong> ${ficha.idade || 'Não informado'}</p>
        <p><strong>Data de Nascimento:</strong> ${formatDate(ficha.dataNascimento) || 'Não informado'}</p>
        <p><strong>CID:</strong> ${ficha.cid || 'Não informado'}</p>
        <p><strong>Horário de Atendimento:</strong> ${ficha.horarioAtendimento || 'Não informado'}</p>
        <p><strong>Turno de Atendimento:</strong> ${ficha.turnoAtendimento || 'Não informado'}</p>
        <p><strong>Relatório:</strong> ${ficha.relatorioConteudo || 'Não informado'}</p>
        <p><strong>Exercício para Casa:</strong> ${ficha.exercicioParaCasa || 'Não informado'}</p>
    `;
}

// Funções de formatação

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