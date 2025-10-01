const API_URL = "http://localhost:8080/api/plant/cie";

document.addEventListener("DOMContentLoaded", () => {
  montarModal();
  carregarPlantas();

  // Toggle da pesquisa
  document.getElementById("btn-search").addEventListener("click", () => {
    const searchArea = document.getElementById("search-area");
    searchArea.style.display = searchArea.style.display === "none" ? "flex" : "none";
  });

  // Botão de pesquisar
  document.getElementById("btn-do-search").addEventListener("click", pesquisarPlantas);
});

// ======================
// MONTA MODAL
// ======================
function montarModal() {
  const modal = document.createElement("div");
  modal.id = "modal-cadastro";
  modal.className = "modal";
  modal.innerHTML = `
    <div class="modal-content">
      <h2>CADASTRO PLANTA CIE</h2>

      <input type="hidden" id="id">
      
      <label for="nome">NOME PLANTA</label>
      <input type="text" id="nome" placeholder="nome planta...">

      <label for="nomeCientifico">NOME CIENTÍFICO</label>
      <input type="text" id="nomeCientifico" placeholder="nome cientifico...">

      <label for="especie">ESPÉCIE</label>
      <input type="text" id="especie" placeholder="espécie...">

      <label for="rega">REGA</label>
      <input type="text" id="rega" placeholder="rega...">

      <label for="poda">PODA</label>
      <input type="text" id="poda" placeholder="poda...">

      <label for="resumoDadosPlanta">RESUMO</label>
      <input type="text" id="resumoDadosPlanta" placeholder="resumo da planta...">

      <label for="urlFoto">URL FOTO</label>
      <div class="input-img">
        <input type="text" id="urlFoto" placeholder="url foto...">
        <img id="preview-img" src="/src/main/resources/static/img/img.svg" alt="preview" />
      </div>

      <button id="btn-salvar">SALVAR</button>
      <button id="btn-fechar">FECHAR</button>
    </div>
  `;
  document.body.appendChild(modal);

  // Bind eventos
  const btnAdd = document.getElementById("btn-add");
  const btnSalvar = document.getElementById("btn-salvar");
  const btnFechar = document.getElementById("btn-fechar");
  const urlFotoInput = document.getElementById("urlFoto");
  const previewImg = document.getElementById("preview-img");

  btnAdd.addEventListener("click", abrirModalNovo);
  btnFechar.addEventListener("click", () => (modal.style.display = "none"));
  urlFotoInput.addEventListener("input", () => {
    previewImg.src = urlFotoInput.value.trim() || "/src/main/resources/static/img/img.svg";
  });
  btnSalvar.addEventListener("click", salvarPlanta);
}

// ======================
// CARREGAR LISTA
// ======================
async function carregarPlantas() {
  try {
    const response = await fetch(`${API_URL}/pesquisar/todos`);
    if (!response.ok) throw new Error("Erro ao buscar plantas");

    const plantas = await response.json();
    renderizarLista(plantas);
  } catch (error) {
    console.error("Erro:", error);
  }
}

// ======================
// PESQUISA
// ======================
async function pesquisarPlantas() {
  const metodo = document.getElementById("search-method").value;
  const valor = document.getElementById("search-value").value.trim();

  if (!valor) {
    alert("Digite um valor para pesquisar!");
    return;
  }

  // Mapeamento do método para a URL correta
  let url = "";
  switch (metodo) {
    case "nome":
      url = `${API_URL}/pesquisar/nome?nome=${encodeURIComponent(valor)}`;
      break;
    case "nomeCientifico":
      url = `${API_URL}/pesquisar/nomeCientifico?nomeCientifico=${encodeURIComponent(valor)}`;
      break;
    case "especie":
      url = `${API_URL}/pesquisar/especie?especie=${encodeURIComponent(valor)}`;
      break;
    default:
      alert("Método de pesquisa inválido!");
      return;
  }

  try {
    const response = await fetch(url);
    if (!response.ok) throw new Error("Erro ao pesquisar plantas");

    const plantas = await response.json();
    renderizarLista(plantas);
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao pesquisar plantas");
  }
}

