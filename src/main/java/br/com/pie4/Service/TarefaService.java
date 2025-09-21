package br.com.pie4.Service;

import br.com.pie4.DTO.TarefaDTO;
import br.com.pie4.Domain.PlantaUser;
import br.com.pie4.Domain.Tarefas;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Repository.PlantaUserRepository;
import br.com.pie4.Repository.TarefasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TarefaService {
    @Autowired
    private TarefasRepository tarefasRepository;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PlantaUserRepository plantaUserRepository;

    public Tarefas cadastrarTarefa(TarefaDTO tarefaDTO){
        Tarefas tarefas = new Tarefas();
        Usuario usuario = usuarioService.findById(tarefaDTO.getUsuarioId());
        if (usuario ==  null){
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
        tarefas.setUsuario(usuario);
        //pesquisar se o usuário tem plantas cadastradas
        List<PlantaUser> plantaUserAux = plantaUserRepository.findByUsuarioId(tarefaDTO.getUsuarioId());
        if (plantaUserAux.isEmpty() || plantaUserAux == null){
            throw new IllegalArgumentException("Usuário não possui plantas cadastradas!");
        }
        //fazer um for pegando todos os id's de plantas pesquisando dentro do banco de dados e setando em outra lista de PlantaUser

        List<PlantaUser> plantasSelecionadas = new ArrayList<>();
        for (Long idPlantaDTO : tarefaDTO.getPlantaUserIds()) {
            boolean encontrada = false;
            for (PlantaUser planta : plantaUserAux) {
                if (planta.getId().equals(idPlantaDTO)) {
                    plantasSelecionadas.add(planta);
                    encontrada = true;
                    break;
                }
            }
            if (!encontrada) {
                throw new IllegalArgumentException("Planta informada não pertence ao usuário: ID " + idPlantaDTO);
            }
        }
        tarefas.setNome_tarefa(tarefaDTO.getNome_tarefa());
        tarefas.setDescricao_tarefa(tarefaDTO.getDescricao_tarefa());
        tarefas.setHora_efetuar_atv(tarefaDTO.getHora_efetuar_atv());
        tarefas.setRepetir(tarefaDTO.getRepetir());
        tarefas.setHorario_efetuado_atv(tarefaDTO.getHorario_efetuado_atv());
        tarefas.setPlantaUser(plantasSelecionadas);
        return tarefasRepository.save(tarefas);
    }
    public Tarefas findById(Long id){
        return tarefasRepository.findById(id).get();
    }
    public List<Tarefas> findByNome(String nome, Long idUsuario){
        return tarefasRepository.findByNome_tarefaAndUsuarioId(nome, idUsuario);
    }
    public List<Tarefas> findByDiaSemana(Tarefas.Repetir dia, Long idUsuario){
        return tarefasRepository.findByRepetirDiaSemanaAndUsuarioId(dia, idUsuario);
    }
    public List<Tarefas> findByNomePlanta(String nome, Long idUsuario){
        List<PlantaUser> plantaUsers = plantaUserRepository.findByUsuarioIdAndPlantaCieNome(idUsuario, nome);
        List<Tarefas> resultado = new ArrayList<>();
        for (PlantaUser plantaUser : plantaUsers) {
            List<Tarefas> tarefasAssociadas = tarefasRepository.findAll();
            for (Tarefas tarefa : tarefasAssociadas) {
                if (tarefa.getPlantaUser().contains(plantaUser) && tarefa.getUsuario().getId().equals(idUsuario)) {
                    resultado.add(tarefa);
                }
            }
        }
        return resultado;
    }
    public List<Tarefas> findByUsuarioId(Long idUsuario){
        List<Tarefas> todasTarefas = tarefasRepository.findAll();
        List<Tarefas> tarefasUsuario = new ArrayList<>();
        for (Tarefas tarefa : todasTarefas) {
            if (tarefa.getUsuario().getId().equals(idUsuario)) {
                tarefasUsuario.add(tarefa);
            }
        }
        return tarefasUsuario;
    }
    public List<Tarefas> findAll(){
        return tarefasRepository.findAll();
    }
    public Tarefas alterar(TarefaDTO dto) {
        Tarefas tarefa = tarefasRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com id: " + dto.getId()));

        tarefa.setNome_tarefa(dto.getNome_tarefa());
        tarefa.setDescricao_tarefa(dto.getDescricao_tarefa());
        tarefa.setHora_efetuar_atv(dto.getHora_efetuar_atv());
        //tarefa.setHorario_efetuado_atv(dto.getHorario_efetuado_atv());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioService.findById(dto.getUsuarioId());
            tarefa.setUsuario(usuario);
        }

        if (dto.getPlantaUserIds() != null) {
            List<PlantaUser> plantasValidas = new ArrayList<>();
            for (Long plantaId : dto.getPlantaUserIds()) {
                plantaUserRepository.findById(plantaId).ifPresent(plantasValidas::add);
            }
            tarefa.setPlantaUser(plantasValidas);
        }

        if (dto.getRepetir() != null) {
            tarefa.setRepetir(dto.getRepetir());
        }

        return tarefasRepository.save(tarefa);
    }
    public void deleteTarefa(Long id, Long idUsuario) {
        Tarefas tarefa = tarefasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com id: " + id));

        if (!tarefa.getUsuario().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("Usuário não tem permissão para excluir esta tarefa!");
        }

        tarefasRepository.delete(tarefa);
    }

}
