const formLogin = document.getElementById("formLogin");

async function login(event) {
    event.preventDefault();

    const email = document.querySelector("#email").value;
    const password = document.querySelector("#password").value;

    try {
        const response = await fetch("/login/auth", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email: email, password: password})
        });

        if (!response.ok) throw new Error("Credenciais inválidas!");

        console.log("Login feito. Redirecionando...");
        window.location.href = "/";

    } catch (error) {
        alert(error.message);
    }
}

formLogin.addEventListener("submit", login);