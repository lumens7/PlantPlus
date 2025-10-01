package br.com.pie4.Service;

import br.com.pie4.DTO.UsuarioDTO;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(UsuarioDTO usuarioCadastrar){
        if (usuarioRepository.findByDocumento(usuarioCadastrar.getDocumento_pessoal()) != null){
            throw new IllegalArgumentException("Documento já cadastrado em outro usuário!");
        }
        if (usuarioRepository.findByMail(usuarioCadastrar.getMail()) != null){
            throw new IllegalArgumentException("E-mail já cadastrado em outro usuário!");
        }
        if (usuarioRepository.findByTelefone(usuarioCadastrar.getTelefone()) != null){
            throw new IllegalArgumentException("Telefone já cadastrado em outro usuário!");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioCadastrar.getNome());
        usuario.setMail(usuarioCadastrar.getMail());
        usuario.setTelefone(usuarioCadastrar.getTelefone());
        usuario.setDocumento_pessoal(usuarioCadastrar.getDocumento_pessoal());
        usuario.setSenha(usuarioCadastrar.getSenha());
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(Long id_usuario){
        return usuarioRepository.findById(id_usuario).orElse(null);
    }

    public Usuario findByNome(String nome){
        List<Usuario> usuarios = usuarioRepository.findByNome(nome);
        if (usuarios.isEmpty()) {
            return null;
        }
        if (usuarios.size() > 1){
            throw new IllegalArgumentException("Mais de um usuário cadastrado com o mesmo nome");
        }
        return usuarios.get(0);
    }

    public Usuario findByDocumento(String documento){
        Usuario usuarios = usuarioRepository.findByDocumento(documento);
        if (usuarios.getId() != null){
            throw new IllegalArgumentException("Este documento já foi cadastrado em outro usuário!");
        }
        return usuarios;
    }

    public Usuario findByMail(String mail){
        Usuario usuarios = usuarioRepository.findByMail(mail);
        if (usuarios.getId() != null){
            throw new IllegalArgumentException("Este e-mail já foi cadastrado em outro usuário!");
        }
        return usuarios;
    }
}

