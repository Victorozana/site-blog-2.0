var url = "http://localhost:8080/login"
const form = document.getElementById("formLogin")

// O "async" aqui em cima permite o uso do "await" lá embaixo
form.addEventListener("submit", async (event) => {
    event.preventDefault();

    // 1. Captura os dados (Cuidado com o nome das variáveis!)
    const userLoginDTO = {
        email: document.getElementById("email").value,
        cryptographyPassword: document.getElementById("cryptography_password").value,
    };

    // 2. O try/catch PRECISA estar aqui dentro para o 'await' funcionar
    try {
        const response = await enviarParaBackend(userLoginDTO);

        if (response.ok) {
            alert("Usuário logado com sucesso!");
            form.reset();
        } else {
            const erro = await response.json();
            console.error("Erro do servidor:", erro);
            alert("Erro ao logar: " + (erro.details || "Verifique os dados."));
        }
    } catch (error) {
        console.error("Falha na conexão:", error);
        alert("Erro de conexão. O servidor Quarkus está ligado?");
    }
});

async function enviarParaBackend(dadosDTO){
    return await fetch("http://localhost:8080/login/auth", {
        method: "GET",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(dadosDTO)
    });
}

function logarUsuario(usuario){
    // requisição
    fetch(url, {
        method: "GET",
        body: JSON.stringify(usuario)
    }).then(function (response){
        if (response.status === 200)
            console.log("sucesso ao logar usuário")
    }).then(function(data){
        console.log(data);
    }).catch(function (erro){
        console.error("Erro ao enviar", erro);
    });
}