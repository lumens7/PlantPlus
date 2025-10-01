package br.com.pie4.Controller;

import br.com.pie4.DTO.PlantaUserDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Domain.PlantaUser;
import br.com.pie4.Service.PlantaCieService;
import br.com.pie4.Service.PlantaUserService;
import br.com.pie4.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping(value = "/api/plant/user")
public class PlantaUserController {

    @Autowired
    private PlantaUserService plantaUserService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody PlantaUserDTO plantaUserDTO){
        try{
            PlantaUser plantaUser = plantaUserService.cadastrar(plantaUserDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(plantaUserDTO);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }

    @GetMapping("/pesquisar/plant_user")
    public ResponseEntity<?> pesquisarPlantUser(@RequestParam("id_plant_user")Long id_plant_user){
        try{
            List<PlantaUser> plantaUsers = plantaUserService.findByUsuarioId(id_plant_user);
            if (plantaUsers.isEmpty() || plantaUsers == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O usuário ainda não cadastrou nenhuma planta!");
            }
            return ResponseEntity.ok(plantaUsers);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }

    @GetMapping("/pesquisar/nome")
    public ResponseEntity<?> pesquisarPlantNome(@RequestParam("nome")String nome){
        try{
            //este metodo pesquisa a planta pelo nome dado pelo administrador, o qual o usuário setou quando escolheu a especie
            Long id_usuario = 1L;
            List<PlantaUser> plantaUsers = plantaUserService.findByPlantNomeUser(nome, id_usuario);
            if (plantaUsers.isEmpty() || plantaUsers == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Planta Encontrada com esse nome: "+ nome);
            }
            return ResponseEntity.ok(plantaUsers);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @PutMapping("/alterar")
    public ResponseEntity<?> alterar(@RequestBody PlantaUserDTO plantaUserDTO) {
        try {
            PlantaUser plantaUser = plantaUserService.alterar(plantaUserDTO);
            return ResponseEntity.ok(plantaUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletar(@RequestParam("id") Long id, @RequestParam("idUsuario") Long idUsuario) {
        try {
            if (id == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id da Planta é obrigatório para exclusão!");
            }
            plantaUserService.deletarPlantaUser(id, idUsuario);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}
