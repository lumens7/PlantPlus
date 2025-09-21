package br.com.pie4.DTO;

import br.com.pie4.Domain.Tarefas.Repetir;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarefaDTO {
    private Long id;
    private String nome_tarefa;
    private String descricao_tarefa;
    private LocalDateTime hora_efetuar_atv;
    private List<Long> plantaUserIds;
    private List<Repetir> repetir;
    private LocalDateTime horario_efetuado_atv;
    private Long usuarioId;

    public TarefaDTO() {
    }

    public TarefaDTO(Long id, String nome_tarefa, String descricao_tarefa, LocalDateTime hora_efetuar_atv, List<Long> plantaUserIds, List<Repetir> repetir, LocalDateTime horario_efetuado_atv, Long usuarioId) {
        this.id = id;
        this.nome_tarefa = nome_tarefa;
        this.descricao_tarefa = descricao_tarefa;
        this.hora_efetuar_atv = hora_efetuar_atv;
        this.plantaUserIds = plantaUserIds;
        this.repetir = repetir;
        this.horario_efetuado_atv = horario_efetuado_atv;
        this.usuarioId = usuarioId;
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

    public LocalDateTime getHora_efetuar_atv() {
        return hora_efetuar_atv;
    }

    public void setHora_efetuar_atv(LocalDateTime hora_efetuar_atv) {
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

    public LocalDateTime getHorario_efetuado_atv() {
        return horario_efetuado_atv;
    }

    public void setHorario_efetuado_atv(LocalDateTime horario_efetuado_atv) {
        this.horario_efetuado_atv = horario_efetuado_atv;
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
                ", horario_efetuado_atv=" + horario_efetuado_atv +
                '}';
    }
}
