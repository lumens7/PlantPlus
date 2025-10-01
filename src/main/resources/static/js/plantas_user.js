const API_URL_USER = "http://localhost:8080/api/plant/user";
const ID_USUARIO = 1; // 🔹 depois você troca pelo usuário logado

document.addEventListener("DOMContentLoaded", () => {
  montarModalUser();
  carregarPlantasUser();

  // Toggle da pesquisa
  document.getElementById("btn-search").addEventListener("click", () => {
    const searchArea = document.getElementById("search-area");
    searchArea.style.display = searchArea.style.display === "none" ? "flex" : "none";
  });

  // Botão de pesquisar
  document.getElementById("btn-do-search").addEventListener("click", pesquisarPlantasUser);
});

// ======================
// MONTA MODAL
// ======================
function montarModalUser() {
  const modal = document.createElement("div");
  modal.id = "modal-cadastro-user";
  modal.className = "modal";
  modal.innerHTML = `
    <div class="modal-content">
      <h2>CADASTRO PLANTA USER</h2>

      <input type="hidden" id="id">

      <label for="id_plantaCie">ID PLANTA CIE</label>
      <input type="number" id="id_plantaCie" placeholder="id da planta cie...">

      <label for="quantidade">QUANTIDADE</label>
      <input type="number" id="quantidade" placeholder="quantidade...">

      <label for="idade">IDADE (meses)</label>
      <input type="number" id="idade" placeholder="idade em meses...">

      <button id="btn-salvar">SALVAR</button>
      <button id="btn-fechar">FECHAR</button>
    </div>
  `;
  document.body.appendChild(modal);

  // Bind eventos
  document.getElementById("btn-add").addEventListener("click", abrirModalNovoUser);
  document.getElementById("btn-fechar").addEventListener("click", () => (modal.style.display = "none"));
  document.getElementById("btn-salvar").addEventListener("click", salvarPlantaUser);
}

// ======================
// CARREGAR LISTA
// ======================
async function carregarPlantasUser() {
  try {
    const response = await fetch(`${API_URL_USER}/pesquisar/plant_user?id_plant_user=${ID_USUARIO}`);
    if (!response.ok) throw new Error("Erro ao buscar plantas do usuário");

    const plantas = await response.json();
    renderizarListaUser(plantas);
  } catch (error) {
    console.error("Erro:", error);
  }
}

// ======================
// PESQUISA
// ======================
async function pesquisarPlantasUser() {
  const metodo = document.getElementById("search-method").value;
  const valor = document.getElementById("search-value").value.trim();

  if (!valor) {
    alert("Digite um valor para pesquisar!");
    return;
  }

  let url = "";
  if (metodo === "nome") {
    url = `${API_URL_USER}/pesquisar/nome?nome=${encodeURIComponent(valor)}`;
  } else if (metodo === "id") {
    url = `${API_URL_USER}/pesquisar/plant_user?id_plant_user=${encodeURIComponent(valor)}`;
  }

  try {
    const response = await fetch(url);
    if (!response.ok) throw new Error("Erro na pesquisa");

    const plantas = await response.json();
    renderizarListaUser(plantas);
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao pesquisar plantas");
  }
}

// ======================
// RENDERIZA LISTA
// ======================
function renderizarListaUser(plantas) {
  const lista = document.getElementById("plantas-user-list");
  lista.innerHTML = "";

  plantas.forEach(planta => {
    const card = document.createElement("div");
    card.className = "planta-card";
    card.innerHTML = `
      <div>
        <h2>ID: ${planta.id}</h2>
        <div class="dados-plant">
          <p>Planta CIE: ${planta.plantaCie ? planta.plantaCie.nomeCientifico : "N/A"}</p>
          <p>Quantidade: ${planta.quantidade}</p>
          <p>Idade: ${planta.idade} meses</p>
        </div>
      </div>
      <div class="card-actions">
        <button class="btn-editar" data-id="${planta.id}" title="Editar">
          <img src="/src/main/resources/static/img/edit.svg" alt="Editar" class="icon-btn">
        </button>
        <button class="btn-excluir" data-id="${planta.id}" title="Excluir">
          <img src="/src/main/resources/static/img/delete.svg" alt="Excluir" class="icon-btn">
        </button>
      </div>
    `;
    lista.appendChild(card);
  });

  document.querySelectorAll(".btn-editar").forEach(btn =>
    btn.addEventListener("click", () => abrirModalEdicaoUser(btn.dataset.id))
  );
  document.querySelectorAll(".btn-excluir").forEach(btn =>
    btn.addEventListener("click", () => deletarPlantaUser(btn.dataset.id))
  );
}

// ======================
// MODAL - NOVO
// ======================
function abrirModalNovoUser() {
  document.querySelectorAll("#modal-cadastro-user input").forEach(el => (el.value = ""));
  document.getElementById("modal-cadastro-user").style.display = "flex";
}

// ======================
// MODAL - EDITAR
// ======================
async function abrirModalEdicaoUser(id) {
  try {
    const response = await fetch(`${API_URL_USER}/pesquisar/plant_user?id_plant_user=${ID_USUARIO}`);
    const plantas = await response.json();
    const planta = plantas.find(p => p.id == id);

    if (!planta) return;

    document.getElementById("id").value = planta.id;
    document.getElementById("id_plantaCie").value = planta.plantaCie ? planta.plantaCie.id : "";
    document.getElementById("quantidade").value = planta.quantidade || "";
    document.getElementById("idade").value = planta.idade || "";

    document.getElementById("modal-cadastro-user").style.display = "flex";
  } catch (error) {
    console.error("Erro:", error);
  }
}

// ======================
// SALVAR
// ======================
async function salvarPlantaUser() {
  const plantaUser = {
    id: document.getElementById("id").value || null,
    quantidade: document.getElementById("quantidade").value,
    idade: document.getElementById("idade").value,
    id_usuario: ID_USUARIO,
    id_plantaCie: document.getElementById("id_plantaCie").value
  };

  try {
    const url = plantaUser.id ? `${API_URL_USER}/alterar` : `${API_URL_USER}/cadastrar`;
    const method = plantaUser.id ? "PUT" : "POST";

    const response = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(plantaUser)
    });

    if (!response.ok) throw new Error("Erro ao salvar planta do usuário");

    alert(plantaUser.id ? "Planta atualizada com sucesso!" : "Planta cadastrada com sucesso!");
    document.getElementById("modal-cadastro-user").style.display = "none";
    carregarPlantasUser();
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao salvar planta do usuário");
  }
}

// ======================
// DELETAR
// ======================
async function deletarPlantaUser(id) {
  if (!confirm("Deseja realmente excluir esta planta?")) return;

  try {
    const response = await fetch(`${API_URL_USER}/deletar?id=${id}&idUsuario=${ID_USUARIO}`, { method: "DELETE" });
    if (!response.ok) throw new Error("Erro ao excluir planta");

    alert("Planta excluída com sucesso!");
    carregarPlantasUser();
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao excluir planta do usuário");
  }
}
