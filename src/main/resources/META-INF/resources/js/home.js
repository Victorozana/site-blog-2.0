// VARIÁVEIS DE ESTADO (A memória do sistema)
let paginaAtual = 0; // O Quarkus/Panache costuma começar a contagem em 0
const tamanhoPagina = 6;

// SELETORES DO DOM (As ferramentas de manipulação)
const containerLista = document.querySelector('#listaBlogs');
const templateCard = document.querySelector('#blog-card-template');
const btnAnterior = document.querySelector('#btnAnterior');
const btnProximo = document.querySelector('#btnProximo');
const textoPagina = document.querySelector('#infoPagina');

function normalizeImageUrl(url) {
    if (!url) return '/img/avatar-placeholder.svg';
    if (url.startsWith('/uploads/images/')) return url.replace('/uploads/images/', '/user/uploads/images/');
    return url;
}

// Função para formatar data em DD/MM/AAAA
function formatarData(dataString) {
    if (!dataString) return '';

    const date = new Date(dataString);
    if (!isNaN(date)) {
        return `Postado dia ${new Intl.DateTimeFormat('pt-BR').format(date)}`;
    }

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
        const url = `/home/posts?page=${paginaAtual}&size=${tamanhoPagina}`;

        const response = await fetch(url);

        if (!response.ok) throw new Error("Falha na comunicação com o servidor");

        let posts = await response.json();

        posts = ordenarPorData(posts);
        renderizarCards(posts);
        atualizarControles(posts.length);

    } catch (error) {
        console.error("[home.js] Erro técnico:", error);
        containerLista.innerHTML = '<div class="empty-state text-danger">Erro ao carregar o conteúdo. Tente novamente mais tarde.</div>';
        btnProximo.disabled = true;
    }
}

function renderizarCards(listaDePosts) {
    containerLista.innerHTML = '';

    if (listaDePosts.length === 0) {
        containerLista.innerHTML = '<div class="empty-state">Nenhuma publicação encontrada nesta página.</div>';
        return;
    }

    listaDePosts.forEach(post => {
        const clone = templateCard.content.cloneNode(true);

        clone.querySelector('.post-title').textContent = post.title || 'Post sem título';
        clone.querySelector('.post-date').textContent = formatarData(post.localDateTime);
        clone.querySelector('.post-subtitle').textContent = post.subtitle || 'Sem resumo disponível.';
        clone.querySelector('.post-link').href = `/blog/?id=${post.id}`;

        const authorLink = clone.querySelector('.post-author-row');
        const authorAvatar = clone.querySelector('.post-author-avatar');
        const authorName = clone.querySelector('.post-author-name');

        authorLink.href = post.authorId ? `/profile?id=${post.authorId}` : '#';
        authorAvatar.src = normalizeImageUrl(post.authorProfilePictureUrl);
        authorAvatar.alt = `Foto de ${post.author || 'autor'}`;
        authorName.textContent = post.author || 'Autor';

        containerLista.appendChild(clone);
    });
}

function atualizarControles(quantidadeRecebida) {
    btnAnterior.disabled = (paginaAtual === 0);
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

document.addEventListener('DOMContentLoaded', () => {
    const labelUsuario = document.querySelector('#userName');

    if (labelUsuario) {
        // Reaproveita a função de leitura que existe no security.js
        const nomeDoUsuario = readCookie('userName');

        if (nomeDoUsuario) {
            labelUsuario.textContent = `Olá, ${nomeDoUsuario}!`;
        } else {
            labelUsuario.textContent = 'Olá, usuário!';
        }
    }
});

// GATILHO INICIAL (Quando o sistema liga)
document.addEventListener('DOMContentLoaded', carregarPosts);
