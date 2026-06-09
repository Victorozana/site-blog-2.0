const formLogin = document.getElementById("formLogin");

async function login(event) {
    event.preventDefault();

    const email = document.querySelector("#email").value;
    const password = document.querySelector("#password").value;

    try {
        const response = await fetch("/login/auth", { // Ajuste para sua rota real
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email: email, password: password})
        });

        if (!response.ok) throw new Error("Credenciais inválidas!");

        // Como o Java devolveu o Set-Cookie no cabeçalho, o navegador já guardou
        // o token invisível e os cookies de nome/tipo de usuário automaticamente!

        console.log("Login feito. Redirecionando...");
        window.location.href = "/"; // Vai para a home

    } catch (error) {
        alert(error.message);
    }
}

formLogin.addEventListener("submit", login);