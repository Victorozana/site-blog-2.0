const usersTable = document.querySelector('#usersTable');
const usersCount = document.querySelector('#usersCount');
const auditList = document.querySelector('#auditList');
const refreshAudit = document.querySelector('#refreshAudit');
const logoutAdmin = document.querySelector('#logoutAdmin');

function formatDate(value) {
    if (!value) return '-';
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return value;
    return new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' }).format(date);
}

function roleLabel(role) {
    const labels = { ADMIN: 'Administrador', WRITER: 'Escritor', READER: 'Leitor' };
    return labels[role] || role || '-';
}

async function fetchJson(url) {
    const response = await fetch(url, { credentials: 'include' });

    if (!response.ok) {
        throw new Error(response.status === 403 ? 'Acesso restrito a administradores.' : 'Não foi possível carregar os dados.');
    }

    return response.json();
}

function renderUsers(users) {
    usersCount.textContent = users.length;
    usersTable.innerHTML = '';

    users.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${user.name || '-'}</td>
            <td>${user.email || '-'}</td>
            <td><span class="role-badge">${roleLabel(user.userType)}</span></td>
            <td>${formatDate(user.createdDateTime)}</td>
        `;
        usersTable.appendChild(row);
    });
}

function renderAudit(logs) {
    auditList.innerHTML = '';

    if (logs.length === 0) {
        auditList.innerHTML = '<div class="empty-state">Nenhuma ação auditada ainda.</div>';
        return;
    }

    logs.forEach(log => {
        const item = document.createElement('article');
        item.className = 'audit-item';
        item.innerHTML = `
            <div>
                <strong>${log.action}</strong>
                <p>${log.details || 'Sem detalhes.'}</p>
                <small>${log.userName || 'Sistema'} ${log.userType ? `- ${roleLabel(log.userType)}` : ''}</small>
            </div>
            <time>${formatDate(log.createdAt)}</time>
        `;
        auditList.appendChild(item);
    });
}

async function loadAdminData() {
    try {
        const [users, logs] = await Promise.all([
            fetchJson('/admin/users'),
            fetchJson('/admin/audit?limit=80')
        ]);

        renderUsers(users);
        renderAudit(logs);
    } catch (error) {
        auditList.innerHTML = `<div class="empty-state text-danger">${error.message}</div>`;
    }
}

refreshAudit.addEventListener('click', loadAdminData);
logoutAdmin.addEventListener('click', () => logoutUser());
document.addEventListener('DOMContentLoaded', loadAdminData);
