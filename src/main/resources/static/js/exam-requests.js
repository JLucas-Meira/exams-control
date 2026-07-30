const API_BASE_URL = '';

const alertContainer = document.getElementById('alert-container');
const loadingEl = document.getElementById('requests-loading');
const emptyEl = document.getElementById('requests-empty');
const tableWrapperEl = document.getElementById('requests-table-wrapper');
const tableBodyEl = document.getElementById('requests-table-body');

document.addEventListener('DOMContentLoaded', init);

function init() {
    console.log('Exam Requests module initialized.');
    // In Task 1, we only create the structure without integration.
    // Future integration will call loadRequests() here.
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
