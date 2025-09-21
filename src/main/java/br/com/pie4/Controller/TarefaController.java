package br.com.pie4.Controller;

import br.com.pie4.DTO.TarefaDTO;
import br.com.pie4.Domain.Tarefas;
import br.com.pie4.Service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tarefa")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarTarefa(@RequestBody TarefaDTO tarefaDTO ){
        try{
            if (tarefaDTO.getUsuarioId() == null) {
                return ResponseEntity.badRequest().body("Usuário não encontrado!");
            }
            if (tarefaDTO.getPlantaUserIds() == null || tarefaDTO.getPlantaUserIds().isEmpty()) {
                return ResponseEntity.badRequest().body("É necessário associar a tarefa a pelo menos uma planta!");
            }
            Tarefas tarefas = tarefaService.cadastrarTarefa(tarefaDTO);
//            return ResponseEntity.ok("Tarefa cadastrada com sucesso!\n"+tarefaDTO.toString());
           return ResponseEntity.status(HttpStatus.CREATED).body(tarefas);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao iniciar cadastro: " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/id")
    public ResponseEntity<?> pesquisarIdTarefa(@RequestParam("id")Long id){
        try{
            Tarefas tarefas = tarefaService.findById(id);
            if(tarefas == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Tarefa encontrada com o id: "+ id);
            }
            return ResponseEntity.ok(tarefas);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/nome")
    public ResponseEntity<?> pesquisarNomeTarefa(@RequestParam("nome")String nome, @RequestParam("idUsuario")Long idUsuario){
        try{
            List<Tarefas> tarefas = tarefaService.findByNome(nome, idUsuario);
            if(tarefas == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Tarefa encontrada com o nome: "+ nome);
            }
            return ResponseEntity.ok(tarefas);

        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/dia")
    public ResponseEntity<?> pesquisarDiaTarefa(@RequestParam("dia")Tarefas.Repetir dia, @RequestParam("idUsuario")Long idUsuario){
        try{
            List<Tarefas> tarefas = tarefaService.findByDiaSemana(dia, idUsuario);
            if (tarefas.isEmpty() || tarefas == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma tarefa cadastrada para esse dia da semana!");
            }
            return ResponseEntity.ok(tarefas);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/planta")
    public ResponseEntity<?> pesquisarPlantaTarefa(@RequestParam("nomePlanta")String nomePlanta, @RequestParam("idUsuario")Long idUsuario){
        try{
            List<Tarefas> tarefas = tarefaService.findByNomePlanta(nomePlanta, idUsuario);
            if (tarefas.isEmpty() || tarefas == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma tarefa cadastrada para essa planta!");
            }
            return ResponseEntity.ok(tarefas);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/usuario")
    public ResponseEntity<?> pesquisarUsuarioTarefa(@RequestParam("idUsuario")Long idUsuario){
        try{
            List<Tarefas> tarefas = tarefaService.findByUsuarioId(idUsuario);
            if (tarefas.isEmpty() || tarefas == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma tarefa cadastrada para esse usuário!");
            }
            return ResponseEntity.ok(tarefas);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @GetMapping("/pesquisar/todos")
    public ResponseEntity<?> pesquisarTodasTarefas(){
        try{
            List<Tarefas> tarefas = tarefaService.findAll();
            if (tarefas.isEmpty() || tarefas == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma tarefa cadastrada!");
            }
            return ResponseEntity.ok(tarefas);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno:  " + e.getMessage());
        }
    }
    @PutMapping("/alterar")
    public ResponseEntity<?> alterar(@RequestBody TarefaDTO tarefaDTO) {
        try {
            Tarefas tarefa = tarefaService.alterar(tarefaDTO);
            return ResponseEntity.ok(tarefa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletarTarefa(@RequestParam("id")Long id, @RequestParam("idUsuario")Long idUsuario) {
        try {
            if (id == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id da tarefa é obrigatório para exclusão!");
            }
            tarefaService.deleteTarefa(id, idUsuario);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar tarefa: " + e.getMessage());
        }
    }

}
