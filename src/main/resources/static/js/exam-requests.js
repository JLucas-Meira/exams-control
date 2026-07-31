const API_BASE_URL = '';

const alertContainer = document.getElementById('alert-container');
const loadingEl = document.getElementById('requests-loading');
const emptyEl = document.getElementById('requests-empty');
const tableWrapperEl = document.getElementById('requests-table-wrapper');
const tableBodyEl = document.getElementById('requests-table-body');

document.addEventListener('DOMContentLoaded', init);

function init() {
    loadRequests();
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
        'ALTA': '<span class="badge bg-danger">Alta</span>',
        'MEDIA': '<span class="badge bg-warning text-dark">Média</span>',
        'BAIXA': '<span class="badge bg-info text-dark">Baixa</span>'
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
