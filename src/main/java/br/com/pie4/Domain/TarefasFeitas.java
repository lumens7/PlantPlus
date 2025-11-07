package br.com.pie4.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_tarefas_feitas")
public class TarefasFeitas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_tarefa;
    private LocalDateTime horario_efetuado_atv;
    public TarefasFeitas(){

    }

    public TarefasFeitas(Long id, Long id_tarefa, LocalDateTime horario_efetuado_atv) {
        this.id = id;
        this.id_tarefa = id_tarefa;
        this.horario_efetuado_atv = horario_efetuado_atv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_tarefa() {
        return id_tarefa;
    }

    public void setId_tarefa(Long id_tarefa) {
        this.id_tarefa = id_tarefa;
    }

    public LocalDateTime getHorario_efetuado_atv() {
        return horario_efetuado_atv;
    }

    public void setHorario_efetuado_atv(LocalDateTime horario_efetuado_atv) {
        this.horario_efetuado_atv = horario_efetuado_atv;
    }
}
