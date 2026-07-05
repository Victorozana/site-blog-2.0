const postsList = document.querySelector('#managedPostsList');
const template = document.querySelector('#managedPostTemplate');
const message = document.querySelector('#manageMessage');
const headerCreatePost = document.querySelector('#headerCreatePost');

function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    if (Number.isNaN(date.getTime())) return '';
    return `Publicado em ${new Intl.DateTimeFormat('pt-BR').format(date)}`;
}

function showMessage(text, type = 'info') {
    message.textContent = text;
    message.classList.remove('is-error', 'is-success');
    if (type === 'error') message.classList.add('is-error');
    if (type === 'success') message.classList.add('is-success');
}

async function readErrorMessage(response, fallbackMessage) {
    try {
        const data = await response.json();
        return data.error || data.message || fallbackMessage;
    } catch (error) {
        return fallbackMessage;
    }
}

// carregar posts
async function loadManagedPosts() {
    showMessage('Carregando posts...');

    try {
        const response = await fetch('/blog/manage/data', { credentials: 'include' });

        if (!response.ok) {
            throw new Error(await readErrorMessage(response, 'Não foi possível carregar seus posts.'));
        }

        renderPosts(await response.json());
        showMessage('');
    } catch (error) {
        showMessage(error.message, 'error');
    }
}

function renderPosts(posts) {
    postsList.innerHTML = '';

    if (!Array.isArray(posts) || posts.length === 0) {
        postsList.innerHTML = '<div class="empty-managed-posts">Nenhum post publicado para gerenciar.</div>';
        return;
    }

    posts.forEach(post => {
        const clone = template.content.cloneNode(true);
        const article = clone.querySelector('.managed-post');
        const title = clone.querySelector('.managed-post-title');
        const subtitle = clone.querySelector('.managed-post-subtitle');
        const author = clone.querySelector('.managed-post-author');
        const date = clone.querySelector('.managed-post-date');
        const edit = clone.querySelector('.managed-post-edit');
        const deleteButton = clone.querySelector('.managed-post-delete');

        title.textContent = post.title || 'Post sem título';
        subtitle.textContent = post.subtitle || 'Sem subtítulo.';
        author.textContent = post.author ? `Autor: ${post.author}` : '';
        date.textContent = formatDate(post.localDateTime);
        edit.href = `/blog/?id=${post.id}`;

        deleteButton.addEventListener('click', () => deletePost(post.id, article));

        postsList.appendChild(clone);
    });
}

async function deletePost(postId, article) {
    const confirmed = window.confirm('Tem certeza que deseja excluir este post? Essa ação não pode ser desfeita.');
    if (!confirmed) return;

    const deleteButton = article.querySelector('.managed-post-delete');

    try {
        deleteButton.disabled = true;
        showMessage('Excluindo post...');

        const response = await fetch(`/blog/${postId}`, {
            method: 'DELETE',
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error(await readErrorMessage(response, 'Não foi possível excluir o post.'));
        }

        article.remove();
        showMessage('Post excluído com sucesso.', 'success');

        if (!postsList.querySelector('.managed-post')) {
            postsList.innerHTML = '<div class="empty-managed-posts">Nenhum post publicado para gerenciar.</div>';
        }
    } catch (error) {
        deleteButton.disabled = false;
        showMessage(error.message, 'error');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const userRole = typeof readCookie === 'function' ? readCookie('userType') : null;

    if (headerCreatePost && !['WRITER', 'ADMIN'].includes(userRole)) {
        headerCreatePost.remove();
    }

    loadManagedPosts();
});
