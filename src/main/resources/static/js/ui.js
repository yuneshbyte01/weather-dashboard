document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('form.search-form');
  if (!form) return;

  const input = form.querySelector('#city-input');
  const submitBtn = form.querySelector('button[type="submit"]');

  // Inline status element
  const status = document.createElement('div');
  status.setAttribute('role', 'status');
  status.className = 'subtle mt-2';
  status.style.minHeight = '1.25rem';
  form.appendChild(status);

  const setBusy = (busy, message = '') => {
    form.setAttribute('aria-busy', busy ? 'true' : 'false');
    submitBtn.disabled = !!busy;
    if (busy) {
      submitBtn.dataset.label = submitBtn.textContent.trim();
      submitBtn.textContent = 'Searching…';
    } else {
      submitBtn.textContent = submitBtn.dataset.label || 'Search';
    }
    status.textContent = message;
    status.className = `subtle mt-2 ${busy ? 'status-success' : ''}`;
  };

  form.addEventListener('submit', (e) => {
    const value = (input.value || '').trim();
    if (!value) {
      e.preventDefault();
      status.textContent = 'Please enter a city name.';
      status.className = 'subtle mt-2 status-error';
      input.focus();

      // Shake animation
      form.classList.add('shake');
      setTimeout(() => form.classList.remove('shake'), 200);

      return false;
    }
    setBusy(true, 'Fetching current weather…');
  });

  // Focus map if a city query exists
  const url = new URL(window.location.href);
  if (url.searchParams.get('city')) {
    const map = document.getElementById('map');
    if (map) {
      map.setAttribute('tabindex', '-1');
      setTimeout(() => map.focus({ preventScroll: true }), 600);
    }
  }
});
