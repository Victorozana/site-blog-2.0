// 1. CAPTURA DO ID DA URL (A mala que veio da Home)
const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');

// 2. SELETORES DO DOM (Seus Alvos)
const postTitle = document.querySelector('#post-title');
const postSubtitle = document.querySelector('#post-subtitle');
const postDate = document.querySelector('#post-data');
const postAuthorLink = document.querySelector('#post-author-link');
const postAuthorName = document.querySelector('#post-author-name');
const postAuthorAvatar = document.querySelector('#post-author-avatar');
const postDescription = document.querySelector('#post-description');
const postManagement = document.querySelector('#post-management');
const editPostForm = document.querySelector('#editPostForm');
const editTitle = document.querySelector('#editTitle');
const editSubtitle = document.querySelector('#editSubtitle');
const editCategory = document.querySelector('#editCategory');
const editDescription = document.querySelector('#editDescription');
const btnDeletePost = document.querySelector('#btnDeletePost');
const btnSavePost = document.querySelector('#btnSavePost');
const postManagementMessage = document.querySelector('#postManagementMessage');

let currentPost = null;

function normalizeImageUrl(url) {
    if (!url) return '/img/avatar-placeholder.svg';
    if (url.startsWith('/uploads/images/')) return url.replace('/uploads/images/', '/user/uploads/images/');
    return url;
}

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
    if (!id) {
        console.error("Nenhum ID de postagem foi fornecido na URL.");
        postTitle.textContent = 'Post não encontrado';
        postSubtitle.textContent = 'Abra um post a partir da página inicial.';
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
        postTitle.textContent = 'Erro ao carregar o post';
        postSubtitle.textContent = 'Tente novamente mais tarde.';
    }
}

// 5. INJEÇÃO DOS DADOS NA TELA
function renderizarPost(post){
    currentPost = post;
    postTitle.textContent = post.title || 'Post sem título';
    postSubtitle.textContent = post.subtitle || '';
    postAuthorName.textContent = post.author || 'Autor desconhecido';
    postAuthorAvatar.src = normalizeImageUrl(post.authorProfilePictureUrl);
    postAuthorAvatar.alt = `Foto de ${post.author || 'autor'}`;
    postAuthorLink.href = post.authorId ? `/profile?id=${post.authorId}` : '#';
    postDate.textContent = formatarData(post.localDateTime);
    postDescription.textContent = post.description || '';
    renderManagementPanel(post);
}

function canManagePost(post) {
    const userRole = typeof readCookie === 'function' ? readCookie('userType') : null;
    const userId = typeof readCookie === 'function' ? readCookie('userId') : null;

    if (userRole === 'ADMIN') return true;
    return userRole === 'WRITER' && userId && String(post.authorId) === String(userId);
}

function renderManagementPanel(post) {
    if (!postManagement || !canManagePost(post)) {
        postManagement?.classList.add('d-none');
        return;
    }

    postManagement.classList.remove('d-none');
    editTitle.value = post.title || '';
    editSubtitle.value = post.subtitle || '';
    editCategory.value = post.category || 'advices';
    editDescription.value = post.description || '';
}

