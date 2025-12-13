(() => {
  const ID_USUARIO = localStorage.getItem("id");
  const token = localStorage.getItem("token");
  let mapTarefaFeita = new Map(); 

  document.addEventListener("DOMContentLoaded", () => {
    if (!validarAcesso(["ROLE_TAREFAS"])) return;
    carregarTarefasDoDia();
  });

  async function carregarTarefasDoDia() {
    if (!ID_USUARIO) {
      console.error("ID do usuário não encontrado!");
      return;
    }

    const diaSemana = getDiaSemanaAtual();
    const container = document.getElementById("tarefas-dia");
    container.innerHTML = "<p>Carregando tarefas...</p>";

    try {
      const responseTarefas = await fetch(
        `${API_URL_USER}/tarefa/pesquisar/dia?dia=${diaSemana}&idUsuario=${ID_USUARIO}`,
        {
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        }
      );

      if (!responseTarefas.ok) {
        container.innerHTML = `<p>Nenhuma tarefa cadastrada para hoje.</p>`;
        return;
      }

      const tarefas = await responseTarefas.json();
      if (!tarefas || tarefas.length === 0) {
        container.innerHTML = `<p>Nenhuma tarefa para ${diaSemana.toLowerCase()}.</p>`;
        return;
      }

      // 🔹 Busca tarefas já feitas
      const feitasResponse = await fetch(
        `${API_URL_USER}/tarefa/pesquisar/tarefa_feita?id_usuario=${ID_USUARIO}`,
        {
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        }
      );

      mapTarefaFeita.clear();
      let tarefasFeitas = [];
      
      if (feitasResponse.ok) {
        tarefasFeitas = await feitasResponse.json();
        tarefasFeitas.forEach(t => mapTarefaFeita.set(t.id_tarefa, t.id));
      } else if (feitasResponse.status === 404) {
        tarefasFeitas = [];
      } else {
        throw new Error("Erro ao buscar tarefas feitas");
      }
      
      const idsFeitos = [...new Set(tarefasFeitas.map(t => t.id))];
      renderizarTarefas(tarefas, idsFeitos);
      
    } catch (error) {
      console.error("Erro ao carregar tarefas:", error);
      container.innerHTML = `<p>Erro ao carregar tarefas.</p>`;
    }
  }

  function getDiaSemanaAtual() {
    const dias = ["DOMINGO", "SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA", "SABADO"];
    return dias[new Date().getDay()];
  }

  function renderizarTarefas(tarefas, idsFeitos) {
    const container = document.getElementById("tarefas-dia");
    container.innerHTML = "";

    tarefas.forEach(tarefa => {
      const horaEfetuar = tarefa.hora_efetuar_atv
        ? tarefa.hora_efetuar_atv.substring(0, 5)
        : "N/A";

      const feitaHoje = idsFeitos.includes(tarefa.id);

      const card = document.createElement("div");
      card.className = "main-card tarefa-card";

      card.innerHTML = `
        <div class="dados-main">
          <div class="info-tarefa">
            <h2>${tarefa.nome_tarefa}</h2>
            <p>${tarefa.descricao_tarefa}</p>
            <p><strong>Horário:</strong> ${horaEfetuar}</p>
          </div>
          <div class="switch-container">
            <label class="switch">
              <input type="checkbox" id="switch-${tarefa.id}" ${feitaHoje ? "checked" : ""}>
              <span class="slider"></span>
            </label>
            <span class="switch-label">Feita</span>
          </div>
        </div>
      `;

      const switchInput = card.querySelector(`#switch-${tarefa.id}`);
      switchInput.addEventListener("change", e => {
        if (e.target.checked) {
          marcarTarefaComoFeita(tarefa.id);
        } else {
          desmarcarTarefaFeita(tarefa.id);
        }
      });

      container.appendChild(card);
    });
    const concluidasHoje = tarefas.filter(tarefa => idsFeitos.includes(tarefa.id)).length;

  atualizarDashboard(tarefas.length, concluidasHoje);

  }

  async function marcarTarefaComoFeita(idTarefa) {
    try {
      const response = await fetch(
        `${API_URL_USER}/tarefa/alterar/tarefa_feita?id_tarefa=${idTarefa}&id_usuario=${ID_USUARIO}`,
        {
          method: "PUT",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        }
      );

      if (!response.ok) throw new Error("Erro ao marcar tarefa como feita");

      const tarefaFeita = await response.json();
      mapTarefaFeita.set(idTarefa, tarefaFeita.id);

      mostrarPopupSucesso("Tarefa marcada como concluída!");
    } catch (error) {
      console.error("Erro:", error);
    }
  }

  async function desmarcarTarefaFeita(idTarefa) {
    try {
      const idTarefaFeita = mapTarefaFeita.get(idTarefa);
      if (!idTarefaFeita) {
        console.error("ID da tarefa feita não encontrado para tarefa:", idTarefa);
        return;
      }

      const response = await fetch(
        `${API_URL_USER}/tarefa/deletar/tarefa_feita?id_tarefa_feita=${idTarefaFeita}`,
        {
          method: "DELETE",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        }
      );

      if (!response.ok) throw new Error("Erro ao desmarcar tarefa feita");

      mapTarefaFeita.delete(idTarefa); 
      mostrarPopupSucesso("Tarefa marcada como não concluída.");
    } catch (error) {
      console.error("Erro:", error);
    }
  }

  function atualizarDashboard(total, concluidas) {
    document.getElementById("dash-total").textContent = total;
    document.getElementById("dash-concluidas").textContent = concluidas;
    document.getElementById("dash-pendentes").textContent = total - concluidas;
  }
  
})();
