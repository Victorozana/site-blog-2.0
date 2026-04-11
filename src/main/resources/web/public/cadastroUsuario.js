let url = "http://localhost:8080/cadastro"

// function voltarParaTelaLogin() {
// // aqui volta para a tela de login
// }

// const form = document.getElementById("formCadastro")

// form.addEventListener("submit", function (event){
//     event.preventDefault();
//     const usuarioDTO = {
//         nome: document.getElementById(name)
//     }
// }){


function clicBtnSalvar(Objeto){

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

