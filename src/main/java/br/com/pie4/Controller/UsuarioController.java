package br.com.pie4.Controller;

import br.com.pie4.DTO.UsuarioDTO;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Repository.UsuarioRepository;
import br.com.pie4.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/api/user")
public class UsuarioController {

    @Autowired
    private Usuario usuario;
    @Autowired
    private UsuarioDTO usuarioDTO;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO usuarioCadastrar){
        try {
            if (usuarioCadastrar.getSenha().length() > 8){
                return ResponseEntity.badRequest().body("A senha deve ter no minimo 8 caracteres!");
            }
            if (usuarioCadastrar.getSenha() != usuarioCadastrar.getConfirmarSenha()){
                return ResponseEntity.badRequest().body("As duas senhas informadas são divergentes!");
            }

            usuarioService.cadastrarUsuario(usuarioCadastrar);
            return ResponseEntity.ok("Usuário cadastrado com Sucesso!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao iniciar cadastro: " + e.getMessage());
        }
    }

}
