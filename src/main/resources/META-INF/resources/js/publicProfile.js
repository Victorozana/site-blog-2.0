const params = new URLSearchParams(window.location.search);
const id = params.get('id');
const publicImage = document.querySelector('#publicProfileImage');
const publicName = document.querySelector('#publicProfileName');
const publicBio = document.querySelector('#publicProfileBio');

function normalizeImageUrl(url) {
    if (!url) return '/img/avatar-placeholder.svg';
    if (url.startsWith('/uploads/images/')) return url.replace('/uploads/images/', '/user/uploads/images/');
    return url;
}

async function loadPublicProfile() {
    if (!id) {
        publicName.textContent = 'Perfil não encontrado';
        publicBio.textContent = 'Nenhum autor foi informado.';
        return;
    }

    try {
        const response = await fetch(`/profile/data?id=${id}`);

        if (!response.ok) throw new Error('Não foi possível carregar este perfil.');

        const profile = await response.json();
        publicImage.src = normalizeImageUrl(profile.profilePictureUrl);
        publicName.textContent = profile.name || 'Autor';
        publicBio.textContent = profile.bio || 'Este autor ainda não escreveu uma bio.';
    } catch (error) {
        publicName.textContent = 'Perfil indisponível';
        publicBio.textContent = error.message;
    }
}

document.addEventListener('DOMContentLoaded', loadPublicProfile);
