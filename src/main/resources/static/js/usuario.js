document.addEventListener("DOMContentLoaded", async () => {
  if (!validarAcesso(["ROLE_USUARIO"])) return;

  const usuarioCard = document.getElementById("usuario-card");
  const token = localStorage.getItem("token");
  const userId = localStorage.getItem("id");

  if (!token || !userId) {
    usuarioCard.innerHTML = `<p>Acesso negado. Faça login novamente.</p>`;
    setTimeout(() => window.location.href = "/src/main/resources/static/pages/login.html", 2000);
    return;
  }

  try {
    const resp = await fetch(`${API_URL_USER}/user/pesquisar/id?id=${userId}`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (!resp.ok) throw new Error(`Erro ao carregar usuário (${resp.status})`);
    const usuario = await resp.json();

    usuarioCard.innerHTML = `
      <div class="usuario-header">
        <h2>Dados do Usuário</h2>
        <button id="btn-editar-usuario" class="usuario-edit-btn" title="Editar">
          <img src="/src/main/resources/static/img/edit.svg" alt="Editar" class="icon-btn">
        </button>
      </div>

      <div><strong>Nome:</strong> <span>${escapeHtml(usuario.nome)}</span></div>
      <div><strong>Email:</strong> <span>${escapeHtml(usuario.mail)}</span></div>
      <div><strong>Documento:</strong> <span>${formatarCPF(usuario.documento_pessoal)}</span></div>
      <div><strong>Telefone:</strong> <span>${formatarTelefone(usuario.telefone)}</span></div>
    `;

    document.getElementById("btn-editar-usuario").addEventListener("click", () => abrirModalEditar(usuario));

  } catch (error) {
    console.error(error);
    usuarioCard.innerHTML = `<p>Erro ao carregar dados do usuário.</p>`;
  }
});

function abrirModalEditar(usuario) {
  const modal = document.createElement("div");
  modal.className = "usuario-modal";
  modal.innerHTML = `
    <div class="usuario-modal-content">
      <h2>Editar dados</h2>
      <label>Nome:</label>
      <input type="text" id="edit-nome" value="${usuario.nome || ''}">
      <label>Email:</label>
      <input type="email" id="edit-mail" value="${usuario.mail || ''}">
      <label>Documento (CPF):</label>
      <input type="text" id="edit-documento" value="${formatarCPF(usuario.documento_pessoal || '')}">
      <label>Telefone:</label>
      <input type="text" id="edit-telefone" value="${formatarTelefone(usuario.telefone || '')}">
      <div class="usuario-modal-actions">
        <button id="save-edit">Salvar</button>
        <button id="cancel-edit">Cancelar</button>
      </div>
    </div>
  `;
  document.body.appendChild(modal);

  document.getElementById("edit-documento").addEventListener("input", e => {
    e.target.value = formatarCPF(e.target.value);
  });
  document.getElementById("edit-telefone").addEventListener("input", e => {
    e.target.value = formatarTelefone(e.target.value);
  });

  document.getElementById("cancel-edit").addEventListener("click", () => modal.remove());
  document.getElementById("save-edit").addEventListener("click", async () => {
    await salvarAlteracoes(usuario.id);
    modal.remove();
  });
}

async function salvarAlteracoes(id) {
  const token = localStorage.getItem("token");
  const putUserDTO = {
    id,
    nome: document.getElementById("edit-nome").value.trim(),
    mail: document.getElementById("edit-mail").value.trim(),
    documento_pessoal: document.getElementById("edit-documento").value.replace(/\D/g, ""), 
    telefone: document.getElementById("edit-telefone").value.replace(/\D/g, "") 
  };

  try {
    const resp = await fetch(`${API_URL_USER}/user/alterar`, {
      method: "PUT",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(putUserDTO)
    });

    const texto = await resp.text();
    if (!resp.ok) {
      if (texto.includes("Documento já cadastrado")) {
        mostrarPopupErro("Este CPF já está cadastrado em outro usuário.");
      } else {
        mostrarPopupErro("Erro ao salvar alterações do usuário.");
      }
      throw new Error(texto || "Erro ao salvar dados");
    }

    mostrarPopupSucesso("Dados atualizados com sucesso!");
    setTimeout(() => location.reload(), 2500);

  } catch (error) {
    console.error("Erro ao salvar dados:", error);
  }
}

function escapeHtml(str) {
  if (!str) return "";
  return str
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}

function formatarCPF(cpf) {
  cpf = cpf.replace(/\D/g, ""); 
  cpf = cpf.slice(0, 11); 
  if (cpf.length <= 3) return cpf;
  if (cpf.length <= 6) return cpf.replace(/(\d{3})(\d+)/, "$1.$2");
  if (cpf.length <= 9) return cpf.replace(/(\d{3})(\d{3})(\d+)/, "$1.$2.$3");
  return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, "$1.$2.$3-$4");
}

function formatarTelefone(telefone) {
  telefone = telefone.replace(/\D/g, ""); 
  telefone = telefone.slice(0, 11); 
  if (telefone.length <= 2) return `(${telefone}`;
  if (telefone.length <= 6) return telefone.replace(/(\d{2})(\d+)/, "($1) $2");
  if (telefone.length <= 10) return telefone.replace(/(\d{2})(\d{4})(\d+)/, "($1) $2-$3");
  return telefone.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
}

document.getElementById("documento").addEventListener("input", (e) => {
  e.target.value = formatarCPF(e.target.value);
});

document.getElementById("telefone").addEventListener("input", (e) => {
  e.target.value = formatarTelefone(e.target.value);
});

