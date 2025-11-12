function showError(message, redirectUrl = null, timeout = 4000, redirect = false) {
  // Remove modal anterior se existir
  const oldModal = document.querySelector(".error-modal-overlay");
  if (oldModal) oldModal.remove();

  // Cria o fundo de escurecimento
  const overlay = document.createElement("div");
  overlay.className = "error-modal-overlay";
  overlay.innerHTML = `
    <div class="error-modal">
      <h2>⚠️ Acesso negado</h2>
      <p>${message}</p>
      <button id="error-modal-btn">Entendido</button>
    </div>
  `;

  document.body.appendChild(overlay);

  // ===== Estilo visual (CSS injetado dinamicamente) =====
  const style = document.createElement("style");
  style.innerHTML = `
    .error-modal-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.6);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 9999;
      animation: fadeIn 0.3s ease-in-out;
    }

    .error-modal {
      background: #ffffff;
      padding: 30px 40px;
      border-radius: 12px;
      text-align: center;
      box-shadow: 0 0 15px rgba(0,0,0,0.3);
      max-width: 400px;
      width: 90%;
      animation: slideUp 0.4s ease;
    }

    .error-modal h2 {
      color: #d9534f;
      margin-bottom: 15px;
    }

    .error-modal p {
      color: #333;
      margin-bottom: 25px;
      font-size: 16px;
      line-height: 1.4;
    }

    .error-modal button {
      background: #d9534f;
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 6px;
      font-weight: bold;
      cursor: pointer;
      transition: background 0.3s;
    }

    .error-modal button:hover {
      background: #c9302c;
    }

    @keyframes fadeIn {
      from { opacity: 0; }
      to { opacity: 1; }
    }

    @keyframes slideUp {
      from { transform: translateY(30px); opacity: 0; }
      to { transform: translateY(0); opacity: 1; }
    }
  `;
  document.head.appendChild(style);

  // ===== Fechamento manual =====
  const btn = overlay.querySelector("#error-modal-btn");
  btn.addEventListener("click", () => fecharModal());

  // ===== Fechamento automático =====
  setTimeout(() => {
    fecharModal();
    if (redirect && redirectUrl) {
      window.location.href = redirectUrl;
    }
  }, timeout);

  // ===== Função auxiliar =====
  function fecharModal() {
    overlay.style.opacity = "0";
    overlay.style.transition = "opacity 0.3s";
    setTimeout(() => overlay.remove(), 300);
  }
}


function checkAuth() {
  const token = localStorage.getItem("token");

  if (!token) {
    showError("Você precisa estar logado para acessar esta página.", "/src/main/resources/static/pages/login.html", 5000, true);
    return false;
  }

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expirationDate = new Date(payload.exp * 1000);

    if (expirationDate < new Date()) {
      localStorage.removeItem("token");
      showError("Sua sessão expirou. Faça login novamente.", "/src/main/resources/static/pages/login.html", 5000, true);
      return false;
    }
  } catch (err) {
    console.error("Token inválido:", err);
    localStorage.removeItem("token");
    showError("Sessão inválida. Faça login novamente.", "/src/main/resources/static/pages/login.html", 5000, true);
    return false;
  }

  return true;
}


function checkRole(requiredRole) {
  const rolesData = JSON.parse(localStorage.getItem("roles")) || [];
  const roleNames = rolesData.map(r => r.nome);

  if (!roleNames.includes(requiredRole)) {
    showError("Você não tem permissão para acessar esta página.", "/src/main/resources/static/pages/login.html", 5000, true);
    return false;
  }

  return true;
}

function validarAcesso(rolesNecessarias = []) {
  if (!checkAuth()) return false;

  const rolesData = JSON.parse(localStorage.getItem("roles")) || [];
  const roleNames = rolesData.map(r => r.nome);

  if (rolesNecessarias.length > 0) {
    const autorizado = roleNames.some(role => rolesNecessarias.includes(role));

    if (!autorizado) {
      showError("Você não tem permissão para acessar esta página.", "/src/main/resources/static/pages/login.html", 5000, true);
      return false;
    }
  }

  return true;
}
