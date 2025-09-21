package br.com.pie4.Controller;

import br.com.pie4.DTO.UsuarioDTO;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO usuarioCadastrar){
        try {
            if (usuarioCadastrar.getSenha().length() < 8){
                return ResponseEntity.badRequest().body("A senha deve ter no minimo 8 caracteres!");
            }
            if (!usuarioCadastrar.getSenha().equals(usuarioCadastrar.getConfirmarSenha())){
                return ResponseEntity.badRequest().body("As duas senhas informadas são divergentes!");
            }

            Usuario usuario1 = usuarioService.cadastrarUsuario(usuarioCadastrar);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario1);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao iniciar cadastro: " + e.getMessage());
        }
    }

    @GetMapping("/pesquisar/id")
    public ResponseEntity<?> pesquisarIdUser(@RequestParam("id")Long id){
        try{
            Usuario usuario = usuarioService.findById(id);
            if(usuario == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Usuário encontrado com o id: "+ id);
            }
            return ResponseEntity.ok(usuario);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/nome")
    public ResponseEntity<?> pesquisarNomeUser(@RequestParam("nome")String nome){
        try{
            Usuario usuario = usuarioService.findByNome(nome);
            if(usuario == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Usuário encontrado com o nome: "+ nome);
            }
            return ResponseEntity.ok(usuario);

        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }

    @GetMapping("/pesquisar/documento")
    public ResponseEntity<?> pesquisarDocumentoUser(@RequestParam("documento")String documento){
        try{
            Usuario usuario = usuarioService.findByDocumento(documento);
            if(usuario == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Usuário encontrado com o documento: "+ documento);
            }
            return ResponseEntity.ok(usuario);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/mail")
    public ResponseEntity<?> pesquisarMailUser(@RequestParam("mail")String mail){
        try{
            Usuario usuario = usuarioService.findByMail(mail);
            if(usuario == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Usuário encontrado com o mail: "+ mail);
            }
            return ResponseEntity.ok(usuario);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }


}
