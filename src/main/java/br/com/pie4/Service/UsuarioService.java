package br.com.pie4.Service;

import br.com.pie4.DTO.AlterPasswordUserDTO;
import br.com.pie4.DTO.PutUserDTO;
import br.com.pie4.DTO.RolesPutDTO;
import br.com.pie4.DTO.UsuarioDTO;
import br.com.pie4.Domain.Roles;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Repository.RolesRepository;
import br.com.pie4.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolesRepository rolesRepository;

    public Usuario cadastrarUsuario(UsuarioDTO usuarioCadastrar) {
        if (usuarioRepository.findByDocumento(usuarioCadastrar.getDocumento_pessoal()) != null) {
            throw new IllegalArgumentException("Documento já cadastrado em outro usuário!");
        }
        if (usuarioRepository.findByMail(usuarioCadastrar.getMail()) != null) {
            throw new IllegalArgumentException("E-mail já cadastrado em outro usuário!");
        }
        if (usuarioRepository.findByTelefone(usuarioCadastrar.getTelefone()) != null) {
            throw new IllegalArgumentException("Telefone já cadastrado em outro usuário!");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioCadastrar.getNome());
        usuario.setMail(usuarioCadastrar.getMail());
        usuario.setTelefone(usuarioCadastrar.getTelefone());
        usuario.setDocumento_pessoal(usuarioCadastrar.getDocumento_pessoal());
        usuario.setSenha(passwordEncoder.encode(usuarioCadastrar.getSenha()));

        Set<String> rolesPadrao = Set.of(
                "ROLE_PLANTAS_USER",
                "ROLE_USUARIO",
                "ROLE_TAREFAS"
        );

        Set<Roles> rolesUsuario = rolesPadrao.stream()
                .map(nome -> rolesRepository.findByNome(nome)
                        .orElseThrow(() -> new RuntimeException("Role não encontrada: " + nome)))
                .collect(Collectors.toSet());

        usuario.setRoles(rolesUsuario);

        return usuarioRepository.save(usuario);
    }

    public void alterarDadosUsuario(PutUserDTO putUserDTO) {
        Usuario usuario = usuarioRepository.findById(putUserDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o id: " + putUserDTO.getId()));

        Usuario usuarioComMesmoDocumento = usuarioRepository.findByDocumento(putUserDTO.getDocumento_pessoal());
        if (usuarioComMesmoDocumento != null && !usuarioComMesmoDocumento.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Documento já cadastrado em outro usuário!");
        }
        Usuario usuarioComMesmoEmail = usuarioRepository.findByMail(putUserDTO.getMail());
        if (usuarioComMesmoEmail != null && !usuarioComMesmoEmail.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("E-mail já cadastrado em outro usuário!");
        }
        Usuario usuarioComMesmoTelefone = usuarioRepository.findByTelefone(putUserDTO.getTelefone());
        if (usuarioComMesmoTelefone != null && !usuarioComMesmoTelefone.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Telefone já cadastrado em outro usuário!");
        }
        usuario.setNome(putUserDTO.getNome());
        usuario.setMail(putUserDTO.getMail());
        usuario.setTelefone(putUserDTO.getTelefone());
        usuario.setDocumento_pessoal(putUserDTO.getDocumento_pessoal());
        usuarioRepository.save(usuario);
    }

    public void alterarSenhaUsuario(AlterPasswordUserDTO alterPasswordUserDTO){
        Usuario usuario = usuarioRepository.findById(alterPasswordUserDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o id: " + alterPasswordUserDTO.getId()));
        Usuario usuarioComparar = usuarioRepository.findByMail(alterPasswordUserDTO.getEmail());
        if (usuarioComparar == null ) {
            throw new IllegalArgumentException("E-mail inválido!");
        }
        if (!usuarioComparar.getDocumento_pessoal().equals(alterPasswordUserDTO.getDocumento_pessoal())){
            throw new IllegalArgumentException("Documento pessoal inválido!");
        }
        if (!alterPasswordUserDTO.getNovaSenha().equals(alterPasswordUserDTO.getConfirmacaoNovaSenha())){
            throw new IllegalArgumentException("As senhas informadas são divergentes!");
        }
        usuario.setSenha(passwordEncoder.encode(alterPasswordUserDTO.getNovaSenha()));
        usuarioRepository.save(usuario);
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

    public Usuario findByDocumento(String documento) {
        Usuario usuario = usuarioRepository.findByDocumento(documento);
        if (usuario == null) {
            throw new IllegalArgumentException("Nenhum usuário encontrado com o documento informado.");
        }
        return usuario;
    }

    public Usuario findByMail(String mail) {
        Usuario usuario = usuarioRepository.findByMail(mail);
        if (usuario == null) {
            throw new IllegalArgumentException("Nenhum usuário encontrado com o e-mail informado.");
        }
        return usuario;
    }
    @Transactional
    public void alterarRolesUsuario(RolesPutDTO rolesPutDTO) {
        Usuario usuario = usuarioRepository.findById(rolesPutDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o id: " + rolesPutDTO.getId()));

        Set<Roles> novasRoles = new HashSet<>();
        for (Long idRole : rolesPutDTO.getIdsRoles()) {
            Roles role = rolesRepository.findById(idRole)
                    .orElseThrow(() -> new IllegalArgumentException("Role não encontrada com o id: " + idRole));
            novasRoles.add(role);
        }

        usuario.setRoles(novasRoles);
        usuarioRepository.save(usuario); // ✅ salva as alterações no banco
    }
    public Set<Roles> findAllRoles(){
        return new HashSet<>(rolesRepository.findAll());
    }
    public Set<Roles> findRolesByIdUsuario(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o id: " + idUsuario));
        return usuario.getRoles();
    }
}

