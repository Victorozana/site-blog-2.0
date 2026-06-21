// Arquivo: security.js

// 1. O Leitor Universal de Cookies
function readCookie(cookieName) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(cookieName + '=')) {
            // O +1 corrigido para não cortar a primeira letra!
            return decodeURIComponent(cookie.substring(cookieName.length + 1));
        }
    }
    return null;
}

// 2. O Porteiro da Tela (Redireciona se não estiver logado)
function protectRoute() {
    const userName = readCookie('userName');
    if (!userName) {
        window.location.href = "/login"; // URL da sua tela de login
    }
}

// 3. O Gerente de Interface (Esconde botões que o usuário não tem permissão)
function checkPermissionAndHide(buttonSelector, requiredRole) {
    const userRole = readCookie('userType'); // Lê o cargo direto do cookie público

    if (userRole !== requiredRole) {
        const element = document.querySelector(buttonSelector);
        if (element) {
            element.style.display = 'none'; // Esconde o botão
        }
    }
}

async function logoutUser() {
    try {
        await fetch('/login/logout', {
            method: 'POST',
            credentials: 'include'
        });
    } finally {
        window.location.href = '/login';
    }
}

// 4. Executa a proteção de tela imediatamente
protectRoute();
