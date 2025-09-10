package br.com.pie4.Service;

import br.com.pie4.DTO.UsuarioDTO;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioCadastrar){
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioCadastrar.getNome());
        usuario.setMail(usuarioCadastrar.getMail());
        usuario.setTelefone(usuarioCadastrar.getTelefone());
        usuario.setDocumento_pessoal(usuarioCadastrar.getDocumento_pessoal());
        usuario.setSenha(usuarioCadastrar.getSenha());
        return usuarioRepository.save(usuario);
    }

}
