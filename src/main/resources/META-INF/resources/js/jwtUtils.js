function getTokenFromStorage() {
    return localStorage.getItem('token');
}

function parseJWT(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map((c) => {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } catch (error) {
        console.error('Erro ao fazer parse do JWT:', error);
        return null;
    }
}

function getUserIdFromToken() {
    const token = getTokenFromStorage();
    if (!token) return null;
    
    const payload = parseJWT(token);
    return payload ? payload.idUser : null;
}

function getNameFromToken() {
    const token = getTokenFromStorage();
    if (!token) return null;
    
    const payload = parseJWT(token);
    return payload ? payload.name : null;
}

function getUserTypeFromToken() {
    const token = getTokenFromStorage();
    if (!token) return null;
    
    const payload = parseJWT(token);
    return payload ? payload.userType : null;
}

function getAuthHeaders() {
    const token = getTokenFromStorage();
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
}
