const formLogin = document.getElementById("formLogin");

async function login(event) {
    event.preventDefault();

    const email = document.querySelector("#email").value.trim();
    const password = document.querySelector("#password").value.trim();

    if (!email || !password) {
        alert("Informe email e senha.");
        return;
    }

    try {
        const response = await fetch("/login/auth", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email: email, password: password})
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || error.message || "Credenciais inválidas!");
        }

        console.log("Login feito. Redirecionando...");
        window.location.href = "/";

    } catch (error) {
        alert(error.message);
    }
}

formLogin.addEventListener("submit", login);
