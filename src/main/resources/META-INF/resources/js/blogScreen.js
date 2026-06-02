console.log("=== O JavaScript foi carregado com sucesso! ===");

// 1. CAPTURA DO ID DA URL (A mala que veio da Home)
const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');

// 2. SELETORES DO DOM (Seus Alvos)
const postTitle = document.querySelector('#post-title');
const postSubtitle = document.querySelector('#post-subtitle');
const postDate = document.querySelector('#post-data');
const postAuthor = document.querySelector('#post-author');
const postDescription = document.querySelector('#post-description');

// 3. FUNÇÃO UTILITÁRIA DE DATA
function formatarData(dataString) {
    if (!dataString) return '';
    const date = new Date(dataString);
    if (!isNaN(date)) {
        const dia = String(date.getDate()).padStart(2, '0');
        const mes = String(date.getMonth() + 1).padStart(2, '0');
        const ano = date.getFullYear();
        return `Postado dia ${dia}/${mes}/${ano}`;
    }
    return dataString;
}

// 4. CHAMADA ASSÍNCRONA À API
async function carregarPost(){
    // Proteção cética: se não houver ID na URL, avisa no console e interrompe
    if (!id) {
        console.error("Nenhum ID de postagem foi fornecido na URL.");
        return;
    }

    try {
        const url = `/blog/data?id=${id}`;
        const response = await fetch(url);

        if(!response.ok) throw new Error("Falha na comunicação com o servidor");

        let post = await response.json();

        renderizarPost(post);
    } catch (error){
        console.error("Erro técnico:", error);
    }
}

// 5. INJEÇÃO DOS DADOS NA TELA
function renderizarPost(post){
    postTitle.textContent = post.title;
    postSubtitle.textContent = post.subtitle;
    postAuthor.textContent = " — Autor: " + post.author;
    postDate.textContent = formatarData(post.localDateTime);
    postDescription.innerHTML = post.description;
}

// 6. GATILHO DE SUCESSO
document.addEventListener('DOMContentLoaded', carregarPost);

