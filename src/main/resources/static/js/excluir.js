// popupConfirmacao.js
export function confirmarAcao(mensagem, onConfirmar, onCancelar) {
  // Remove se já existir um popup aberto
  const existente = document.querySelector(".popup-confirmacao-container");
  if (existente) existente.remove();

  // Cria container
  const container = document.createElement("div");
  container.className = "popup-confirmacao-container";

  // Cria conteúdo
  const box = document.createElement("div");
  box.className = "popup-confirmacao-box";

  const texto = document.createElement("p");
  texto.textContent = mensagem;

  const botoes = document.createElement("div");
  botoes.className = "popup-confirmacao-botoes";

  const btnSim = document.createElement("button");
  btnSim.textContent = "Sim";
  btnSim.className = "btn-sim";

  const btnNao = document.createElement("button");
  btnNao.textContent = "Cancelar";
  btnNao.className = "btn-nao";

  botoes.appendChild(btnSim);
  botoes.appendChild(btnNao);
  box.appendChild(texto);
  box.appendChild(botoes);
  container.appendChild(box);
  document.body.appendChild(container);

  // Animação de entrada
  setTimeout(() => container.classList.add("mostrar"), 10);

  // Eventos
  btnSim.addEventListener("click", () => {
    fechar();
    if (onConfirmar) onConfirmar();
  });

  btnNao.addEventListener("click", () => {
    fechar();
    if (onCancelar) onCancelar();
  });

  function fechar() {
    container.classList.remove("mostrar");
    setTimeout(() => container.remove(), 200);
  }
}
