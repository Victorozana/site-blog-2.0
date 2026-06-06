function protectRouter() {
    const loginUser = readCookie('userName');
    const userType = localStorage.getItem('userType');

    if (!loginUser) {
        window.location.href = "/login";
        return;
    }
}

function readCookie(cookieName) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(cookieName + '=')) {
            return decodeURIComponent(cookie.substring(cookieName.length + 2));
        }
    }
    return null;
}

function getUserType() {
    return localStorage.getItem('userType');
}

function hasPermission(requiredRole) {
    const userType = getUserType();
    if (Array.isArray(requiredRole)) {
        return requiredRole.includes(userType);
    }
    return userType === requiredRole;
}

function checkPermissionAndHide(buttonSelector, requiredRole) {
    if (!hasPermission(requiredRole)) {
        const element = document.querySelector(buttonSelector);
        if (element) {
            element.style.display = 'none';
        }
    }
}

protectRouter();
