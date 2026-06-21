const profileForm = document.querySelector('#profileForm');
const photoInput = document.querySelector('#photoInput');
const bioInput = document.querySelector('#bio');
const bioCount = document.querySelector('#bioCount');
const saveBtn = document.querySelector('#saveBtn');
const message = document.querySelector('#profileMessage');
const previewImage = document.querySelector('#profilePreviewImage');
const previewName = document.querySelector('#profilePreviewName');
const previewBio = document.querySelector('#profilePreviewBio');

let selectedPhoto = null;

function normalizeImageUrl(url) {
    if (!url) return '/img/avatar-placeholder.svg';
    if (url.startsWith('/uploads/images/')) return url.replace('/uploads/images/', '/user/uploads/images/');
    return url;
}

function updateBioPreview() {
    const bio = bioInput.value.trim();
    bioCount.textContent = bioInput.value.length;
    previewBio.textContent = bio || 'Sua bio aparecerá aqui.';
}

function showMessage(text, type = 'info') {
    message.textContent = text;
    message.className = `profile-message profile-message--${type}`;
}

async function loadProfile() {
    try {
        const response = await fetch('/user/me', { credentials: 'include' });

        if (!response.ok) throw new Error('Não foi possível carregar seu perfil.');

        const profile = await response.json();
        previewName.textContent = profile.name || 'Seu nome';
        bioInput.value = profile.bio || '';
        previewImage.src = normalizeImageUrl(profile.profilePictureUrl);
        updateBioPreview();
    } catch (error) {
        showMessage(error.message, 'error');
    }
}

async function uploadPhoto() {
    if (!selectedPhoto) return;

    const formData = new FormData();
    formData.append('file', selectedPhoto);

    const response = await fetch('/user/profile-picture', {
        method: 'PATCH',
        credentials: 'include',
        body: formData
    });

    if (!response.ok) throw new Error('Não foi possível salvar a foto.');
}

async function saveProfile(event) {
    event.preventDefault();
    saveBtn.disabled = true;
    saveBtn.textContent = 'Salvando...';
    showMessage('Salvando alterações...');

    try {
        await uploadPhoto();

        const response = await fetch('/user/profile', {
            method: 'PATCH',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ bio: bioInput.value.trim() })
        });

        if (!response.ok) throw new Error('Não foi possível salvar a bio.');

        selectedPhoto = null;
        photoInput.value = '';
        await loadProfile();
        showMessage('Perfil atualizado com sucesso.', 'success');
    } catch (error) {
        showMessage(error.message, 'error');
    } finally {
        saveBtn.disabled = false;
        saveBtn.textContent = 'Salvar perfil';
    }
}

photoInput.addEventListener('change', () => {
    const [file] = photoInput.files;
    selectedPhoto = file || null;

    if (selectedPhoto) {
        previewImage.src = URL.createObjectURL(selectedPhoto);
    }
});

bioInput.addEventListener('input', updateBioPreview);
profileForm.addEventListener('submit', saveProfile);
document.addEventListener('DOMContentLoaded', loadProfile);
