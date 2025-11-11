const ID_USUARIO = localStorage.getItem("id"); 

document.addEventListener("DOMContentLoaded", () => {
  if (!validarAcesso(["ROLE_PLANTAS_USER"])) return;
  montarModalUser();
  carregarPlantasUser();

  document.getElementById("btn-search").addEventListener("click", () => {
    const searchArea = document.getElementById("search-area");
    searchArea.style.display = searchArea.style.display === "none" ? "flex" : "none";
  });

  document.getElementById("btn-do-search").addEventListener("click", pesquisarPlantasUser);
});

function montarModalUser() {
  const modal = document.createElement("div");
  modal.id = "modal-cadastro-user";
  modal.className = "modal";
  modal.innerHTML = `
    <div class="modal-content">
      <h2>CADASTRO PLANTA USER</h2>

      <input type="hidden" id="id">
      <input type="hidden" id="id_plantaCie">

      <label for="nomePlantaBusca">NOME DA PLANTA</label>
      <div id="busca-wrapper">
        <div id="busca-container">
          <input type="text" id="nomePlantaBusca" placeholder="digite o nome da planta...">
        </div>
        <select id="resultadoBusca">
          <option value="">Digite para buscar...</option>
        </select>
      </div>
      <label for="previewPlanta">FOTO DA PLANTA</label>
      <div class="input-img">
        <img id="previewPlanta" src="/src/main/resources/static/img/img.svg" alt="preview planta">
      </div>

      <label for="quantidade">QUANTIDADE</label>
      <input type="number" id="quantidade" placeholder="quantidade...">

      <label for="idade">IDADE (meses)</label>
      <input type="number" id="idade" placeholder="idade em meses...">

      <button id="btn-salvar">SALVAR</button>
      <button id="btn-fechar">FECHAR</button>
    </div>
  `;
  document.body.appendChild(modal);

  document.getElementById("btn-add").addEventListener("click", abrirModalNovoUser);
  document.getElementById("btn-fechar").addEventListener("click", () => (modal.style.display = "none"));
  document.getElementById("btn-salvar").addEventListener("click", salvarPlantaUser);

  const inputBusca = document.getElementById("nomePlantaBusca");
  let debounceTimer;

  inputBusca.addEventListener("input", () => {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(buscarPlantasCie, 300); 
  });
}
async function buscarPlantasCie() {
  const termo = document.getElementById("nomePlantaBusca").value.trim().toLowerCase();
  const selectResultados = document.getElementById("resultadoBusca");

  selectResultados.innerHTML = '<option value="">Buscando...</option>';

  if (!termo || termo.length < 2) {
    selectResultados.innerHTML = '<option value="">Digite pelo menos 2 letras</option>';
    return;
  }

  try {

    const response = await fetch(`${API_URL_USER}/plant/cie/pesquisar/todos`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (!response.ok) throw new Error("Erro ao buscar plantas CIE");

    const plantas = await response.json();

    const filtradas = plantas.filter(
      p =>
        p.nome?.toLowerCase().includes(termo) ||
        p.nomeCientifico?.toLowerCase().includes(termo)
    );

    selectResultados.innerHTML = '<option value="">Selecione uma planta</option>';

    if (filtradas.length === 0) {
      const opt = document.createElement("option");
      opt.value = "";
      opt.textContent = "Nenhuma planta encontrada";
      selectResultados.appendChild(opt);
    } else {
      filtradas.forEach(planta => {
        const opt = document.createElement("option");
        opt.value = planta.id;
        opt.textContent = `${planta.nome} (${planta.nomeCientifico})`;
        selectResultados.appendChild(opt);
      });
    }

    selectResultados.onchange = e => {
      const plantaSelecionada = filtradas.find(p => p.id == e.target.value);
      if (plantaSelecionada) selecionarPlantaCie(plantaSelecionada);
    };

  } catch (error) {
    console.error("Erro ao buscar plantas:", error);
    selectResultados.innerHTML = '<option value="">Erro ao buscar</option>';
  }
}


function selecionarPlantaCie(planta) {
  document.getElementById("id_plantaCie").value = planta.id;
  document.getElementById("nomePlantaBusca").value = planta.nome;

  const previewImg = document.getElementById("previewPlanta");
  previewImg.src = planta.urlFoto || "/src/main/resources/static/img/img.svg";

  const selectResultados = document.getElementById("resultadoBusca");
  selectResultados.innerHTML = '<option value="">Selecione uma planta</option>';
}


function abrirModalNovoUser() {
  document.querySelectorAll("#modal-cadastro-user input").forEach(el => (el.value = ""));
  document.getElementById("resultadoBusca").innerHTML = '<option value="">Digite para buscar...</option>';
  document.getElementById("modal-cadastro-user").style.display = "flex";
}


async function carregarPlantasUser() {
  try {
    const response = await fetch(`${API_URL_USER}/plant/user/pesquisar/plant_user?id_plant_user=${ID_USUARIO}`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
    if (!response.ok) throw new Error("Erro ao buscar plantas do usuário");

    const plantas = await response.json();
    renderizarListaUser(plantas);
  } catch (error) {
    console.error("Erro:", error);
  }
}

async function pesquisarPlantasUser() {
  const metodo = document.getElementById("search-method").value;
  const valor = document.getElementById("search-value").value.trim();

  if (!valor) {
    alert("Digite um valor para pesquisar!");
    return;
  }

  let url = "";
  if (metodo === "nome") {
    url = `${API_URL_USER}/plant/user/pesquisar/nome?nome=${encodeURIComponent(valor)}`;
  } else if (metodo === "id") {
    url = `${API_URL_USER}/plant/user/pesquisar/plant_user?id_plant_user=${encodeURIComponent(valor)}`;
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

function renderizarListaUser(plantas) {
  const lista = document.getElementById("main-list");
  lista.innerHTML = "";

  plantas.forEach(planta => {
    const card = document.createElement("div");
    card.className = "main-card";
    card.innerHTML = `
      <div>
        <h2>ID: ${planta.id}</h2>
        <div class="dados-main">
          <p>${planta.plantaCie ? planta.plantaCie.nomeCientifico : "N/A"}</p>
          <p>Quantidade: ${planta.quantidade}</p>
          <p>Idade: ${planta.idade} meses</p>
          <img src="${planta.plantaCie.urlFoto}" alt="foto_planta" class="foto-main">
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

function abrirModalNovoUser() {
  document.querySelectorAll("#modal-cadastro-user input").forEach(el => (el.value = ""));
  document.getElementById("modal-cadastro-user").style.display = "flex";
}

async function abrirModalEdicaoUser(id) {
  try {
    const response = await fetch(`${API_URL_USER}/plant/user/pesquisar/plant_user?id_plant_user=${ID_USUARIO}`, {
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });
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

async function salvarPlantaUser() {
  const plantaUser = {
    id: document.getElementById("id").value || null,
    quantidade: document.getElementById("quantidade").value,
    idade: document.getElementById("idade").value,
    id_usuario: ID_USUARIO,
    id_plantaCie: document.getElementById("id_plantaCie").value
  };

  try {
    const url = plantaUser.id ? `${API_URL_USER}/plant/user/alterar` : `${API_URL_USER}/plant/user/cadastrar`;
    const method = plantaUser.id ? "PUT" : "POST";

    const response = await fetch(url, {
      method,
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(plantaUser)
    });

    if (!response.ok) throw new Error("Erro ao salvar planta do usuário");

    mostrarPopupSucesso(plantaUser.id ? "Planta atualizada com sucesso!" : "Planta cadastrada com sucesso!")
    setTimeout(()=>{
      document.getElementById("modal-cadastro-user").style.display = "none";
    }, 2000)
    carregarPlantasUser();
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao salvar planta do usuário");
  }
}

async function deletarPlantaUser(id) {
  confirmarAcao(
    "Deseja realmente excluir esta planta?",
    async () => {
      try {
        const response = await fetch(
          `${API_URL_USER}/plant/user/deletar?id=${id}&idUsuario=${ID_USUARIO}`,
          {
            method: "DELETE",
            headers: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
            }
          }
        );

        if (!response.ok) {
          const msg = await response.text();
          throw new Error(msg || "Erro ao excluir planta");
        }

        mostrarPopupSucesso("Planta excluída com sucesso!");
        setTimeout(() => carregarPlantasUser(), 3000);

      } catch (error) {
        console.error("Erro:", error);
        alert("Erro ao excluir planta do usuário: " + error.message);
      }
    },
    () => {
      console.log("Usuário cancelou a exclusão.");
    }
  );
}
