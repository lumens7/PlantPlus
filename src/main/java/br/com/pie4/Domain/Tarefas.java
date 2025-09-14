package br.com.pie4.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_tarefas")
public class Tarefas {

    public enum Repetir{
        DOMINGO, SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome_tarefa;

    @Column(columnDefinition = "TEXT")
    private String descricao_tarefa;
    private LocalDateTime hora_efetuar_atv;

    @ManyToMany
    @JoinTable(
            name = "tarefas_planta_user",
            joinColumns = @JoinColumn(name = "tarefa_id"),
            inverseJoinColumns = @JoinColumn(name = "planta_user_id")
    )
    private List<PlantaUser> plantaUser;


    @Enumerated(EnumType.STRING)
    private Repetir repetir;
    private LocalDateTime horario_efetuado_atv;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Tarefas() {
    }

    public Tarefas(Long id, String nome_tarefa, String descricao_tarefa, LocalDateTime hora_efetuar_atv, List<PlantaUser> plantaUser, Repetir repetir, LocalDateTime horario_efetuado_atv, Usuario usuario) {
        this.id = id;
        this.nome_tarefa = nome_tarefa;
        this.descricao_tarefa = descricao_tarefa;
        this.hora_efetuar_atv = hora_efetuar_atv;
        this.plantaUser = plantaUser;
        this.repetir = repetir;
        this.horario_efetuado_atv = horario_efetuado_atv;
        this.usuario = usuario;
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

    public List<PlantaUser> getPlantaUser() {
        return plantaUser;
    }

    public void setPlantaUser(List<PlantaUser> plantaUser) {
        this.plantaUser = plantaUser;
    }

    public Repetir getRepetir() {
        return repetir;
    }

    public void setRepetir(Repetir repetir) {
        this.repetir = repetir;
    }

    public LocalDateTime getHorario_efetuado_atv() {
        return horario_efetuado_atv;
    }

    public void setHorario_efetuado_atv(LocalDateTime horario_efetuado_atv) {
        this.horario_efetuado_atv = horario_efetuado_atv;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
