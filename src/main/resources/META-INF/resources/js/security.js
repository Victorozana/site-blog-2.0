// 1. Leitor de Cookies
function readCookie(cookieName) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(cookieName + '=')) {
            return decodeURIComponent(cookie.substring(cookieName.length + 1));
        }
    }
    return null;
}

function deleteCookie(cookieName) {
    document.cookie = `${cookieName}=; Max-Age=0; path=/`;
}

function clearAuthCookies() {
    ['meu_token_jwt', 'userName', 'userType', 'userId'].forEach(deleteCookie);

    document.cookie.split(';').forEach(cookie => {
        const cookieName = cookie.split('=')[0].trim();

        if (cookieName) {
            deleteCookie(cookieName);
        }
    });
}

// 2. Redireciona se não estiver logado
function protectRoute() {
    const userName = readCookie('userName');
    if (!userName) {
        window.location.href = "/login"; // URL da sua tela de login
    }
}

// 3. Esconde botões que o usuário não tem permissão
function checkPermissionAndHide(buttonSelector, requiredRole) {
    const userRole = readCookie('userType'); // Lê o cargo direto do cookie público

    if (userRole !== requiredRole) {
        const element = document.querySelector(buttonSelector);
        if (element) {
            element.style.display = 'none'; // Esconde o botão
        }
    }
}

// 4. Logout do usuário, apagando os cookie
async function logoutUser() {
    try {
        await fetch('/login/logout', {
            method: 'POST',
            credentials: 'include'
        });
    } finally {
        clearAuthCookies();
        window.location.href = '/login';
    }
}

// 5. Executa a proteção de tela imediatamente
protectRoute();
