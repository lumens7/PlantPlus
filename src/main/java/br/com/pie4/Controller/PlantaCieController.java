package br.com.pie4.Controller;

import br.com.pie4.DTO.PlantaCieDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Service.PlantaCieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/plant/cie")
public class PlantaCieController {
    @Autowired
    private PlantaCieService plantaCieService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody PlantaCieDTO plantaCieDTO){
        try{
            if (plantaCieDTO.getNome().isEmpty() || plantaCieDTO.getNome() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nome da planta é obrigatório!");
            }
            if (plantaCieDTO.getNome_cientifico().isEmpty() || plantaCieDTO.getNome_cientifico() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nome cientifico da planta é obrigatório!");
            }
            if (plantaCieDTO.getEspecie().isEmpty() || plantaCieDTO.getEspecie() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Especie da planta é obrigatório!");
            }
            PlantaCie plantaCie = plantaCieService.cadastrar(plantaCieDTO);
            return ResponseEntity.ok(plantaCie);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/nome")
    public ResponseEntity<?> pesquisarNome(@RequestParam("nome")String nome){
        List<PlantaCie> plantaCies = plantaCieService.findByNome(nome);
        if (plantaCies == null || plantaCies.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Planta Encontrada com o nome: "+ nome);
        }
        return ResponseEntity.ok(plantaCies);

    }
    @GetMapping("/pesquisar/cientifico")
    public ResponseEntity<?> pesquisarCientifico(@RequestParam("nome_cientifico")String nome_cientifico){
        List<PlantaCie> plantaCies = plantaCieService.findByNomeCientifico(nome_cientifico);
        if (plantaCies == null || plantaCies.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Planta Encontrada com o nome cientifico: "+ nome_cientifico);
        }
        return ResponseEntity.ok(plantaCies);
    }
    @GetMapping("/pesquisar/especie")
    public ResponseEntity<?> pesquisarEspecie(@RequestParam("especie")String especie){
        List<PlantaCie> plantaCies = plantaCieService.findByEspecie(especie);
        if (plantaCies == null || plantaCies.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Planta Encontrada com a especie: "+ especie);
        }
        return ResponseEntity.ok(plantaCies);
    }
    @GetMapping("/pesquisar/todos")
    public ResponseEntity<?> pesquisarTodos(){
        List<PlantaCie> plantaCies = plantaCieService.findAll();
        if (plantaCies == null || plantaCies.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Planta Encontrada! ");
        }
        return ResponseEntity.ok(plantaCies);
    }

    /*@PostMapping("/alterar")
    public ResponseEntity<?> alterar(@RequestBody )*/
}
