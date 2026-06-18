// Mocked profile persistence using localStorage. Save all updatable fields every time 'Salvar' is pressed.
const STORAGE_KEY = 'mockProfile';

function readMockProfile(){
  try{ return JSON.parse(localStorage.getItem(STORAGE_KEY)) || {} }catch(e){ return {} }
}

function writeMockProfile(profile){
  localStorage.setItem(STORAGE_KEY, JSON.stringify(profile));
}

// UI helpers
const el = id => document.getElementById(id);
let photos = []; // array of data URLs

function renderPhotos(){
  const list = el('photoList');
  list.innerHTML = '';
  photos.forEach((src, idx) => {
    const div = document.createElement('div');
    div.className = 'photo-item';
    const img = document.createElement('img'); img.src = src; div.appendChild(img);
    const btn = document.createElement('button'); btn.className = 'remove'; btn.textContent = '×';
    btn.title = 'Remover foto';
    btn.addEventListener('click', ()=>{ removePhoto(idx) });
    div.appendChild(btn);
    list.appendChild(div);
  });
  if(photos.length===0){ list.textContent = 'Nenhuma foto adicionada.' }
}

function removePhoto(index){ photos.splice(index,1); renderPhotos(); }

function addFiles(files){
  const arr = Array.from(files);
  if(arr.length===0) return;
  arr.forEach(file => {
    const reader = new FileReader();
    reader.onload = (e) => { photos.push(e.target.result); renderPhotos(); };
    reader.readAsDataURL(file);
  });
}

function loadForm(){
  const data = readMockProfile();
  el('name').value = data.name || '';
  el('email').value = data.email || '';
  el('bio').value = data.bio || '';
  photos = (data.photos && Array.isArray(data.photos)) ? data.photos.slice() : [];
  renderPhotos();
  showResult('Dados carregados (mock).');
}

function showResult(msg){ el('result').textContent = typeof msg === 'string' ? msg : JSON.stringify(msg, null, 2); }

function collectForm(){
  // Collect all updatable columns and always save them (even if unchanged)
  return {
    id: readMockProfile().id || generateId(),
    name: el('name').value.trim(),
    email: el('email').value.trim(),
    bio: el('bio').value.trim(),
    photos: photos.slice(),
    updatedAt: new Date().toISOString()
  };
}

function generateId(){ // simple mock id
  return 'u_' + Math.random().toString(36).slice(2,10);
}

function mockSave(profile){
  // simulate network delay and always persist all fields
  showResult('Salvando...');
  return new Promise(resolve => setTimeout(()=>{
    writeMockProfile(profile);
    resolve(profile);
  }, 700));
}

async function onSave(){
  const profile = collectForm();
  try{
    const saved = await mockSave(profile);
    showResult(saved);
    // reflect saved state into form (id/update timestamp)
    loadForm();
  }catch(e){ showResult('Erro ao salvar: '+e.message) }
}

function onClear(){
  photos = []; renderPhotos();
  el('name').value=''; el('email').value=''; el('bio').value='';
  localStorage.removeItem(STORAGE_KEY);
  showResult('Dados locais removidos.');
}

// Init
ndocument.addEventListener('DOMContentLoaded', ()=>{
  el('photoInput').addEventListener('change', (e)=> addFiles(e.target.files));
  el('saveBtn').addEventListener('click', onSave);
  el('clearBtn').addEventListener('click', onClear);
  loadForm();
});
