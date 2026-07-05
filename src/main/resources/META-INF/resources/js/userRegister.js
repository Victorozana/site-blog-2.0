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

function isValidBirthDate(dateStr) {
    if (!/^\d{2}\/\d{2}\/\d{4}$/.test(dateStr)) return false;

    const [day, month, year] = dateStr.split('/').map(Number);
    const date = new Date(year, month - 1, day);

    if (date.getFullYear() !== year || date.getMonth() !== month - 1 || date.getDate() !== day) {
        return false;
    }

    const today = new Date();
    const minimumDate = new Date(today.getFullYear() - 120, today.getMonth(), today.getDate());
    const maximumDate = new Date(today.getFullYear() - 13, today.getMonth(), today.getDate());

    return date >= minimumDate && date <= maximumDate;
}

function isValidName(value) {
    return /^[A-Za-zÀ-ÖØ-öø-ÿ\s]{2,60}$/.test(value.trim());
}

// Adicionar event listeners para as máscaras
document.getElementById("dt_nasc").addEventListener('input', (e) => maskDate(e.target));
document.getElementById("fone").addEventListener('input', (e) => maskPhone(e.target));

const form = document.getElementById("formCadastro")

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const dtNascRaw = document.getElementById("dt_nasc").value;
    const name = document.getElementById("name").value.trim();
    const lastname = document.getElementById("lastname").value.trim();

    if (!isValidName(name)) {
        alert("Nome deve conter apenas letras e espaços.");
        return;
    }

    if (!isValidName(lastname)) {
        alert("Sobrenome deve conter apenas letras e espaços.");
        return;
    }
    
    if (!isValidBirthDate(dtNascRaw)) {
        alert("Data inválida. Informe uma data real, entre 13 e 120 anos de idade.");
        return;
    }

    const userRegistrationDTO = {
        name: name,
        lastname: lastname,
        dtNasc: convertDateFormat(dtNascRaw),
        fone: document.getElementById("fone").value,
        email: document.getElementById("email").value.trim(),
        password: document.getElementById("password").value.trim(),
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
            alert("Erro ao cadastrar: " + (erro.error || erro.message || "Verifique os dados."));
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