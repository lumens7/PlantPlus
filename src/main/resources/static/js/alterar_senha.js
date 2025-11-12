// Referências aos elementos
const emailInput = document.getElementById("email");
const documentoInput = document.getElementById("documento");
const formVerificar = document.getElementById("form-verificar");
const formAlterar = document.getElementById("form-alterar");
const btnSalvar = document.querySelector(".btn-salvar");

let usuarioDocumento = null;
let usuarioEmail = null;

// -----------------------------
// VERIFICAR USUÁRIO
// -----------------------------
formVerificar.addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = emailInput.value.trim();
  const documento = documentoInput.value.trim();

  if (!email || !documento) {
    return mostrarPopupErro("Preencha email e documento.");
  }

  try {
    // Verifica documento
    const respDoc = await fetch(`${API_URL_USER}/user/pesquisar/documento?documento=${documento}`);
    if (!respDoc.ok) throw new Error("Documento não encontrado.");
    usuarioDocumento = await respDoc.json();

    // Verifica email
    const respEmail = await fetch(`${API_URL_USER}/user/pesquisar/mail?mail=${email}`);
    if (!respEmail.ok) throw new Error("Email não encontrado.");
    usuarioEmail = await respEmail.json();

    // Confirma se ambos pertencem ao mesmo usuário
    if (usuarioDocumento.id !== usuarioEmail.id) {
      throw new Error("Email e documento não correspondem ao mesmo usuário!");
    }

    // Sucesso
    mostrarPopupSucesso("Verificação concluída! Você pode alterar sua senha.");

    // Preenche o modal
    document.getElementById("modal-nome").innerText = "Nome: " + usuarioDocumento.nome;
    document.getElementById("modal-email").innerText = "E-mail: " + usuarioDocumento.mail;

    // Abre modal
    abrirModal();

  } catch (err) {
    mostrarPopupErro(err.message);
  }
});

// -----------------------------
// ABRIR E FECHAR MODAL
// -----------------------------
function abrirModal() {
  document.getElementById("modalAlterarSenha").style.display = "flex";
}

function fecharModal() {
  document.getElementById("modalAlterarSenha").style.display = "none";
}

// -----------------------------
// SALVANDO A NOVA SENHA
// -----------------------------
btnSalvar.addEventListener("click", async () => {

  const novaSenha = document.getElementById("novaSenha").value.trim();
  const confirmacao = document.getElementById("confirmacaoNovaSenha").value.trim();

  if (!novaSenha || !confirmacao) {
    return mostrarPopupErro("Preencha todos os campos.");
  }

  if (novaSenha !== confirmacao) {
    return mostrarPopupErro("As senhas não coincidem!");
  }

  if (novaSenha.length < 6) {
    return mostrarPopupErro("A senha deve ter no mínimo 6 caracteres.");
  }

  // Obtem o ID do usuário
  const idUser = usuarioDocumento.id;

  try {
  const resp = await fetch(`${API_URL_USER}/user/alterar/senha`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      id: idUser,
      novaSenha
    })
  });

  const mensagem = await resp.text();

  console.log("Resposta recebida:", resp);
  console.log("Mensagem recebida:", mensagem);

  if (!resp.ok) {
    throw new Error(mensagem || "Erro ao alterar senha.");
  }

  mostrarPopupSucesso(mensagem);
  fecharModal();

  setTimeout(() => {
    window.location.href = "/pages/login.html";
  }, 2000);

} catch (err) {
  mostrarPopupErro(err.message);
}


});

