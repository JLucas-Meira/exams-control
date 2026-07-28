const API_BASE_URL = '';

const form = document.getElementById('procedure-form');
const alertContainer = document.getElementById('alert-container');
const submitBtn = document.getElementById('submit-btn');
const submitLabel = submitBtn.querySelector('.submit-label');
const submitSpinner = submitBtn.querySelector('.submit-spinner');

document.addEventListener('DOMContentLoaded', () => {
    form.addEventListener('submit', handleSubmit);
    form.addEventListener('reset', () => {
        clearAlerts();
        clearValidationState();
    });
});

async function handleSubmit(event) {
    event.preventDefault();
    clearAlerts();

    const payload = buildPayload();
    if (!validatePayload(payload)) {
        return;
    }

    setLoading(true);

    try {
        const response = await fetch(`${API_BASE_URL}/procedures`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const procedure = await response.json();
            showAlert(
                'success',
                `Procedimento <strong>${escapeHtml(procedure.name)}</strong> cadastrado com sucesso.`
            );
            form.reset();
            clearValidationState();
            return;
        }

        const errorMessage = await extractErrorMessage(response);
        showAlert('danger', errorMessage);
    } catch (error) {
        showAlert(
            'danger',
            'Não foi possível conectar ao servidor. Verifique se a API está rodando em <code>http://localhost:8080</code>.'
        );
    } finally {
        setLoading(false);
    }
}

function buildPayload() {
    const montlyQuotaRaw = document.getElementById('montlyQuota').value.trim();

    return {
        name: document.getElementById('name').value.trim(),
        description: document.getElementById('description').value.trim(),
        montlyQuota: montlyQuotaRaw === '' ? null : Number(montlyQuotaRaw)
    };
}

function validatePayload(payload) {
    let isValid = true;
    clearValidationState();

    if (!payload.name) {
        setFieldInvalid('name');
        isValid = false;
    }

    if (!payload.description) {
        setFieldInvalid('description');
        isValid = false;
    }

    if (payload.montlyQuota === null || !Number.isInteger(payload.montlyQuota) || payload.montlyQuota < 0) {
        setFieldInvalid('montlyQuota');
        isValid = false;
    }

    if (!isValid) {
        showAlert('warning', 'Revise os campos destacados antes de enviar o formulário.');
    }

    return isValid;
}

function setFieldInvalid(fieldId) {
    document.getElementById(fieldId).classList.add('is-invalid');
}

function clearValidationState() {
    form.querySelectorAll('.is-invalid').forEach((field) => {
        field.classList.remove('is-invalid');
    });
}

async function extractErrorMessage(response) {
    const contentType = response.headers.get('content-type') || '';

    if (contentType.includes('application/json')) {
        try {
            const body = await response.json();

            if (typeof body === 'string') {
                return body;
            }

            if (body.message) {
                return body.message;
            }

            if (Array.isArray(body.errors) && body.errors.length > 0) {
                return body.errors.map((error) => error.defaultMessage || error.message).join('<br>');
            }
        } catch (error) {
            // fallback to text below
        }
    }

    const text = await response.text();
    if (text) {
        return text;
    }

    return `Erro ao cadastrar procedimento (HTTP ${response.status}).`;
}

function showAlert(type, message) {
    alertContainer.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fechar"></button>
        </div>
    `;
}

function clearAlerts() {
    alertContainer.innerHTML = '';
}

function setLoading(isLoading) {
    submitBtn.disabled = isLoading;
    submitLabel.classList.toggle('d-none', isLoading);
    submitSpinner.classList.toggle('d-none', !isLoading);
}

function escapeHtml(value) {
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}