function showManagementMessage(text, type = 'info') {
    if (!postManagementMessage) return;
    postManagementMessage.textContent = text;
    postManagementMessage.classList.remove('is-success', 'is-error');
    if (type === 'success') postManagementMessage.classList.add('is-success');
    if (type === 'error') postManagementMessage.classList.add('is-error');
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

function renderLikeState(data) {
    likesCount = data.totalLikes || 0;
    likeCountEl.textContent = likesCount;
    btnLike.setAttribute('aria-pressed', String(Boolean(data.userLiked)));
    btnLike.classList.toggle('text-danger', Boolean(data.userLiked));
}

async function readErrorMessage(response, fallbackMessage = 'Falha ao processar a solicitação') {
    try {
        const data = await response.json();
        return data.error || data.message || fallbackMessage;
    } catch (e) {
        return fallbackMessage;
    }
}

async function fetchLikes(){
    try{
        const res = await fetch(`/blogs/${id}/likes`, { method: 'GET', credentials: 'include' });
        if (res.ok){
            const data = await res.json();
            renderLikeState(data);
        }
    }catch(e){ console.warn('Erro ao buscar likes', e); }
}

async function fetchComments(){
    if (!id) return;

    try{
        const res = await fetch(`/blogs/${id}/comments`, { method: 'GET', credentials: 'include' });
        if (res.ok){
            const data = await res.json();
            renderComments(data);
        }
    }catch(e){ console.warn('Erro ao buscar comentários', e); }
}

function renderComments(list){
    commentsList.innerHTML = '';
    const validComments = Array.isArray(list)
        ? list.filter(c => c && c.author && c.comment)
        : [];

    if (validComments.length === 0) {
        commentsList.innerHTML = '<div class="text-muted">Seja o primeiro a comentar.</div>';
        return;
    }
    validComments.forEach(c => {
        const div = document.createElement('div');
        div.className = 'list-group-item';

        const avatar = document.createElement('img');
        avatar.className = 'comment-avatar';
        avatar.src = normalizeImageUrl(c.authorProfilePictureUrl);
        avatar.alt = `Foto de ${c.author || 'usuário'}`;

        const content = document.createElement('div');
        content.className = 'comment-content';

        const author = document.createElement('div');
        author.className = 'fw-semibold';
        author.textContent = c.author;

        const text = document.createElement('div');
        text.className = 'comment-text';
        text.textContent = c.comment || '';

        content.appendChild(author);
        content.appendChild(text);
        div.appendChild(avatar);
        div.appendChild(content);
        commentsList.appendChild(div);
    });
}

btnToggleComments?.addEventListener('click', () => {
    commentsSection.classList.toggle('d-none');
    const isOpen = !commentsSection.classList.contains('d-none');
    btnToggleComments.setAttribute('aria-expanded', String(isOpen));
    if (isOpen) fetchComments();
});

btnLike?.addEventListener('click', async () => {
    const isLiked = btnLike.getAttribute('aria-pressed') === 'true';
    const metodoHttp = isLiked ? 'DELETE' : 'POST';

    try {
        btnLike.disabled = true;

        const res = await fetch(`/blogs/${id}/like`, {
            method: metodoHttp,
            credentials: 'include'
        });

        if (!res.ok) {
            throw new Error(await readErrorMessage(res, 'Falha ao processar a curtida'));
        }

        const data = await res.json();
        renderLikeState(data);
    } catch(e) {
        console.warn('Erro ao processar a curtida', e);
        fetchLikes();
    } finally {
        btnLike.disabled = false;
    }
});

commentForm?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const text = commentText.value.trim();
    if (!text || !id || btnSendComment.disabled) return;

    if (text.length < 2 || text.length > 1000) {
        console.warn('Comentário deve ter entre 2 e 1000 caracteres.');
        return;
    }

    try{
        btnSendComment.disabled = true;

        const res = await fetch(`/blogs/${id}/comment`, {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ comment: text })
        });
        if (res.ok){
            commentText.value = '';
            fetchComments();
        } else {
            console.warn(await readErrorMessage(res, 'Falha ao enviar comentário'));
        }
    }catch(e){
        console.warn('Erro ao enviar comentário', e);
    } finally {
        btnSendComment.disabled = false;
    }
});

editPostForm?.addEventListener('submit', async (event) => {
    event.preventDefault();

    const title = editTitle.value.trim();
    const subtitle = editSubtitle.value.trim();
    const category = editCategory.value;
    const description = editDescription.value.trim();

    if (title.length < 3 || title.length > 120) {
        showManagementMessage('Título deve ter entre 3 e 120 caracteres.', 'error');
        return;
    }

    if (subtitle.length > 160) {
        showManagementMessage('Subtítulo deve ter no máximo 160 caracteres.', 'error');
        return;
    }

    if (!category) {
        showManagementMessage('Selecione uma categoria.', 'error');
        return;
    }

    if (description.length < 20 || description.length > 10000) {
        showManagementMessage('Conteúdo deve ter entre 20 e 10000 caracteres.', 'error');
        return;
    }

    try {
        btnSavePost.disabled = true;
        showManagementMessage('Salvando alterações...');

        const response = await fetch(`/blog/${id}`, {
            method: 'PATCH',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, subtitle, category, description })
        });

        if (!response.ok) {
            throw new Error(await readErrorMessage(response, 'Não foi possível atualizar o post.'));
        }

        showManagementMessage('Post atualizado com sucesso.', 'success');
        await carregarPost();
    } catch (error) {
        showManagementMessage(error.message, 'error');
    } finally {
        btnSavePost.disabled = false;
    }
});

btnDeletePost?.addEventListener('click', async () => {
    if (!currentPost) return;

    const confirmed = window.confirm('Tem certeza que deseja excluir este post? Essa ação não pode ser desfeita.');
    if (!confirmed) return;

    try {
        btnDeletePost.disabled = true;
        showManagementMessage('Excluindo post...');

        const response = await fetch(`/blog/${id}`, {
            method: 'DELETE',
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error(await readErrorMessage(response, 'Não foi possível excluir o post.'));
        }

        window.location.href = '/';
    } catch (error) {
        showManagementMessage(error.message, 'error');
        btnDeletePost.disabled = false;
    }
});

// Reaplica carregamento inicial
document.addEventListener('DOMContentLoaded', () => {
    carregarPost();
    fetchLikes();
});

