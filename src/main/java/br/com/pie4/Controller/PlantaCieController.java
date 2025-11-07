package br.com.pie4.Controller;

import br.com.pie4.DTO.PlantaCieDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Service.PlantaCieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping(value = "/api/plant/cie")
public class PlantaCieController {
    @Autowired
    private PlantaCieService plantaCieService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody PlantaCieDTO plantaCieDTO){
        try{
            if (plantaCieDTO.getNome().isEmpty() || plantaCieDTO.getNome() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome da planta é obrigatório!");
            }
            if (plantaCieDTO.getNomeCientifico().isEmpty() || plantaCieDTO.getNomeCientifico() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome cientifico da planta é obrigatório!");
            }
            if (plantaCieDTO.getEspecie().isEmpty() || plantaCieDTO.getEspecie() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Especie da planta é obrigatório!");
            }
            PlantaCie plantaCie = plantaCieService.cadastrar(plantaCieDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(plantaCieDTO);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/nome")
    public ResponseEntity<?> pesquisarNome(@RequestParam("nome")String nome){
        List<PlantaCie> plantaCies = plantaCieService.findByNome(nome);
        if (plantaCies == null || plantaCies.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma Planta Encontrada com o nome: "+ nome);
        }
        return ResponseEntity.ok(plantaCies);

    }
    @GetMapping("/pesquisar/nomeCientifico")
    public ResponseEntity<?> pesquisarCientifico(@RequestParam("nomeCientifico")String nome_cientifico){
        List<PlantaCie> plantaCies = plantaCieService.findByNomeCientifico(nome_cientifico);
        if (plantaCies == null || plantaCies.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma Planta Encontrada com o nome cientifico: "+ nome_cientifico);
        }
        return ResponseEntity.ok(plantaCies);
    }
    @GetMapping("/pesquisar/especie")
    public ResponseEntity<?> pesquisarEspecie(@RequestParam("especie")String especie){
        List<PlantaCie> plantaCies = plantaCieService.findByEspecie(especie);
        if (plantaCies == null || plantaCies.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma Planta Encontrada com a especie: "+ especie);
        }
        return ResponseEntity.ok(plantaCies);
    }
    @GetMapping("/pesquisar/todos")
    public ResponseEntity<?> pesquisarTodos(){
        List<PlantaCie> plantaCies = plantaCieService.findAll();
        if (plantaCies == null || plantaCies.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma Planta Encontrada! ");
        }
        return ResponseEntity.ok(plantaCies);
    }

    @PutMapping("/alterar")
    public ResponseEntity<?> alterar(@RequestBody PlantaCieDTO plantaCieDTO) {
        try {
            if (plantaCieDTO.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id da planta é obrigatório para alteração!");
            }

            PlantaCie plantaCie = plantaCieService.alterar(plantaCieDTO);
            return ResponseEntity.ok("Dados da planta cie alterado com sucesso!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletar(@RequestParam("id")Long id, @RequestParam("idUsuario")Long idUsuario){
        try{
            if (id == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id da planta é obrigatório para exclusão!");
            }
            plantaCieService.deletarPlantaCie(id, idUsuario);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }


}
