function protectRouter() {
    const loginUser =readCookie('userName');

    if (!loginUser){
        window.location.href = "/login"
        console.log("chegou aqui");
    }
}

function readCookie(cookieName) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++){
        const cookie = cookies[i].trim();
        if (cookie.startsWith(cookieName + '=')){
            return decodeURIComponent(cookie.substring(cookieName.length + 2));
        }
    }
    return null;
}

protectRouter();