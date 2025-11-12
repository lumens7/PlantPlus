const token = localStorage.getItem("token");
document.addEventListener("DOMContentLoaded", function () {
  if (!token) {
    alert("Acesso negado! Faça login novamente.");
    window.location.href = "/src/main/resources/static/pages/login.html";
    return;
  }
  const rolesData = JSON.parse(localStorage.getItem("roles")) || [];
  // Extrai apenas o campo "nome" de cada role (caso seja objeto)
  const roles = rolesData.map(r => (typeof r === "string" ? r : r.nome));

  const header = document.getElementById("menu-principal");

  // Cria botão do menu principal (topo)
  const menuBtnItem = document.createElement("div");
  menuBtnItem.className = "menu-item";

  const menuBtnLink = document.createElement("a");
  menuBtnLink.href = "#";

  const button = document.createElement("button");
  button.className = "menu-btn";

  const img = document.createElement("img");
  img.src = "/src/main/resources/static/img/menu.svg";
  img.alt = "menu";
  button.appendChild(img);

  const title = document.createElement("h1");
  // Detecta o nome da página atual e coloca no título
  const pageName = window.location.pathname.split("/").pop().replace(".html", "").toUpperCase();
  
  // Mapeia nomes de arquivo → título legível
  const pageTitles = {
    "home": "HOME",
    "plantas_user": "PLANTAS",
    "plantas_cie": "PLANTAS CIE",
    "tarefas": "TAREFAS",
    "constrole_usuario": "CONTROLE DE USUÁRIOS",
    "usuario": "USUÁRIO"
  };
  // Usa o título mapeado, se existir
  if (pageTitles[pageName]) {
    pageTitle = pageTitles[pageName];
  }
  let pageTitle = pageName;

  title.textContent = pageTitle;


  menuBtnLink.appendChild(button);
  menuBtnLink.appendChild(title);
  menuBtnItem.appendChild(menuBtnLink);
  header.appendChild(menuBtnItem);

  // 👉 Abre/fecha o menu ao clicar no botão MENU
  button.addEventListener("click", () => {
    header.classList.toggle("aberto");
  });

  // Função auxiliar para criar itens
  const createMenuItem = (href, text) => {
    const div = document.createElement("div");
    div.className = "menu-item";

    const a = document.createElement("a");
    a.href = `/src/main/resources/static/pages/${href}`;

    const h1 = document.createElement("h1");
    h1.textContent = text;

    a.appendChild(h1);
    div.appendChild(a);
    return div;
  };

  // Adiciona itens conforme permissões
  header.appendChild(createMenuItem("home.html", "HOME"));

  if (roles.includes("ROLE_PLANTAS_USER")){
    header.appendChild(createMenuItem("plantas_user.html", "PLANTAS"));}

  if (roles.includes("ROLE_PLANTAS_CIE")){
    header.appendChild(createMenuItem("plantas_cie.html", "PLANTAS CIE"));}

  if (roles.includes("ROLE_TAREFAS")){
    header.appendChild(createMenuItem("tarefas.html", "TAREFAS"));}

  if (roles.includes("ROLE_CONTROLE_USUARIO")){
    header.appendChild(createMenuItem("controle_usuario.html", "CONTROLE DE USUÁRIOS"));}
  if(roles.includes("ROLE_USUARIO")){
    header.appendChild(createMenuItem("usuario.html", "USUÁRIO"));}
});
