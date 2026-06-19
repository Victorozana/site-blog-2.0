let url = "http://localhost:8080/register/user"

// Máscara para data (dd/mm/yyyy)
function maskDate(input) {
    let value = input.value.replace(/\D/g, '');
    if (value.length > 8) value = value.slice(0, 8);
    
    if (value.length >= 1) {
        value = value.slice(0, 2) + (value.length >= 3 ? '/' : '') + value.slice(2, 4) + (value.length >= 5 ? '/' : '') + value.slice(4, 8);
    }
    input.value = value;
}

// Máscara para telefone ((99)99999-2222)
function maskPhone(input) {
    let value = input.value.replace(/\D/g, '');
    if (value.length > 11) value = value.slice(0, 11);
    
    if (value.length >= 1) {
        value = '(' + value.slice(0, 2) + (value.length >= 3 ? ')' : '') + value.slice(2, 7) + (value.length >= 8 ? '-' : '') + value.slice(7, 11);
    }
    input.value = value;
}

// Converter dd/mm/yyyy para yyyy-mm-dd
function convertDateFormat(dateStr) {
    const [day, month, year] = dateStr.split('/');
    return `${year}-${month}-${day}`;
}

// Adicionar event listeners para as máscaras
document.getElementById("dt_nasc").addEventListener('input', (e) => maskDate(e.target));
document.getElementById("fone").addEventListener('input', (e) => maskPhone(e.target));

const form = document.getElementById("formCadastro")

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const dtNascRaw = document.getElementById("dt_nasc").value;
    
    // Validar formato da data
    if (!/^\d{2}\/\d{2}\/\d{4}$/.test(dtNascRaw)) {
        alert("Data inválida! Use o formato DD/MM/YYYY");
        return;
    }

    const userRegistrationDTO = {
        name: document.getElementById("name").value,
        lastname: document.getElementById("lastname").value,
        dtNasc: convertDateFormat(dtNascRaw),
        fone: document.getElementById("fone").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        userType: document.getElementById("user_type").value
    };

    try {
        const response = await sendForBackend(userRegistrationDTO);

        if (response.ok) {
            alert("Usuário cadastrado com sucesso!");
            form.reset();
            window.location.href = "/login";
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

async function sendForBackend(dataDTO){
    return await fetch("/register/user", {
        method: "POST",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(dataDTO)
    });
}