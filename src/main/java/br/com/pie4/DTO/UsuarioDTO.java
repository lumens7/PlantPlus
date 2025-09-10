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
}
