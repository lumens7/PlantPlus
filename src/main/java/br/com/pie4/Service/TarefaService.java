package br.com.pie4.Service;

import br.com.pie4.DTO.PlantaCieTarefaDTO;
import br.com.pie4.DTO.TarefaDTO;
import br.com.pie4.DTO.TarefasFeitasDTO;
import br.com.pie4.Domain.*;
import br.com.pie4.Repository.PlantaUserRepository;
import br.com.pie4.Repository.TarefasFeitasRepository;
import br.com.pie4.Repository.TarefasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarefaService {
    @Autowired
    private TarefasRepository tarefasRepository;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PlantaUserRepository plantaUserRepository;
    @Autowired
    private TarefasFeitasRepository tarefasFeitasRepository;

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
        public List<TarefaDTO> findByUsuarioId(Long idUsuario) {
            List<Tarefas> tarefas = tarefasRepository.findByUsuarioId(idUsuario);

            return tarefas.stream().map(tarefa -> {
                TarefaDTO dto = new TarefaDTO();
                dto.setId(tarefa.getId());
                dto.setNome_tarefa(tarefa.getNome_tarefa());
                dto.setDescricao_tarefa(tarefa.getDescricao_tarefa());
                dto.setHora_efetuar_atv(tarefa.getHora_efetuar_atv());
                dto.setRepetir(tarefa.getRepetir());
                dto.setUsuarioId(tarefa.getUsuario().getId());

                if (tarefa.getPlantaUser() != null) {
                    List<Long> plantaIds = tarefa.getPlantaUser().stream()
                            .map(PlantaUser::getId)
                            .collect(Collectors.toList());
                    dto.setPlantaUserIds(plantaIds);

                    List<PlantaCieTarefaDTO> plantaDTOs = tarefa.getPlantaUser().stream()
                            .map(pu -> {
                                PlantaCie plantaCie = pu.getPlantaCie();
                                return new PlantaCieTarefaDTO(
                                        pu.getId(),
                                        plantaCie.getNome(),
                                        plantaCie.getUrlFoto(),
                                        pu.getQuantidade()
                                );
                            }).collect(Collectors.toList());
                    dto.setPlantas(plantaDTOs);
                }

                return dto;
            }).collect(Collectors.toList());
        }


    public List<Tarefas> findAll(){
        return tarefasRepository.findAll();
    }
    public TarefasFeitas tarefaFeita(Long id_tarefa, Long id_usuario) {
        Tarefas tarefa = tarefasRepository.findByIdAndUsuarioId(id_tarefa, id_usuario);
        if (tarefa == null) {
            throw new IllegalArgumentException("Tarefa não encontrada para este usuário!");
        }

        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.atTime(LocalTime.MAX);

        Optional<TarefasFeitas> feitaHoje = tarefasFeitasRepository.findFeitaHoje(id_tarefa, inicio, fim);
        if (feitaHoje.isPresent()) {
            throw new IllegalStateException("Esta tarefa já foi marcada como feita hoje!");
        }

        TarefasFeitas nova = new TarefasFeitas();
        nova.setId_tarefa(tarefa.getId());
        nova.setHorario_efetuado_atv(LocalDateTime.now());

        return tarefasFeitasRepository.save(nova);
    }


    public void deleteTarefaFeita(Long id_tarefa_feita){
        TarefasFeitas tarefaFeita = tarefasFeitasRepository.findById(id_tarefa_feita)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa feita não encontrada com id: " + id_tarefa_feita));
        tarefasFeitasRepository.delete(tarefaFeita);
    }
    public List<Tarefas> findTarefasFeitas(Long id_usuario){
        List<Tarefas> tarefasFeitasList = tarefasRepository.findByUsuarioId(id_usuario);
        List<TarefasFeitas> tarefasFeitasRecords = tarefasFeitasRepository.findAll();
        List<Tarefas> tarefasFeitasResultado = new ArrayList<>();
        for (Tarefas tarefa : tarefasFeitasList) {
            for (TarefasFeitas feita : tarefasFeitasRecords) {
                if (tarefa.getId().equals(feita.getId_tarefa())) {
                    tarefasFeitasResultado.add(tarefa);
                }
            }
        }
        return tarefasFeitasResultado;
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
