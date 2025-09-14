package br.com.pie4.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String nome;
    private String documento_pessoal;
    private String mail;
    private String telefone;
    private String senha;
    private String confirmarSenha;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String nome, String documento_pessoal, String mail, String telefone, String senha, String confirmarSenha) {
        this.nome = nome;
        this.documento_pessoal = documento_pessoal;
        this.mail = mail;
        this.telefone = telefone;
        this.senha = senha;
        this.confirmarSenha = confirmarSenha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento_pessoal() {
        return documento_pessoal;
    }

    public void setDocumento_pessoal(String documento_pessoal) {
        this.documento_pessoal = documento_pessoal;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "nome='" + nome + '\'' +
                ", documento_pessoal='" + documento_pessoal + '\'' + //criar metodo para mascarar alguns digitos do documento
                ", mail='" + mail + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
