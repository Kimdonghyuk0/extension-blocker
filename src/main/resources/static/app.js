// Skeleton wiring: confirm the backend is reachable.
// Policy management + upload logic are implemented per tech spec.
(async function checkHealth() {
  const el = document.getElementById('health');
  try {
    const res = await fetch('/api/health');
    if (!res.ok) throw new Error('HTTP ' + res.status);
    const data = await res.json();
    el.textContent = `${data.status} (seeded: ${data.seededExtensions})`;
  } catch (err) {
    el.textContent = '연결 실패 — ' + err.message;
  }
})();
