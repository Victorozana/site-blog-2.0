// VARIÁVEIS DE ESTADO (A memória do sistema)
let paginaAtual = 0; // O Quarkus/Panache costuma começar a contagem em 0
const tamanhoPagina = 6;

// SELETORES DO DOM (As ferramentas de manipulação)
const containerLista = document.querySelector('#listaBlogs');
const templateCard = document.querySelector('#blog-card-template');
const btnAnterior = document.querySelector('#btnAnterior');
const btnProximo = document.querySelector('#btnProximo');
const textoPagina = document.querySelector('#infoPagina');

// Função para formatar data em DD/MM/AAAA
function formatarData(dataString) {
    if (!dataString) return '';
    
    // Se for ISO (YYYY-MM-DDTHH:MM:SS)
    const date = new Date(dataString);
    if (!isNaN(date)) {
        const dia = String(date.getDate()).padStart(2, '0');
        const mes = String(date.getMonth() + 1).padStart(2, '0');
        const ano = date.getFullYear();
        return `Postado dia ${dia}/${mes}/${ano}`;
    }
    
    // Se for string custom, tenta parsear manualmente
    return dataString;
}

// Função para ordenar posts por data (mais recente primeiro)
function ordenarPorData(posts) {
    return posts.sort((a, b) => {
        const dataA = new Date(a.localDateTime || a.createdAt || 0);
        const dataB = new Date(b.localDateTime || b.createdAt || 0);
        return dataB - dataA; // Decrescente (mais recente primeiro)
    });
}

async function carregarPosts() {
    try {
        // 1. Montagem da URL (Contrato de API)
        const url = `/api/posts?page=${paginaAtual}&size=${tamanhoPagina}`;
        console.log("🔵 [home.js] Chamando API:", url);

        const response = await fetch(url);
        console.log("🔵 [home.js] Response status:", response.status);

        if (!response.ok) throw new Error("Falha na comunicação com o servidor");

        let posts = await response.json();
        console.log("🔵 [home.js] Posts recebidos:", posts);

        // 2. Ordenar por data (mais recente primeiro)
        posts = ordenarPorData(posts);

        // 3. Renderização
        renderizarCards(posts);

        // 4. Atualização da Interface (Botões)
        atualizarControles(posts.length);

    } catch (error) {
        console.error("❌ [home.js] Erro técnico:", error);
        containerLista.innerHTML = `<p class="text-danger text-center">Erro ao carregar o conteúdo. Tente novamente mais tarde.</p>`;
    }
}

function renderizarCards(listaDePosts) {
    // 1. Limpa o terreno (Fundamental para não encavalar os posts)
    containerLista.innerHTML = '';

    if (listaDePosts.length === 0) {
        containerLista.innerHTML = '<p class="text-center">Nenhuma publicação encontrada nesta página.</p>';
        return;
    }

    listaDePosts.forEach(post => {
        // 2. Tira o 'Xerox' do molde
        const clone = templateCard.content.cloneNode(true);

        // 3. Preenche as lacunas com os dados do DTO
        clone.querySelector('.post-title').textContent = post.title;
        clone.querySelector('.post-date').textContent = formatarData(post.localDateTime);
        clone.querySelector('.post-subtitle').textContent = post.subtitle;
        clone.querySelector('.post-link').href = `/blog/?id=${post.id}`;

        // 4. Pendura o card pronto na tela
        containerLista.appendChild(clone);
    });
}

function atualizarControles(quantidadeRecebida) {
    // Lógica cética: Se estou na página 0, não posso voltar
    btnAnterior.disabled = (paginaAtual === 0);

    // Se veio menos posts do que o limite, significa que a próxima página está vazia
    btnProximo.disabled = (quantidadeRecebida < tamanhoPagina);

    textoPagina.textContent = `Página ${paginaAtual + 1}`;
}

// OUVINTES DE EVENTOS (Gatilhos)
btnAnterior.addEventListener('click', () => {
    if (paginaAtual > 0) {
        paginaAtual--;
        carregarPosts();
    }
});

btnProximo.addEventListener('click', () => {
    paginaAtual++;
    carregarPosts();
});

// GATILHO INICIAL (Quando o sistema liga)
document.addEventListener('DOMContentLoaded', carregarPosts);