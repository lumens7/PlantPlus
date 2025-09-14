package br.com.pie4.Controller;

import br.com.pie4.DTO.PlantaUserDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Domain.PlantaUser;
import br.com.pie4.Service.PlantaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/plant/user")
public class PlantaUserController {

    @Autowired
    private PlantaUserService plantaUserService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody PlantaUserDTO plantaUserDTO){
        try{
            PlantaUser plantaUser = plantaUserService.cadastrar(plantaUserDTO);
            return ResponseEntity.ok(plantaUser);
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
            Long id_usuario = 1L;
            List<PlantaUser> plantaUsers = plantaUserService.findByPlantNome(nome, id_usuario);
            if (plantaUsers.isEmpty() || plantaUsers == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Planta Encontrada com esse nome: "+ nome);
            }
            return ResponseEntity.ok(plantaUsers);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
}
