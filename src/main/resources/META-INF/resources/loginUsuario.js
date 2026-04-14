var url = "http://localhost:8080/login/auth"

const form = document.getElementById("formLogin")

// No seu arquivo loginUsuario.js
form.addEventListener('submit', async function(e) { // Adicione 'async' aqui
    e.preventDefault();

    const usuarioDTO = {
        email: document.getElementById("email").value,
        cryptographyPassword: document.getElementById("password").value
    };

    try {
        // Espera a função logarUsuario terminar e trazer a resposta
        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(usuarioDTO)
        });

        // AGORA o 'response' existe e o '.ok' vai funcionar!
        if (response.ok) {
            console.log("Login de sucesso!");
            const data = await response.json();
            // Redirecionar usuário ou salvar token
        } else {
            alert("Usuário ou senha inválidos");
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
    }
});