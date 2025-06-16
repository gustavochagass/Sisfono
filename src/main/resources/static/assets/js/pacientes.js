let authToken = localStorage.getItem("authToken");

async function loadPatients() {
    try {
        const response = await fetch("/api/pacientes", {
            headers: {
                Authorization: `Bearer ${authToken}`,
            },
        });

        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                alert("Sua sessão expirou ou você não tem permissão. Por favor, faça login novamente.");
                window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz
            } else {
                throw new Error("Erro ao carregar pacientes");
            }
        }

        const pacientes = await response.json();
        displayPatients(pacientes);
    } catch (error) {
        console.error("Erro:", error);
        if (!error.message.includes("sessão expirou")) {
            alert("Erro ao carregar pacientes. Por favor, tente novamente mais tarde.");
        }
    }
}

async function searchPatientByCpf(cpf) {
    try {
        const cpfLimpo = cpf.replace(/\D/g, "");

        const response = await fetch(`/api/pacientes/cpf/${cpfLimpo}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${authToken}`,
            },
        });

        if (!response.ok) {
            const contentType = response.headers.get("content-type");
            let errorMessage = "Erro desconhecido ao buscar paciente.";

            if (contentType && contentType.includes("application/json")) {
                const errorData = await response.json();
                errorMessage = errorData.message || "Paciente não encontrado";
            } else {
                errorMessage = await response.text();
            }
            throw new Error(errorMessage);
        }

        const paciente = await response.json();
        displayPatients([paciente]);
    } catch (error) {
        console.error("Erro:", error);
        alert(error.message);
    }
}

function displayPatients(pacientes) {
    const tbody = document.getElementById("patientsList");
    tbody.innerHTML = "";

    if (pacientes.length === 0) {
        const tr = document.createElement("tr");
        tr.innerHTML = '<td colspan="5" style="text-align: center;">Nenhum paciente encontrado</td>';
        tbody.appendChild(tr);
        return;
    }

    pacientes.forEach((paciente) => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${paciente.nomePaciente}</td>
            <td>${formatPhone(paciente.telefone)}</td>
            <td>${formatDay(paciente.diasSemana)}</td>
            <td>${formatTime(paciente.horario)}</td>
            <td>
                <button class="action-btn view-btn" data-id="${paciente.id}">Ver</button>
                <button class="action-btn delete-btn" data-id="${paciente.id}">Excluir</button>
            </td>
        `;

        tbody.appendChild(tr);
    });

    document.querySelectorAll(".view-btn").forEach((btn) => {
        btn.addEventListener("click", () => redirectToPatientDetails(btn.dataset.id));
    });

    document.querySelectorAll(".delete-btn").forEach((btn) => {
        btn.addEventListener("click", () => deletePatient(btn.dataset.id));
    });
}

function formatPhone(phone) {
    if (!phone) return "";
    return `(${phone.substring(0, 2)}) ${phone.substring(2, 7)}-${phone.substring(7)}`;
}

function formatDay(day) {
    const days = {
        MONDAY: "Segunda",
        TUESDAY: "Terça",
        WEDNESDAY: "Quarta",
        THURSDAY: "Quinta",
        FRIDAY: "Sexta",
        SATURDAY: "Sábado",
        SUNDAY: "Domingo",
    };
    return days[day] || day;
}

function formatTime(time) {
    if (!time) return "";
    return time.substring(0, 5);
}

function redirectToPatientDetails(pacienteId) {
    // CORREÇÃO AQUI: Redireciona para a URL mapeada pelo ViewController
    window.location.href = `/detalhes_paciente?id=${pacienteId}`;
}

async function deletePatient(pacienteId) {
    const confirmDelete = confirm("Tem certeza que deseja excluir este paciente? Esta ação é irreversível e excluirá também a ficha do paciente.");

    if (!confirmDelete) {
        return;
    }

    try {
        const response = await fetch(`/api/pacientes/${pacienteId}`, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${authToken}`,
            },
        });

        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                alert("Sua sessão expirou ou você não tem permissão para excluir. Por favor, faça login novamente.");
                window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz
            } else {
                throw new Error("Erro ao excluir paciente");
            }
        }

        alert("Paciente excluído com sucesso");
        loadPatients();
    } catch (error) {
        console.error("Erro:", error);
        alert("Erro ao excluir paciente: " + error.message);
    }
}

document.getElementById("searchBtn").addEventListener("click", () => {
    const cpf = document.getElementById("searchCpf").value.trim();
    if (cpf) {
        searchPatientByCpf(cpf);
    } else {
        loadPatients();
    }
});

document.addEventListener("DOMContentLoaded", () => {
    if (!authToken) {
        alert("Por favor, faça login para acessar esta página");
        window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz
        return;
    }

    loadPatients();
});