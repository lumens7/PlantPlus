package br.com.pie4.DTO;


public class UsuarioExDTO {
    private Long id;
    private String nome;
    private String documento_pessoal;
    private String mail;
    private String telefone;

    public UsuarioExDTO() {
    }

    public UsuarioExDTO(Long id, String nome, String documento_pessoal, String mail, String telefone) {
        this.id = id;
        this.nome = nome;
        this.documento_pessoal = documento_pessoal;
        this.mail = mail;
        this.telefone = telefone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
