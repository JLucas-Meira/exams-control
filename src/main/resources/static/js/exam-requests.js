const API_BASE_URL = '';

const alertContainer = document.getElementById('alert-container');
const loadingEl = document.getElementById('requests-loading');
const emptyEl = document.getElementById('requests-empty');
const tableWrapperEl = document.getElementById('requests-table-wrapper');
const tableBodyEl = document.getElementById('requests-table-body');

const requestModal = new bootstrap.Modal(document.getElementById('requestModal'));
const requestForm = document.getElementById('request-form');
const procedureSelect = document.getElementById('procedureId');
const btnSaveRequest = document.getElementById('btn-save-request');
const hiddenPatientId = document.getElementById('patientId');
const inputSusCard = document.getElementById('susCardSearch');
const btnSearchPatient = document.getElementById('btn-search-patient');
const patientResult = document.getElementById('patient-result');
const patientFeedback = document.getElementById('patient-feedback');

document.addEventListener('DOMContentLoaded', init);

function init() {
    loadRequests();
    loadFormOptions();

    requestForm.addEventListener('submit', saveRequest);
    btnSearchPatient.addEventListener('click', searchPatientBySusCard); // <-- Nova linha
}

async function loadFormOptions() {
    try {
        // Front faz fetch APENAS nos procedimentos. Os pacientes vêm por busca ativa.
        const proceduresRes = await fetch(`${API_BASE_URL}/procedures`);

        if (proceduresRes.ok) {
            const procedures = await proceduresRes.json();
            procedureSelect.innerHTML += procedures.map(p => `<option value="${p.id}">${escapeHtml(p.name)}</option>`).join('');
        }
    } catch (error) {
        console.error('Error loading form options:', error);
    }
}

async function saveRequest(event) {
    event.preventDefault();

    const formData = {
        patientId: hiddenPatientId.value,
        procedureId: procedureSelect.value,
        priority: document.getElementById('priority').value
    };

    setLoading(true);

    try {
        const response = await fetch(`${API_BASE_URL}/exam-requests`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            requestModal.hide();
            requestForm.reset();
            inputSusCard.classList.remove('is-valid');
            patientResult.classList.add('d-none');
            showAlert('success', 'Solicitação cadastrada com sucesso!');
            loadRequests();
        } else {
            const errorData = await response.json().catch(() => ({}));
            showAlert('danger', errorData.message || 'Erro ao salvar solicitação.');
        }
    } catch (error) {
        showAlert('danger', 'Erro de conexão com o servidor.');
    } finally {
        setLoading(false);
    }
}

function setLoading(isLoading) {
    btnSaveRequest.disabled = isLoading;
    const spinner = btnSaveRequest.querySelector('.spinner-border');
    if (spinner) spinner.classList.toggle('d-none', !isLoading);
}

async function loadRequests() {
    showState('loading');
    clearAlerts();

    try {
        const response = await fetch(`${API_BASE_URL}/exam-requests`);

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            const message = errorData.message || `Erro ao carregar solicitações (HTTP ${response.status})`;
            showAlert('danger', message);
            showState('empty');
            return;
        }

        const requests = await response.json();

        if (!requests || requests.length === 0) {
            showState('empty');
            return;
        }

        renderTable(requests);
        showState('table');
    } catch (error) {
        console.error('Error loading requests:', error);
        showAlert(
            'danger',
            'Não foi possível conectar ao servidor. Verifique se a API está rodando em <code>http://localhost:8080</code>.'
        );
        showState('empty');
    }
}

function renderTable(requests) {
    tableBodyEl.innerHTML = requests.map(renderRow).join('');
}

function renderRow(request) {
    return `
        <tr>
            <td class="fw-medium">${escapeHtml(request.patientName)}</td>
            <td>${escapeHtml(request.procedureName)}</td>
            <td>${getPriorityBadge(request.priority)}</td>
            <td>${getStatusBadge(request.status)}</td>
            <td>${formatDate(request.scheduledDate)}</td>
            <td class="text-end">
                <div class="btn-group btn-group-sm">
                    <button type="button" class="btn btn-outline-primary" title="Ver detalhes" onclick="viewDetails(${request.id})">
                        <i class="bi bi-eye"></i>
                    </button>
                </div>
            </td>
        </tr>
    `;
}

function getStatusBadge(status) {
    const badges = {
        'PENDENTE': '<span class="badge bg-warning-subtle text-warning-emphasis">Pendente</span>',
        'AGENDADO': '<span class="badge bg-info-subtle text-info-emphasis">Agendado</span>',
        'REALIZADO': '<span class="badge bg-success-subtle text-success-emphasis">Realizado</span>',
        'CANCELADO': '<span class="badge bg-danger-subtle text-danger-emphasis">Cancelado</span>'
    };
    return badges[status] || `<span class="badge bg-secondary-subtle text-secondary-emphasis">${status}</span>`;
}

function getPriorityBadge(priority) {
    const badges = {
        'URGENT': '<span class="badge bg-danger">Urgente</span>',
        'PRIORITY': '<span class="badge bg-warning text-dark">Prioridade</span>',
        'ELECTIVE': '<span class="badge bg-info text-dark">Eletiva</span>'
    };
    return badges[priority] || `<span class="badge bg-secondary">${priority}</span>`;
}

function formatDate(dateString) {
    if (!dateString) return '<span class="text-muted">Não agendada</span>';

    try {
        const date = new Date(dateString);
        return new Intl.DateTimeFormat('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        }).format(date);
    } catch (e) {
        return dateString;
    }
}

function viewDetails(id) {
    // Placeholder para funcionalidade futura
    console.log('View details for request:', id);
}


/**
 * Mudar o estado visual da página
 * @param {'loading' | 'empty' | 'table'} state 
 */
function showState(state) {
    if (loadingEl) loadingEl.classList.toggle('d-none', state !== 'loading');
    if (emptyEl) emptyEl.classList.toggle('d-none', state !== 'empty');
    if (tableWrapperEl) tableWrapperEl.classList.toggle('d-none', state !== 'table');
}

function showAlert(type, message) {
    if (!alertContainer) return;
    alertContainer.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show mb-4" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fechar"></button>
        </div>
    `;
}

function clearAlerts() {
    if (alertContainer) alertContainer.innerHTML = '';
}

function escapeHtml(text) {
    if (text === null || text === undefined) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

async function searchPatientBySusCard() {
    const susCard = inputSusCard.value.trim();

    if (susCard.length === 0) {
        inputSusCard.classList.add('is-invalid');
        patientFeedback.innerText = "Digite o número do cartão.";
        return;
    }

    try {
        // The Loading State
        btnSearchPatient.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>';
        btnSearchPatient.disabled = true;

        const response = await fetch(`${API_BASE_URL}/patients/search?susCard=${susCard}`);

        if (response.ok) {
            const patient = await response.json();

            inputSusCard.classList.remove('is-invalid');
            inputSusCard.classList.add('is-valid');
            hiddenPatientId.value = patient.id; // The Magic: ID no banco invisível

            patientResult.textContent = `Paciente localizado: ${patient.name}`;
            patientResult.classList.remove('d-none');
        } else {
            // The 404 State
            inputSusCard.classList.add('is-invalid');
            inputSusCard.classList.remove('is-valid');
            hiddenPatientId.value = '';
            patientResult.classList.add('d-none');
            patientFeedback.innerText = "Paciente não encontrado no sistema.";
        }
    } catch (error) {
        console.error("Erro na busca:", error);
    } finally {
        btnSearchPatient.innerHTML = '<i class="bi bi-search"></i> Buscar';
        btnSearchPatient.disabled = false;
    }
}
