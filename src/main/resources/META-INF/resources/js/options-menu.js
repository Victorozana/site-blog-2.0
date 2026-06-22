document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.options-menu').forEach((menu, i) => {
    const toggle = menu.querySelector('.options-toggle');
    const dropdown = menu.querySelector('.options-dropdown');
    const id = dropdown.id || `options-list-${i}`;
    dropdown.id = id;
    toggle.setAttribute('aria-controls', id);

    const closeMenu = () => {
      toggle.setAttribute('aria-expanded', 'false');
      dropdown.hidden = true;
      dropdown.setAttribute('aria-hidden', 'true');
    };

    const openMenu = () => {
      toggle.setAttribute('aria-expanded', 'true');
      dropdown.hidden = false;
      dropdown.setAttribute('aria-hidden', 'false');
      const first = dropdown.querySelector('a,button');
      if (first) first.focus();
    };

    toggle.addEventListener('click', (e) => {
      const open = toggle.getAttribute('aria-expanded') === 'true';
      if (open) closeMenu(); else openMenu();
    });

    document.addEventListener('click', (e) => {
      if (!menu.contains(e.target)) closeMenu();
    });

    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') {
        if (dropdown && !dropdown.hidden) {
          closeMenu();
          toggle.focus();
        }
      }
    });

    dropdown.addEventListener('keydown', (e) => {
      const items = Array.from(dropdown.querySelectorAll('a,button'));
      const idx = items.indexOf(document.activeElement);
      if (e.key === 'ArrowDown') { e.preventDefault(); items[(idx+1) % items.length].focus(); }
      else if (e.key === 'ArrowUp') { e.preventDefault(); items[(idx-1+items.length) % items.length].focus(); }
      else if (e.key === 'Home') { e.preventDefault(); items[0].focus(); }
      else if (e.key === 'End') { e.preventDefault(); items[items.length-1].focus(); }
    });

    const logoutBtn = menu.querySelector('#logout-btn');
    if (logoutBtn) {
      logoutBtn.addEventListener('click', () => {
        if (window.logoutUser) window.logoutUser();
        else window.location.href = '/logout';
      });
    }

    const userRole = typeof readCookie === 'function' ? readCookie('userType') : null;
    const createPostItem = menu.querySelector('#menuCreatePost');
    const managePostsItem = menu.querySelector('#menuManagePosts');
    const adminItem = menu.querySelector('#menuAdmin');

    if (createPostItem && !['WRITER', 'ADMIN'].includes(userRole)) {
      createPostItem.style.display = 'none';
    }

    if (managePostsItem && !['WRITER', 'ADMIN'].includes(userRole)) {
      managePostsItem.style.display = 'none';
    }

    if (adminItem && userRole !== 'ADMIN') {
      adminItem.style.display = 'none';
    }
  });
});
