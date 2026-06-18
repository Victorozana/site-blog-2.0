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
        const response = await fetch(`/blog/data?id=${id}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include'
        });

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
    const descricaoOriginal = post.description;
    document.querySelector('#post-description').innerHTML = descricaoOriginal.replace(/\n/g, '<br>');
}

// 6. INTERAÇÃO: likes e comentários
const btnLike = document.getElementById('btnLike');
const likeCountEl = document.getElementById('likeCount');
const btnToggleComments = document.getElementById('btnToggleComments');
const commentsSection = document.getElementById('comments');
const commentsList = document.getElementById('comments-list');
const commentForm = document.getElementById('comment-form');
const commentText = document.getElementById('commentText');
const btnSendComment = document.getElementById('btnSendComment');

let likesCount = 0;

async function fetchLikes(){
    try{
        const res = await fetch(`/blogs/${id}/likes`, { method: 'GET', credentials: 'include' });
        if (res.ok){
            const data = await res.json(); // assume { count: number, likedByMe: boolean }
            likesCount = data.totalLikes || 0;
            likeCountEl.textContent = likesCount;
            if (data.userLiked) btnLike.setAttribute('aria-pressed', 'true');
        }
    }catch(e){ console.warn('Erro ao buscar likes', e); }
}

async function fetchComments(){
    try{
        const res = await fetch(`/blogs/${id}/comments`, { method: 'GET', credentials: 'include' });
        if (res.ok){
            const data = await res.json(); // assume array of { id, idUser, comment, createdAt }
            renderComments(data);
        }
    }catch(e){ console.warn('Erro ao buscar comentários', e); }
}

function renderComments(list){
    commentsList.innerHTML = '';
    if (!Array.isArray(list) || list.length === 0) {
        commentsList.innerHTML = '<div class="text-muted">Seja o primeiro a comentar.</div>';
        return;
    }
    list.forEach(c => {
        const div = document.createElement('div');
        div.className = 'list-group-item';
        div.innerHTML = `<div class="fw-semibold">${c.author}</div><div class="comment-text">${escapeHtml(c.comment)}</div><small class="text-muted">${formatarData(c.createdAt)}</small>`;
        commentsList.appendChild(div);
    });
}

function escapeHtml(unsafe){ return unsafe.replace(/[&<>"']/g, function(m){ return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'})[m]; }); }

btnToggleComments?.addEventListener('click', () => {
    commentsSection.classList.toggle('d-none');
    if (!commentsSection.classList.contains('d-none')) fetchComments();
});

btnLike?.addEventListener('click', async () => {
    // Verifica visualmente se o botão já está marcado como curtido
    const isLiked = btnLike.getAttribute('aria-pressed') === 'true';

    // Se já curtiu, a intenção é DELETAR. Se não curtiu, a intenção é CRIAR (POST)
    const metodoHttp = isLiked ? 'DELETE' : 'POST';

    try {
        const res = await fetch(`/blogs/${id}/like`, {
            method: metodoHttp,
            credentials: 'include'
        });

        if (res.ok) { // Status 201 (Created) ou 204 (No Content)
            if (isLiked) {
                // Removeu o like
                likesCount -= 1;
                btnLike.setAttribute('aria-pressed', 'false');
                btnLike.classList.remove('text-danger'); // Exemplo: remove a cor vermelha
            } else {
                // Deu o like
                likesCount += 1;
                btnLike.setAttribute('aria-pressed', 'true');
                btnLike.classList.add('text-danger'); // Exemplo: pinta de vermelho
            }

            // Atualiza o número na tela
            likeCountEl.textContent = likesCount;
        }
    } catch(e) {
        console.warn('Erro ao processar a curtida', e);
    }
});

commentForm?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const text = commentText.value.trim();
    if (!text) return;

    try{
        const res = await fetch(`/blogs/${id}/comment`, {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ comment: text })
        });
        if (res.ok){
            commentText.value = '';
            // refresh list
            fetchComments();
        } else {
            console.warn('Falha ao enviar comentário');
        }
    }catch(e){ console.warn('Erro', e); }
});

// Reaplica carregamento inicial
document.addEventListener('DOMContentLoaded', () => {
    carregarPost();
    fetchLikes();
});

