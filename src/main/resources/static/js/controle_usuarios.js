document.addEventListener("DOMContentLoaded", () => {
  if (!validarAcesso(["ROLE_CONTROLE_USUARIO"])) return;

  const campoPesquisa = document.getElementById("campo-pesquisa");
  const tipoPesquisa = document.getElementById("tipo-pesquisa");
  const btnPesquisar = document.getElementById("btn-pesquisar");
  const resultadoDiv = document.getElementById("resultado-usuario");

  btnPesquisar.addEventListener("click", async () => {
    const valor = campoPesquisa.value.trim();
    const tipo = tipoPesquisa.value;
    if (!valor) return mostrarPopupErro("Digite o valor para pesquisar.");

    const token = localStorage.getItem("token");
    let url = "";

    // Define o endpoint correto com base no tipo selecionado
    switch (tipo) {
      case "nome":
        url = `${API_URL_USER}/user/pesquisar/nome?nome=${encodeURIComponent(valor)}`;
        break;
      case "documento":
        url = `${API_URL_USER}/user/pesquisar/documento?documento=${encodeURIComponent(valor)}`;
        break;
      case "email":
        url = `${API_URL_USER}/user/pesquisar/mail?mail=${encodeURIComponent(valor)}`;
        break;
      default:
        return mostrarPopupErro("Tipo de pesquisa inválido.");
    }

    try {
      const response = await fetch(url, {
        headers: { "Authorization": `Bearer ${token}` }
      });

      if (!response.ok) {
        if (response.status === 404) throw new Error("Usuário não encontrado.");
        throw new Error("Erro ao buscar usuário.");
      }

      const usuario = await response.json();

      resultadoDiv.innerHTML = `
        <h3>${usuario.nome}</h3>
        <p><strong>E-mail:</strong> ${usuario.mail}</p>
        <p><strong>Documento:</strong> ${formatarCPF(usuario.documento_pessoal)}</p>
        <button id="btn-permissoes">Gerenciar Permissões</button>
      `;

      document.getElementById("btn-permissoes").addEventListener("click", () => abrirModalPermissoes(usuario));

    } catch (err) {
      resultadoDiv.innerHTML = `<p style="color:red;">${err.message}</p>`;
    }
  });
});


async function abrirModalPermissoes(usuario) {
  const modal = document.getElementById("modal-permissoes");
  const listaDiv = document.getElementById("lista-permissoes");
  const token = localStorage.getItem("token");

  modal.classList.remove("hidden");
  listaDiv.innerHTML = "<p>Carregando permissões...</p>";

  try {
    const [todasResp, usuarioResp] = await Promise.all([
      fetch(`${API_URL_USER}/user/role/pesquisar/todas`, { headers: { "Authorization": `Bearer ${token}` } }),
      fetch(`${API_URL_USER}/user/role/pesquisar/id?id=${usuario.id}`, { headers: { "Authorization": `Bearer ${token}` } })
    ]);

    const todas = await todasResp.json();
    const atuais = await usuarioResp.json();
    const atuaisIds = atuais.map(r => r.id);

    listaDiv.innerHTML = todas.map(role => `
      <div class="permissao-item">
        <span>${role.nome}</span>
        <label class="switch">
          <input type="checkbox" value="${role.id}" ${atuaisIds.includes(role.id) ? "checked" : ""}>
          <span class="slider"></span>
        </label>
      </div>
    `).join("");

    document.getElementById("btn-salvar-roles").onclick = () => salvarPermissoes(usuario.id);
    document.getElementById("btn-cancelar-roles").onclick = () => modal.classList.add("hidden");

  } catch (e) {
    listaDiv.innerHTML = `<p style="color:red;">Erro ao carregar permissões</p>`;
  }
}

async function salvarPermissoes(idUsuario) {
  const modal = document.getElementById("modal-permissoes");
  const token = localStorage.getItem("token");

  const selecionadas = Array.from(document.querySelectorAll("#lista-permissoes input:checked"))
    .map(chk => parseInt(chk.value));

  try {
    const resp = await fetch(`${API_URL_USER}/user/role/alterar`, {
      method: "PUT",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ id: idUsuario, idsRoles: selecionadas })
    });

    if (!resp.ok) throw new Error("Erro ao salvar permissões.");
    mostrarPopupSucesso("Permissões atualizadas com sucesso!");
    modal.classList.add("hidden");
  } catch (err) {
    mostrarPopupErro("Falha ao salvar permissões.");
  }
}

function formatarCPF(cpf) {
  cpf = cpf.replace(/\D/g, "");
  cpf = cpf.slice(0, 11);
  if (cpf.length <= 3) return cpf;
  if (cpf.length <= 6) return cpf.replace(/(\d{3})(\d+)/, "$1.$2");
  if (cpf.length <= 9) return cpf.replace(/(\d{3})(\d{3})(\d+)/, "$1.$2.$3");
  return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, "$1.$2.$3-$4");
}
