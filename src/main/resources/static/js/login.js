document.getElementById("login-form").addEventListener("submit", async (e) => {
  e.preventDefault();

  const mail = document.getElementById("email").value.trim();
  const senha = document.getElementById("senha").value.trim();

  try {
    const response = await fetch(`${API_URL_USER}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ mail, senha })
    });

    if (!response.ok) {
      alert("Email ou senha inválidos!");
      return;
    }

    const data = await response.json();
    localStorage.setItem("username", data.username)
    localStorage.setItem("id", data.id)
    localStorage.setItem("roles", JSON.stringify(data.roles));
    localStorage.setItem("token", data.token)
    localStorage.setItem("type", data.type)
    mostrarPopupSucesso("Login realizado com sucesso!");
    setTimeout(() => {
      window.location.href = "/src/main/resources/static/pages/home.html";
    }, 3000);
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao tentar logar.");
  }
});
