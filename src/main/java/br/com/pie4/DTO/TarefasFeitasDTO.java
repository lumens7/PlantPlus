package br.com.pie4.DTO;

import java.time.LocalDateTime;

public class TarefasFeitasDTO {

    private Long id;
    private Long idTarefa;
    private LocalDateTime horarioEfetuado;

    public TarefasFeitasDTO(Long id, Long idTarefa, LocalDateTime horarioEfetuado) {
        this.id = id;
        this.idTarefa = idTarefa;
        this.horarioEfetuado = horarioEfetuado;
    }

    public Long getId() {
        return id;
    }

    public Long getIdTarefa() {
        return idTarefa;
    }

    public LocalDateTime getHorarioEfetuado() {
        return horarioEfetuado;
    }
}
