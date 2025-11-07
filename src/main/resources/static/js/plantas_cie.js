const ID_USUARIO = localStorage.getItem("id"); 
document.addEventListener("DOMContentLoaded", () => {
  if (!validarAcesso(["ROLE_PLANTAS_CIE"])) return;
  montarModal();
  carregarPlantas();

  // Mostrar/ocultar campo de pesquisa
  const btnSearch = document.getElementById("btn-search");
  const btnDoSearch = document.getElementById("btn-do-search");

  if (btnSearch && btnDoSearch) {
    btnSearch.addEventListener("click", () => {
      const searchArea = document.getElementById("search-area");
      searchArea.style.display =
        searchArea.style.display === "none" ? "flex" : "none";
    });

    btnDoSearch.addEventListener("click", pesquisarPlantas);
  }
});

// ======================
// MONTA O MODAL
// ======================
function montarModal() {
  const modal = document.createElement("div");
  modal.id = "modal-cadastro";
  modal.className = "modal";
  modal.style.display = "none";

  modal.innerHTML = `
    <div class="modal-content">
      <h2>CADASTRO PLANTA CIE</h2>

      <input type="hidden" id="id">

      <label for="nome">NOME PLANTA</label>
      <input type="text" id="nome" placeholder="nome planta...">

      <label for="nomeCientifico">NOME CIENTÍFICO</label>
      <input type="text" id="nomeCientifico" placeholder="nome científico...">

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
        <img id="preview-img" src="/src/main/resources/static/img/img.svg" alt="preview">
      </div>

        <button id="btn-salvar">SALVAR</button>
        <button id="btn-fechar">FECHAR</button>
      
    </div>
  `;

  document.body.appendChild(modal);

  // Eventos internos
  const btnAdd = document.getElementById("btn-add");
  const btnSalvar = document.getElementById("btn-salvar");
  const btnFechar = document.getElementById("btn-fechar");
  const urlFotoInput = document.getElementById("urlFoto");
  const previewImg = document.getElementById("preview-img");

  if (btnAdd) btnAdd.addEventListener("click", abrirModalNovo);
  btnFechar.addEventListener("click", () => (modal.style.display = "none"));
  urlFotoInput.addEventListener("input", () => {
    previewImg.src =
      urlFotoInput.value.trim() ||
      "/src/main/resources/static/img/img.svg";
  });
  btnSalvar.addEventListener("click", salvarPlanta);
}

// ======================
// LISTAR TODAS AS PLANTAS
// ======================
async function carregarPlantas() {
  try {
    const response = await fetch(`${API_URL_USER}/plant/cie/pesquisar/todos`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) throw new Error("Erro ao buscar plantas");

    const plantas = await response.json();
    renderizarLista(plantas);
  } catch (error) {
    console.error("Erro ao carregar plantas:", error);
  }
}



// ======================
// PESQUISAR PLANTAS
// ======================
async function pesquisarPlantas() {
  const metodo = document.getElementById("search-method").value;
  const valor = document.getElementById("search-value").value.trim();

  if (!valor) {
    alert("Digite um valor para pesquisar!");
    return;
  }

  let url = "";
  switch (metodo) {
    case "nome":
      url = `${API_URL_USER}/plant/cie/pesquisar/nome?nome=${encodeURIComponent(valor)}`;
      break;
    case "nomeCientifico":
      url = `${API_URL_USER}/plant/cie/pesquisar/nomeCientifico?nomeCientifico=${encodeURIComponent(valor)}`;
      break;
    case "especie":
      url = `${API_URL_USER}/plant/cie/pesquisar/especie?especie=${encodeURIComponent(valor)}`;
      break;
    default:
      alert("Método de pesquisa inválido!");
      return;
  }

  try {
    const token = localStorage.getItem("token");
    const response = await fetch(url, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (!response.ok) throw new Error("Erro ao pesquisar plantas");

    const plantas = await response.json();
    renderizarLista(plantas);
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao pesquisar plantas");
  }
}


// ======================
// RENDERIZA LISTA DE PLANTAS
// ======================
function renderizarLista(plantas) {
  const lista = document.getElementById("main-list");
  lista.innerHTML = "";

  if (!plantas || plantas.length === 0) {
    lista.innerHTML = "<p>Nenhuma planta encontrada.</p>";
    return;
  }

  plantas.forEach(planta => {
    const card = document.createElement("div");
    card.className = "main-card";
    card.innerHTML = `
      <div>
        <h2>ID: ${planta.id}</h2> 
        <div class="dados-main">
          <p>${planta.nomeCientifico}</p>
          </div>
        </div>
          <img src="${planta.urlFoto}" alt="foto_planta" class="foto-main">
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
// MODAL NOVO CADASTRO
// ======================
function abrirModalNovo() {
  document.querySelectorAll("#modal-cadastro input").forEach(el => (el.value = ""));
  document.getElementById("preview-img").src = "/src/main/resources/static/img/img.svg";
  document.getElementById("modal-cadastro").style.display = "flex";
}

// ======================
// MODAL EDIÇÃO
// ======================
async function abrirModalEdicao(id) {
  try {
    const response = await fetch(`${API_URL_USER}/plant/cie/pesquisar/todos`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
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
    console.error("Erro ao abrir modal de edição:", error);
  }
}

// ======================
// SALVAR PLANTA
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
    const url = planta.id
      ? `${API_URL_USER}/plant/cie/alterar`
      : `${API_URL_USER}/plant/cie/cadastrar`;
    const method = planta.id ? "PUT" : "POST";

    const token = localStorage.getItem("token");
    const response = await fetch(url, {
      method,
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(planta)
    });

    if (!response.ok) throw new Error("Erro ao salvar planta");

    mostrarPopupSucesso(planta.id ? "Planta atualizada com sucesso!" : "Planta cadastrada com sucesso!");
    setTimeout(()=>{
    document.getElementById("modal-cadastro").style.display = "none";
    },3000);
    carregarPlantas();
  } catch (error) {
    console.error("Erro ao salvar planta:", error);
    alert("Erro ao salvar planta");
  }
}


// ======================
// DELETAR PLANTA
// ======================
async function deletarPlanta(id) {
  if (!confirm("Deseja realmente excluir esta planta?")) return;

  try {
    const token = localStorage.getItem("token");
    const response = await fetch(
      `${API_URL_USER}/plant/cie/deletar?id=${id}&idUsuario=${ID_USUARIO}`,
      {
        method: "DELETE",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      }
    );

    if (!response.ok) throw new Error("Erro ao excluir planta");

    mostrarPopupSucesso("Planta excluída com sucesso!");
    setTimeout(()=> carregarPlantas(), 3000) ;
  } catch (error) {
    console.error("Erro ao excluir planta:", error);
    alert("Erro ao excluir planta");
  }
}

