const API_BASE_URL = '';

const alertContainer = document.getElementById('alert-container');
const loadingEl = document.getElementById('dashboard-loading');
const emptyEl = document.getElementById('dashboard-empty');
const tableWrapperEl = document.getElementById('dashboard-table-wrapper');
const tableBodyEl = document.getElementById('dashboard-table-body');

document.addEventListener('DOMContentLoaded', loadDashboard);

async function loadDashboard() {
    showState('loading');
    clearAlerts();

    try {
        const response = await fetch(`${API_BASE_URL}/procedures/dashboard`);

        if (!response.ok) {
            const errorMessage = await extractErrorMessage(response);
            showAlert('danger', errorMessage);
            showState('empty');
            return;
        }

        const procedures = await response.json();

        if (!procedures.length) {
            showState('empty');
            return;
        }

        renderTable(procedures);
        showState('table');
    } catch (error) {
        showAlert(
            'danger',
            'Não foi possível conectar ao servidor. Verifique se a API está rodando em <code>http://localhost:8080</code>.'
        );
        showState('empty');
    }
}

function renderTable(procedures) {
    tableBodyEl.innerHTML = procedures.map(renderRow).join('');
}

function renderRow(procedure) {
    const hasAvailableQuota = procedure.availableQuota > 0;
    const hasQueue = procedure.patientsInQueue > 0;

    const statusBadge = hasAvailableQuota
        ? '<span class="badge bg-success-subtle text-success-emphasis">Vagas disponíveis</span>'
        : '<span class="badge bg-danger-subtle text-danger-emphasis">Sem vagas</span>';

    const availableClass = hasAvailableQuota ? 'text-success fw-semibold' : 'text-danger fw-semibold';
    const queueClass = hasQueue ? 'text-warning-emphasis fw-semibold' : 'text-muted';

    return `
        <tr>
            <td>${escapeHtml(procedure.procedureName)}</td>
            <td class="text-center">${procedure.totalQuota}</td>
            <td class="text-center">${procedure.scheduledPatients}</td>
            <td class="text-center ${availableClass}">${procedure.availableQuota}</td>
            <td class="text-center ${queueClass}">${procedure.patientsInQueue}</td>
            <td class="text-center">${statusBadge}</td>
        </tr>
    `;
}

function showState(state) {
    loadingEl.classList.toggle('d-none', state !== 'loading');
    emptyEl.classList.toggle('d-none', state !== 'empty');
    tableWrapperEl.classList.toggle('d-none', state !== 'table');
}

function showAlert(type, message) {
    alertContainer.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show mb-4" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fechar"></button>
        </div>
    `;
}

function clearAlerts() {
    alertContainer.innerHTML = '';
}

async function extractErrorMessage(response) {
    try {
        const body = await response.json();
        if (body.message) {
            return escapeHtml(body.message);
        }
    } catch (error) {
        // resposta sem corpo JSON
    }

    return `Erro ao carregar o dashboard (HTTP ${response.status}).`;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
