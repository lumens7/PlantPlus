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
@Table(name = "tb_tarefas")
public class Tarefas {

    public enum Repetir{
        DOMINGO, SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome_tarefa;
    private String descricao_tarefa;
    private LocalDateTime hora_efetuar_atv;

    @ManyToMany
    @JoinColumn(name = "id_planta_user", nullable = false)
    private PlantaUser plantaUser;

    @Enumerated(EnumType.STRING)
    private Repetir repetir;
    private LocalDateTime horario_efetuado_atv;

    @OneToMany
    private Usuario usuario;
}
