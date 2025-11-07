package br.com.pie4.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String mail;
    private String senha;

    public LoginRequest() {
    }

    public LoginRequest(String mail, String senha) {
        this.mail = mail;
        this.senha = senha;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}