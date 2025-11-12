function mostrarPopupSucesso(mensagem) {
  let popup = document.getElementById("popup-sucesso");
  let texto = document.getElementById("popup-mensagem");

  if (!popup) {
    popup = document.createElement("div");
    popup.id = "popup-sucesso";
    popup.className = "popup-sucesso";
    popup.innerHTML = `<p id="popup-mensagem"></p>`;
    document.body.appendChild(popup);
    texto = document.getElementById("popup-mensagem");
  }

  texto.textContent = mensagem;
  popup.classList.add("mostrar");

  setTimeout(() => {
    popup.classList.remove("mostrar");
  }, 5000);
}


function mostrarPopupErro(msg) {
  const popup = document.createElement("div");
  popup.className = "popup-erro";
  popup.textContent = msg;
  document.body.appendChild(popup);
  setTimeout(() => popup.remove(), 2500);
}
