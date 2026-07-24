const API_BASE_URL = '';

const form = document.getElementById('patient-form');
const alertContainer = document.getElementById('alert-container');
const submitBtn = document.getElementById('submit-btn');
const submitLabel = submitBtn.querySelector('.submit-label');
const submitSpinner = submitBtn.querySelector('.submit-spinner');

document.addEventListener('DOMContentLoaded', () => {
    restrictNumericInput(document.getElementById('susCard'));
    restrictNumericInput(document.getElementById('phoneNumber'));

    form.addEventListener('submit', handleSubmit);
    form.addEventListener('reset', () => {
        clearAlerts();
        clearValidationState();
    });
});

function restrictNumericInput(input) {
    input.addEventListener('input', () => {
        input.value = input.value.replace(/\D/g, '');
    });
}

async function handleSubmit(event) {
    event.preventDefault();
    clearAlerts();

    const payload = buildPayload();
    if (!validatePayload(payload)) {
        return;
    }

    setLoading(true);

    try {
        const response = await fetch(`${API_BASE_URL}/patients`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const patient = await response.json();
            showAlert(
                'success',
                `Paciente <strong>${escapeHtml(patient.name)}</strong> cadastrado com sucesso.`
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
    return {
        name: document.getElementById('name').value.trim(),
        birthDate: document.getElementById('birthDate').value,
        susCard: document.getElementById('susCard').value.trim(),
        phoneNumber: document.getElementById('phoneNumber').value.trim()
    };
}

function validatePayload(payload) {
    let isValid = true;
    clearValidationState();

    if (!payload.name) {
        setFieldInvalid('name');
        isValid = false;
    }

    if (!payload.birthDate || !isPastDate(payload.birthDate)) {
        setFieldInvalid('birthDate');
        isValid = false;
    }

    if (!/^\d{15}$/.test(payload.susCard)) {
        setFieldInvalid('susCard');
        isValid = false;
    }

    if (!/^\d{10,11}$/.test(payload.phoneNumber)) {
        setFieldInvalid('phoneNumber');
        isValid = false;
    }

    if (!isValid) {
        showAlert('warning', 'Revise os campos destacados antes de enviar o formulário.');
    }

    return isValid;
}

function isPastDate(dateString) {
    const date = new Date(`${dateString}T00:00:00`);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return date < today;
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

    return `Erro ao cadastrar paciente (HTTP ${response.status}).`;
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
