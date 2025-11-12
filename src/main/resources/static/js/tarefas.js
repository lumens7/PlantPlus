document.addEventListener("DOMContentLoaded", () => {
  if (!validarAcesso(["ROLE_TAREFAS"])) return;

  const lista = document.getElementById("main-list");
  const btnAdd = document.getElementById("btn-add");
  const usuarioId = localStorage.getItem("id");

  // 🔹 Variável global para os filtros
  let tarefasCarregadas = [];

  // 🔹 Elementos do filtro
  const btnFiltro = document.getElementById("btn-search");
  const filtroDiasContainer = document.getElementById("filtro-dias");
  const btnAplicarFiltro = document.getElementById("btn-aplicar-filtro");
  const btnLimparFiltro = document.getElementById("btn-limpar-filtro");

  // ----------------------------------------------------
  // ✅ CARREGAR TAREFAS
  // ----------------------------------------------------
  async function carregarTarefas() {
    try {
      const resp = await fetch(`${API_URL_USER}/tarefa/pesquisar/usuario?idUsuario=${usuarioId}`, {
        headers: { "Authorization": `Bearer ${token}` }
      });

      if (!resp.ok) {
        console.error("Erro HTTP:", resp.status);
        return;
      }

      // ✅ Salva as tarefas para filtros
      tarefasCarregadas = await resp.json();

      // ✅ Exibe todas ao carregar
      renderizarTarefas(tarefasCarregadas);

    } catch (e) {
      console.error("Erro ao carregar tarefas:", e);
    }
  }


  // ----------------------------------------------------
  // ✅ RENDERIZAÇÃO DAS TAREFAS
  // ----------------------------------------------------
  function renderizarTarefas(tarefas) {
    lista.innerHTML = "";

    tarefas.forEach(tarefa => {
      const card = document.createElement("div");
      card.className = "tarefa-card";

      const dias = tarefa.repetir?.join(", ") || "Nenhum";
      const horaEfetuar = tarefa.hora_efetuar_atv ? tarefa.hora_efetuar_atv.substring(0, 5) : "N/A";
      const horaFeito = tarefa.horario_efetuado_atv ? new Date(tarefa.horario_efetuado_atv).toLocaleString() : "Não realizado";

      const plantasHTML = (tarefa.plantas || []).map(p => `
        <div class="planta-item">
          <img src="${p.url_foto}" alt="${p.nome}" class="planta-img" />
          <span class="planta-nome">${p.nome}</span>
          <span class="planta-quantidade">Qtd: ${p.quantidade}</span>
        </div>
      `).join("");

      card.innerHTML = `
        <div class="card-header">
          <h2>${tarefa.nome_tarefa}</h2>
          <div class="card-actions">
            <button class="btn-editar" data-id="${tarefa.id}">
              <img src="/src/main/resources/static/img/edit.svg" alt="Editar">
            </button>
            <button class="btn-excluir" data-id="${tarefa.id}">
              <img src="/src/main/resources/static/img/delete.svg" alt="Excluir">
            </button>
          </div>
        </div>
        <div class="card-body">
          <p><strong>Plantas:</strong></p>
          <div class="plantas-list">${plantasHTML}</div>
          <p><strong>Descrição:</strong> ${tarefa.descricao_tarefa}</p>
          <p><strong>Dias:</strong> ${dias}</p>
          <p><strong>Horário:</strong> ${horaEfetuar}</p>
        </div>
      `;

      lista.appendChild(card);
    });

    // Editar/Excluir
    document.querySelectorAll(".btn-editar").forEach(btn =>
      btn.addEventListener("click", () => abrirModalTarefa("editar", btn.dataset.id))
    );

    document.querySelectorAll(".btn-excluir").forEach(btn =>
      btn.addEventListener("click", () => deletarTarefa(btn.dataset.id))
    );
  }


  // ----------------------------------------------------
  // ✅ ABRIR/FECHAR PAINEL DE FILTRO
  // ----------------------------------------------------
  btnFiltro.addEventListener("click", () => {
    filtroDiasContainer.style.display =
      filtroDiasContainer.style.display === "none" ? "block" : "none";
  });


  // ----------------------------------------------------
  // ✅ APLICAR FILTRO
  // ----------------------------------------------------
  btnAplicarFiltro.addEventListener("click", () => {
    const checkboxes = document.querySelectorAll(".filtro-dia:checked");
    const diasSelecionados = Array.from(checkboxes).map(c => c.value);

    if (diasSelecionados.length === 0) {
      mostrarPopupErro("Selecione pelo menos um dia!");
      return;
    }

    const filtradas = tarefasCarregadas.filter(tarefa =>
      tarefa.repetir && tarefa.repetir.some(dia => diasSelecionados.includes(dia))
    );

    renderizarTarefas(filtradas);
  });


  // ----------------------------------------------------
  // ✅ LIMPAR FILTRO
  // ----------------------------------------------------
  btnLimparFiltro.addEventListener("click", () => {
    document.querySelectorAll(".filtro-dia").forEach(c => c.checked = false);
    renderizarTarefas(tarefasCarregadas);
  });


  // ----------------------------------------------------
  // ✅ RESTANTE DO SEU CÓDIGO (MODAL, CRUD)
  // ----------------------------------------------------
  async function abrirModalTarefa(tipo, idTarefa = null) {
    const modal = criarModal(tipo === "editar" ? "Editar Tarefa" : "Cadastrar Tarefa");

    const plantas = await fetch(`${API_URL_USER}/plant/user/pesquisar/plant_user?id_plant_user=${usuarioId}`, {
      headers: { "Authorization": `Bearer ${token}` }
    }).then(r => r.ok ? r.json() : []);

    let tarefa = {};
    if (tipo === "editar") {
      const resp = await fetch(`${API_URL_USER}/tarefa/pesquisar/id?id=${idTarefa}`, {
        headers: { "Authorization": `Bearer ${token}` }
      });
      tarefa = await resp.json();
    }

    const selectedPlantaIds = new Set(
      (tarefa.plantaUserIds || []).map(id => Number(id))
    );

    const plantasHTML = plantas.map(p => {
      const pId = Number(p.id);
      const selecionada = selectedPlantaIds.has(pId);
      return `
        <div class="planta-selecao" data-id="${pId}">
          <input type="checkbox" id="planta-${pId}" value="${pId}" ${selecionada ? "checked" : ""}>
          <label for="planta-${pId}">
            <img src="${(p.plantaCie && (p.plantaCie.urlFoto || p.plantaCie.url_foto)) || '/src/main/resources/static/img/placeholder.png'}" alt="">
            <span>${(p.plantaCie && (p.plantaCie.nome || p.plantaCie.nomeCientifico)) || 'Sem nome'}</span>
            <span>Qtd: ${p.quantidade ?? '-'}</span>
            <span>Idade: ${p.idade ?? '-'} meses</span>
          </label>
        </div>
      `;
    }).join("");

    const diasSemana = ["DOMINGO","SEGUNDA","TERCA","QUARTA","QUINTA","SEXTA","SABADO"];
    const diasHTML = diasSemana.map((dia, i) => `
      <div class="dia-item">
        <input type="checkbox" id="dia-${i}" value="${dia}" ${tarefa.repetir?.includes(dia) ? "checked" : ""}>
        <label for="dia-${i}">${dia.slice(0,3)}</label>
      </div>
    `).join("");

    modal.querySelector(".modal-body").innerHTML = `
      <label>Nome da tarefa:</label>
      <input id="nome_tarefa" type="text" value="${tarefa.nome_tarefa || ""}" required>

      <label>Descrição:</label>
      <textarea id="descricao_tarefa" rows="3">${tarefa.descricao_tarefa || ""}</textarea>

      <label>Horário:</label>
      <input id="hora_efetuar_atv" type="time" value="${tarefa.hora_efetuar_atv || ""}">

      <label>Selecione as plantas:</label>
      <div id="plantas-container" class="plantas-container">${plantasHTML}</div>

      <label>Dias da semana:</label>
      <div id="dias-semana" class="dias-container">${diasHTML}</div>
    `;

    modal.querySelector(".modal-save").addEventListener("click", async () => {
      const nome = document.getElementById("nome_tarefa").value.trim();
      const descricao = document.getElementById("descricao_tarefa").value.trim();
      const hora = document.getElementById("hora_efetuar_atv").value;

      const plantasSelecionadas = Array.from(
        document.querySelectorAll("#plantas-container input[type='checkbox']:checked")
      ).map(i => Number(i.value));

      const diasSelecionados = Array.from(
        document.querySelectorAll("#dias-semana input:checked")
      ).map(i => i.value);

      const corpo = {
        id: tarefa.id,
        nome_tarefa: nome,
        descricao_tarefa: descricao,
        hora_efetuar_atv: hora,
        plantaUserIds: plantasSelecionadas,
        repetir: diasSelecionados,
        horario_efetuado_atv: tipo === "editar" ? tarefa.horario_efetuado_atv : null,
        usuarioId: usuarioId
      };

      await fetch(`${API_URL_USER}/tarefa/${tipo === "editar" ? "alterar" : "cadastrar"}`, {
        method: tipo === "editar" ? "PUT" : "POST",
        headers: { 
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify(corpo)
      });

      mostrarPopupSucesso(tarefa.id ? "Tarefa atualizada com sucesso!" : "Tarefa cadastrada com sucesso!");
      setTimeout(()=> fecharModal(modal), 2000);
      carregarTarefas();
    });
  }


  function criarModal(titulo) {
    const modal = document.createElement("div");
    modal.className = "modal";
    modal.innerHTML = `
      <div class="modal-content">
        <h2>${titulo}</h2>
        <div class="modal-body"></div>
        <div class="modal-footer">
          <button class="modal-save">Salvar</button>
          <button class="modal-close">Cancelar</button>
        </div>
      </div>
    `;
    document.body.appendChild(modal);
    modal.querySelector(".modal-close").addEventListener("click", () => fecharModal(modal));
    return modal;
  }

  function fecharModal(modal) {
    modal.remove();
  }


  async function deletarTarefa(id) {
    const id_user = localStorage.getItem("id");

    if (confirm("Deseja realmente excluir esta tarefa?")) {
      await fetch(`${API_URL_USER}/tarefa/deletar?id=${id}&idUsuario=${id_user}`, {
        method: "DELETE",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });

      mostrarPopupSucesso("Tarefa excluída com sucesso!");

      setTimeout(() => carregarTarefas(), 3000);
    }
  }


  btnAdd.addEventListener("click", () => abrirModalTarefa("cadastrar"));

  carregarTarefas();
});
