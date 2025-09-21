package br.com.pie4.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlantaUserDTO {
    private Long id;
    private Integer quantidade;
    private Integer idade;
    private Long id_usuario;
    private Long id_plantaCie;

    public PlantaUserDTO() {
    }

    public PlantaUserDTO(Long id, Integer quantidade, Integer idade, Long id_usuario, Long id_plantaCie) {
        this.id = id;
        this.quantidade = quantidade;
        this.idade = idade;
        this.id_usuario = id_usuario;
        this.id_plantaCie = id_plantaCie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Long getId_plantaCie() {
        return id_plantaCie;
    }

    public void setId_plantaCie(Long id_plantaCie) {
        this.id_plantaCie = id_plantaCie;
    }
}
