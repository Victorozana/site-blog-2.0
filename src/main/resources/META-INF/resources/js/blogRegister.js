console.log("=== JavaScript de Criação de Posts Carregado ===");

// 1. SELETORES DO DOM
const formBlogRegister = document.querySelector('#formBlogRegister');
const btnPublicar = document.querySelector('#btnPublicar');
const mensagemDiv = document.querySelector('#mensagem');

// 2. VALIDAÇÃO E ENVIO DO FORMULÁRIO
formBlogRegister.addEventListener('submit', async (event) => {
    event.preventDefault();
    
    // Capturar dados do formulário
    const title = document.querySelector('#title').value.trim();
    const subtitle = document.querySelector('#subtitle').value.trim();
    const category = document.querySelector('#category').value;
    const description = document.querySelector('#description').value.trim();

    // 3. VALIDAÇÕES
    if (!title) {
        mostrarMensagem('Por favor, preencha o título!', 'danger');
        return;
    }
    if (!category) {
        mostrarMensagem('Por favor, selecione uma categoria!', 'danger');
        return;
    }
    if (!description || description.length < 20) {
        mostrarMensagem('O conteúdo deve ter pelo menos 20 caracteres!', 'danger');
        return;
    }

    // 4. PREPARAR DADOS PARA ENVIO
    const blogData = {
        title: title,
        subtitle: subtitle,
        category: category,
        description: description
    };

    // 5. CHAMAR API
    try {
        btnPublicar.disabled = true;
        btnPublicar.textContent = 'Publicando...';

        const response = await fetch('/register/blog', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify(blogData)
        });

        if (response.ok) {
            const resultado = await response.json();
            
            mostrarMensagem('Post publicado com sucesso!', 'success');
            
            // Limpar formulário
            formBlogRegister.reset();
            limparRascunho();
            
            // Redirecionar após 2 segundos
            setTimeout(() => {
                window.location.href = '/';
            }, 2000);
        } else {
            const erro = await response.json();
            mostrarMensagem(`Erro ao publicar: ${erro.message || 'Erro desconhecido'}`, 'danger');
        }
    } catch (error) {
        console.error('Erro na requisição:', error);
        mostrarMensagem(`Erro de conexão: ${error.message}`, 'danger');
    } finally {
        btnPublicar.disabled = false;
        btnPublicar.textContent = 'Publicar Post';
    }
});

// 6. FUNÇÃO DE MENSAGEM COM ANIMAÇÃO
function mostrarMensagem(texto, tipo = 'info') {
    mensagemDiv.className = `alert alert-${tipo} d-block`;
    mensagemDiv.textContent = texto;
    
    // Auto-hide após 5 segundos
    setTimeout(() => {
        mensagemDiv.classList.add('d-none');
    }, 5000);
}

// 7. LIMPAR RASCUNHO AO PUBLICAR COM SUCESSO
function limparRascunho() {
    localStorage.removeItem('blogRascunho');
}

// 8. AUTO-SAVE EM LOCALSTORAGE (opcional - salvar rascunho)
formBlogRegister.addEventListener('input', () => {
    const rascunho = {
        title: document.querySelector('#title').value,
        subtitle: document.querySelector('#subtitle').value,
        category: document.querySelector('#category').value,
        description: document.querySelector('#description').value,
        timestamp: new Date().toISOString()
    };
    
    localStorage.setItem('blogRascunho', JSON.stringify(rascunho));
    console.log('Rascunho salvo automaticamente');
});

// 9. RESTAURAR RASCUNHO AO CARREGAR A PÁGINA
document.addEventListener('DOMContentLoaded', () => {
    const rascunho = localStorage.getItem('blogRascunho');
    
    if (rascunho) {
        const dados = JSON.parse(rascunho);
        
        // Verificar se o rascunho tem menos de 1 hora
        const tempoDecorrido = (new Date() - new Date(dados.timestamp)) / (1000 * 60);
        
        if (tempoDecorrido < 60) {
            console.log('Restaurando rascunho...');
            document.querySelector('#title').value = dados.title;
            document.querySelector('#subtitle').value = dados.subtitle;
            document.querySelector('#category').value = dados.category;
            document.querySelector('#description').value = dados.description;
            
            mostrarMensagem('Rascunho restaurado!', 'info');
        }
    }
});
