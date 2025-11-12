package br.com.pie4.DTO;

import br.com.pie4.Domain.Tarefas.Repetir;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class TarefaDTO {
    private Long id;
    private String nome_tarefa;
    private String descricao_tarefa;
    private LocalTime hora_efetuar_atv;
    private List<Long> plantaUserIds;
    private List<Repetir> repetir;
    private Long usuarioId;
    private List<PlantaCieTarefaDTO> plantas;

    public TarefaDTO() {
    }

    public TarefaDTO(Long id, String nome_tarefa, String descricao_tarefa, LocalTime hora_efetuar_atv, List<Long> plantaUserIds, List<Repetir> repetir, Long usuarioId, List<PlantaCieTarefaDTO> plantas) {
        this.id = id;
        this.nome_tarefa = nome_tarefa;
        this.descricao_tarefa = descricao_tarefa;
        this.hora_efetuar_atv = hora_efetuar_atv;
        this.plantaUserIds = plantaUserIds;
        this.repetir = repetir;
        this.usuarioId = usuarioId;
        this.plantas = plantas;
    }


    public List<PlantaCieTarefaDTO> getPlantas() {
        return plantas;
    }

    public void setPlantas(List<PlantaCieTarefaDTO> plantas) {
        this.plantas = plantas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome_tarefa() {
        return nome_tarefa;
    }

    public void setNome_tarefa(String nome_tarefa) {
        this.nome_tarefa = nome_tarefa;
    }

    public String getDescricao_tarefa() {
        return descricao_tarefa;
    }

    public void setDescricao_tarefa(String descricao_tarefa) {
        this.descricao_tarefa = descricao_tarefa;
    }

    public LocalTime getHora_efetuar_atv() {
        return hora_efetuar_atv;
    }

    public void setHora_efetuar_atv(LocalTime hora_efetuar_atv) {
        this.hora_efetuar_atv = hora_efetuar_atv;
    }

    public List<Long> getPlantaUserIds() {
        return plantaUserIds;
    }

    public void setPlantaUserIds(List<Long> plantaUserIds) {
        this.plantaUserIds = plantaUserIds;
    }

    public List<Repetir> getRepetir() {
        return repetir;
    }

    public void setRepetir(List<Repetir> repetir) {
        this.repetir = repetir;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "TarefaDTO{" +
                "nome_tarefa='" + nome_tarefa + '\'' +
                ", descricao_tarefa='" + descricao_tarefa + '\'' +
                ", hora_efetuar_atv=" + hora_efetuar_atv +
                ", plantaUserIds=" + plantaUserIds +
                ", repetir=" + repetir +
                '}';
    }
}
