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
let temporaryPreviewUrl = null;

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

function applyProfile(profile) {
    previewName.textContent = profile.name || 'Seu nome';
    bioInput.value = profile.bio || '';
    previewImage.src = normalizeImageUrl(profile.profilePictureUrl);
    updateBioPreview();
}

async function loadProfile() {
    try {
        const response = await fetch('/user/me', { credentials: 'include' });

        if (!response.ok) throw new Error('Não foi possível carregar seu perfil.');

        applyProfile(await response.json());
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

    return response.json();
}

async function saveProfile(event) {
    event.preventDefault();
    saveBtn.disabled = true;
    saveBtn.textContent = 'Salvando...';
    showMessage('Salvando alterações...');

    try {
        const photoProfile = await uploadPhoto();

        const response = await fetch('/user/profile', {
            method: 'PATCH',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ bio: bioInput.value.trim() })
        });

        if (!response.ok) throw new Error('Não foi possível salvar a bio.');

        selectedPhoto = null;
        photoInput.value = '';
        applyProfile(await response.json());

        if (photoProfile) {
            previewImage.src = normalizeImageUrl(photoProfile.profilePictureUrl);
        }

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
        if (temporaryPreviewUrl) {
            URL.revokeObjectURL(temporaryPreviewUrl);
        }

        temporaryPreviewUrl = URL.createObjectURL(selectedPhoto);
        previewImage.src = temporaryPreviewUrl;
    }
});

bioInput.addEventListener('input', updateBioPreview);
profileForm.addEventListener('submit', saveProfile);
document.addEventListener('DOMContentLoaded', loadProfile);
