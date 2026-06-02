let url = "http://localhost:8080/register/user"

// function voltarParaTelaLogin() {
// // aqui volta para a tela de login
// }

const form = document.getElementById("formCadastro")

// O "async" aqui em cima permite o uso do "await" lá embaixo
form.addEventListener("submit", async (event) => {
    event.preventDefault();

    // 1. Captura os dados (Cuidado com o nome das variáveis!)
    const userRegistrationDTO = {
        name: document.getElementById("name").value,
        lastname: document.getElementById("lastname").value,
        dtNasc: document.getElementById("dt_nasc").value,
        fone: document.getElementById("fone").value,
        email: document.getElementById("email").value,
        cryptographyPassword: document.getElementById("cryptography_password").value,
        userType: document.getElementById("user_type").value
    };

    // 2. O try/catch PRECISA estar aqui dentro para o 'await' funcionar
    try {
        // Corrigi o erro de digitação: era userResgistrationDTO, agora é userRegistrationDTO
        const response = await enviarParaBackend(userRegistrationDTO);

        if (response.ok) {
            alert("Usuário cadastrado com sucesso!");
            form.reset();
        } else {
            const erro = await response.json();
            console.error("Erro do servidor:", erro);
            alert("Erro ao cadastrar: " + (erro.details || "Verifique os dados."));
        }
    } catch (error) {
        console.error("Falha na conexão:", error);
        alert("Erro de conexão. O servidor Quarkus está ligado?");
    }
});

async function enviarParaBackend(dadosDTO){
    return await fetch("http://localhost:8080/register/user", {
        method: "POST",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(dadosDTO)
    });
}

function registrarUsuario(usuario){
    // requisição
    fetch(url, {
        method: "POST",
        body: JSON.stringify(usuario)
    }).then(function (response){
        if (response.status === 200)
            console.log("sucesso ao criar usuário")
    }).then(function(data){
        console.log(data);
    }).catch(function (erro){
        console.error("Erro ao enviar", erro);
    });
}

