function logout() {

    localStorage.removeItem("username")
    localStorage.removeItem("id")
    localStorage.removeItem("roles");
    localStorage.removeItem("token")

    window.location.href = "/src/main/resources/static/pages/login.html";
}

document.addEventListener("DOMContentLoaded", function () {
    const logoutButton = document.getElementById("logoutButton");
    if (logoutButton) {
        logoutButton.addEventListener("click", logout);
    } else {
        console.error("Botão de logoff não encontrado.");
    }
});