// VARIÁVEIS DE ESTADO (A memória do sistema)
let paginaAtual = 0; // O Quarkus/Panache costuma começar a contagem em 0
const tamanhoPagina = 6;

// SELETORES DO DOM (As ferramentas de manipulação)
const containerLista = document.querySelector('#listaBlogs');
const templateCard = document.querySelector('#blog-card-template');
const btnAnterior = document.querySelector('#btnAnterior');
const btnProximo = document.querySelector('#btnProximo');
const textoPagina = document.querySelector('#infoPagina');

async function carregarPosts() {
    try {
        // 1. Montagem da URL (Contrato de API)
        const url = `/blogs/posts?page=${paginaAtual}&size=${tamanhoPagina}`;

        const response = await fetch(url);

        if (!response.ok) throw new Error("Falha na comunicação com o servidor");

        const posts = await response.json();

        // 2. Renderização
        renderizarCards(posts);

        // 3. Atualização da Interface (Botões)
        atualizarControles(posts.length);

    } catch (error) {
        console.error("Erro técnico:", error);
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
        clone.querySelector('.post-titulo').textContent = post.title;
        clone.querySelector('.post-sub')
        clone.querySelector('.post-data').textContent = post.localDateTime;
        clone.querySelector('.post-resumo').textContent = post.subtitle;
        clone.querySelector('.post-link').href = `/blog/post/${post.id}`;

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