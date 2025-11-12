
function formatarCPF(cpf) {
  cpf = cpf.replace(/\D/g, ""); 
  cpf = cpf.slice(0, 11); 
  if (cpf.length <= 3) return cpf;
  if (cpf.length <= 6) return cpf.replace(/(\d{3})(\d+)/, "$1.$2");
  if (cpf.length <= 9) return cpf.replace(/(\d{3})(\d{3})(\d+)/, "$1.$2.$3");
  return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, "$1.$2.$3-$4");
}

function formatarTelefone(telefone) {
  telefone = telefone.replace(/\D/g, ""); 
  telefone = telefone.slice(0, 11); 
  if (telefone.length <= 2) return `(${telefone}`;
  if (telefone.length <= 6) return telefone.replace(/(\d{2})(\d+)/, "($1) $2");
  if (telefone.length <= 10) return telefone.replace(/(\d{2})(\d{4})(\d+)/, "($1) $2-$3");
  return telefone.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
}

document.getElementById("documento").addEventListener("input", (e) => {
  e.target.value = formatarCPF(e.target.value);
});

document.getElementById("telefone").addEventListener("input", (e) => {
  e.target.value = formatarTelefone(e.target.value);
});

document.getElementById("cadastro-form").addEventListener("submit", async (e) => {
  e.preventDefault();

  const documentoSemMascara = document.getElementById("documento").value.replace(/\D/g, "");
  const telefoneSemMascara = document.getElementById("telefone").value.replace(/\D/g, "");

  const usuario = {
    nome: document.getElementById("nome").value,
    documento_pessoal: documentoSemMascara,
    mail: document.getElementById("mail").value,
    telefone: telefoneSemMascara,
    senha: document.getElementById("senha").value,
    confirmarSenha: document.getElementById("confirmarSenha").value,
  };

  try {
    const response = await fetch(`${API_URL_USER}/user/cadastrar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(usuario),
    });

    const mensagemDiv = document.getElementById("mensagem");
    mensagemDiv.innerHTML = "";

    if (response.ok) {
      const data = await response.json();
      mensagemDiv.textContent = "Usuário cadastrado com sucesso!";
      mensagemDiv.className = "mensagem sucesso";
      document.getElementById("cadastro-form").reset();
      window.location.href = "/src/main/resources/static/pages/login.html";
    } else {
      const erro = await response.text();
      mensagemDiv.textContent = erro;
      mensagemDiv.className = "mensagem erro";
    }
  } catch (error) {
    document.getElementById("mensagem").textContent = "Erro ao enviar requisição: " + error;
    document.getElementById("mensagem").className = "mensagem erro";
  }
});