// ======================
// RENDERIZA LISTA
// ======================
function renderizarLista(plantas) {
  const lista = document.getElementById("plantas-list");
  lista.innerHTML = "";

  plantas.forEach(planta => {
    const card = document.createElement("div");
    card.className = "planta-card";
    card.innerHTML = `
    <div >
      <h2>ID: ${planta.id}</h2> 
      <div class="dados-plant">
      <p>${planta.nomeCientifico}</p>
      <img src="${planta.urlFoto}" alt="foto_planta" class="foto-planta">
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
    btn.addEventListener("click", () => abrirModalEdicao(btn.dataset.id))
  );
  document.querySelectorAll(".btn-excluir").forEach(btn =>
    btn.addEventListener("click", () => deletarPlanta(btn.dataset.id))
  );
}

// ======================
// MODAL - NOVO
// ======================
function abrirModalNovo() {
  document.querySelectorAll("#modal-cadastro input").forEach(el => (el.value = ""));
  document.getElementById("preview-img").src = "/src/main/resources/static/img/img.svg";
  document.getElementById("modal-cadastro").style.display = "flex";
}

// ======================
// MODAL - EDITAR
// ======================
async function abrirModalEdicao(id) {
  try {
    const response = await fetch(`${API_URL}/pesquisar/todos`);
    const plantas = await response.json();
    const planta = plantas.find(p => p.id == id);

    if (!planta) return;

    document.getElementById("id").value = planta.id;
    document.getElementById("nome").value = planta.nome || "";
    document.getElementById("nomeCientifico").value = planta.nomeCientifico || "";
    document.getElementById("especie").value = planta.especie || "";
    document.getElementById("rega").value = planta.rega || "";
    document.getElementById("poda").value = planta.poda || "";
    document.getElementById("resumoDadosPlanta").value = planta.resumoDadosPlanta || "";
    document.getElementById("urlFoto").value = planta.urlFoto || "";
    document.getElementById("preview-img").src = planta.urlFoto || "/src/main/resources/static/img/img.svg";

    document.getElementById("modal-cadastro").style.display = "flex";
  } catch (error) {
    console.error("Erro:", error);
  }
}

// ======================
// SALVAR
// ======================
async function salvarPlanta() {
  const planta = {
    id: document.getElementById("id").value || null,
    nome: document.getElementById("nome").value,
    nomeCientifico: document.getElementById("nomeCientifico").value,
    especie: document.getElementById("especie").value,
    rega: document.getElementById("rega").value,
    poda: document.getElementById("poda").value,
    resumoDadosPlanta: document.getElementById("resumoDadosPlanta").value,
    urlFoto: document.getElementById("urlFoto").value
  };

  try {
    const url = planta.id ? `${API_URL}/alterar` : `${API_URL}/cadastrar`;
    const method = planta.id ? "PUT" : "POST";

    const response = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(planta)
    });

    if (!response.ok) throw new Error("Erro ao salvar planta");

    alert(planta.id ? "Planta atualizada com sucesso!" : "Planta cadastrada com sucesso!");
    document.getElementById("modal-cadastro").style.display = "none";
    carregarPlantas(); // 🔄 Atualiza lista
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao salvar planta");
  }
}

// ======================
// DELETAR
// ======================
async function deletarPlanta(id) {
  if (!confirm("Deseja realmente excluir esta planta?")) return;

  try {
    const response = await fetch(`${API_URL}/deletar?id=${id}&idUsuario=1`, { method: "DELETE" });
    if (!response.ok) throw new Error("Erro ao excluir planta");

    alert("Planta excluída com sucesso!");
    carregarPlantas(); // 🔄 Atualiza lista
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao excluir planta");
  }
}
