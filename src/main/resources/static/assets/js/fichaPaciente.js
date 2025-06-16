document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('fichaPacienteForm');
    const message = document.getElementById('message');
    const cpfInput = document.getElementById('cpf'); // Referência ao input do CPF

    const showMessage = (text, type) => {
        message.textContent = text;
        message.style.display = 'block';
        message.classList.remove('success', 'error', 'info');

        const styles = {
            success: ['#d4edda', '#155724', '#c3e6cb'],
            error: ['#f8d7da', '#721c24', '#f5c6cb'],
            info: ['#e2e3e5', '#383d41', '#d6d8db']
        };

        const [bg, color, border] = styles[type] || styles.info;
        Object.assign(message.style, {
            backgroundColor: bg,
            color: color,
            borderColor: border,
            borderWidth: '1px',
            borderStyle: 'solid',
            padding: '10px',
            marginBottom: '10px',
            borderRadius: '5px'
        });
        message.classList.add(type);
    };

    // --- MÁSCARA DE CPF ---
    cpfInput.addEventListener('input', (e) => {
        let value = e.target.value.replace(/\D/g, ''); // Remove tudo que não é dígito
        let formattedValue = '';

        if (value.length > 0) {
            formattedValue = value.substring(0, 3);
            if (value.length > 3) {
                formattedValue += '.' + value.substring(3, 6);
            }
            if (value.length > 6) {
                formattedValue += '.' + value.substring(6, 9);
            }
            if (value.length > 9) {
                formattedValue += '-' + value.substring(9, 11);
            }
        }
        e.target.value = formattedValue;
    });
    // --- FIM DA MÁSCARA DE CPF ---

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        showMessage('Processando...', 'info');

        const token = localStorage.getItem('authToken');
        if (!token) {
            showMessage('Token não encontrado. Faça login novamente.', 'error');
            window.location.href = "/"; // CORRIGIDO: Redireciona para a URL raiz
            return;
        }

        // Obtém o CPF e remove todos os caracteres não numéricos ANTES de usar
        const cpf = cpfInput.value.replace(/\D/g, '');

        // Validação adicional: garante que o CPF tem 11 dígitos após a limpeza
        if (!cpf || cpf.length !== 11) {
            showMessage('Por favor, insira um CPF válido com 11 dígitos.', 'error');
            return;
        }

        try {
            // 1. Buscar paciente por CPF para obter o ID
            // CORREÇÃO AQUI: Mudando de localhost para caminho relativo
            const resPaciente = await fetch(`/api/pacientes/cpf/${cpf}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });

            if (resPaciente.status === 404) {
                showMessage('Paciente não cadastrado com este CPF.', 'error');
                return;
            }
            if (!resPaciente.ok) {
                const errorText = await resPaciente.text();
                throw new Error(`Erro ao buscar paciente: ${errorText}`);
            }

            const paciente = await resPaciente.json();

            let existingFicha = null;
            let method = 'POST';
            // CORREÇÃO AQUI: Mudando de localhost para caminho relativo
            let url = `/api/fichas-paciente`;

            // 2. Verificar ficha existente para o paciente
            // CORREÇÃO AQUI: Mudando de localhost para caminho relativo
            const resFicha = await fetch(`/api/fichas-paciente/paciente/${paciente.id}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });

            if (resFicha.ok) {
                existingFicha = await resFicha.json();
                if (confirm('Já existe uma ficha para este paciente. Deseja sobrescrever?')) {
                    method = 'PUT';
                    // CORREÇÃO AQUI: Mudando de localhost para caminho relativo
                    url = `/api/fichas-paciente/${existingFicha.id}`;
                } else {
                    showMessage('Operação cancelada.', 'info');
                    return;
                }
            } else if (resFicha.status !== 404) {
                // Se o status não for 404 (ficha não encontrada), é um erro real
                const errorText = await resFicha.text();
                throw new Error(`Erro ao verificar ficha existente: ${errorText}`);
            }

            // 3. Montar dados da ficha com os IDs corretos do HTML
            const data = {
                pacienteId: paciente.id,
                nomePaciente: document.getElementById('nome').value,
                idade: parseInt(document.getElementById('idade').value),
                cid: document.getElementById('cid').value,
                diasSemana: document.getElementById('dias_semana').value,
                cpfPaciente: cpf, // O CPF já está limpo aqui
                horarioAtendimento: document.getElementById('horario').value,
                turnoAtendimento: document.getElementById('turno').value,
                dataNascimento: document.getElementById('data_nascimento').value,
                relatorioConteudo: document.getElementById('relatorio').value,
                exercicioParaCasa: document.getElementById('exercicio').value
            };

            // 4. Enviar ficha (POST para criar, PUT para atualizar)
            const resSave = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(data)
            });

            if (resSave.ok) {
                showMessage('Ficha salva com sucesso! Redirecionando...', 'success');
                setTimeout(() => {
                    // CORREÇÃO AQUI: Redirecionando para a URL mapeada pelo ViewController
                    window.location.href = `/detalhes_paciente?id=${paciente.id}`;
                }, 1500);
            } else {
                const errorResponse = await resSave.json().catch(() => null);
                let msg = 'Erro ao salvar ficha.';
                if (errorResponse) {
                    msg = errorResponse.message || (errorResponse.errors || []).map(e => e.defaultMessage || e.message).join('; ');
                } else {
                    const errorText = await resSave.text();
                    msg = `Erro (${resSave.status}): ${errorText.substring(0, 200)}...`;
                }
                showMessage(msg, 'error');
            }
        } catch (err) {
            console.error('Erro inesperado:', err);
            showMessage(`Erro inesperado: ${err.message}`, 'error');
        }
    });
});