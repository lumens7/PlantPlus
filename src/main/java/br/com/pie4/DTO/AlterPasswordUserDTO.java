package br.com.pie4.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class AlterPasswordUserDTO {
    private Long id;
    private String email;
    private String documento_pessoal;
    private String novaSenha;
    private String confirmacaoNovaSenha;

    public AlterPasswordUserDTO() {
    }

    public AlterPasswordUserDTO(Long id, String email, String documento_pessoal, String novaSenha, String confirmacaoNovaSenha) {
        this.id = id;
        this.email = email;
        this.documento_pessoal = documento_pessoal;
        this.novaSenha = novaSenha;
        this.confirmacaoNovaSenha = confirmacaoNovaSenha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumento_pessoal() {
        return documento_pessoal;
    }

    public void setDocumento_pessoal(String documento_pessoal) {
        this.documento_pessoal = documento_pessoal;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmacaoNovaSenha() {
        return confirmacaoNovaSenha;
    }

    public void setConfirmacaoNovaSenha(String confirmacaoNovaSenha) {
        this.confirmacaoNovaSenha = confirmacaoNovaSenha;
    }
}
