package br.com.pie4.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlantaUserDTO {
    private Integer quantidade;
    private Integer idade;
    private UsuarioExDTO usuario;
    private PlantaCieDTO plantaCie;

    public PlantaUserDTO() {
    }

    public PlantaUserDTO(Integer quantidade, Integer idade, UsuarioExDTO usuario, PlantaCieDTO plantaCie) {
        this.quantidade = quantidade;
        this.idade = idade;
        this.usuario = usuario;
        this.plantaCie = plantaCie;
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

    public UsuarioExDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioExDTO usuario) {
        this.usuario = usuario;
    }

    public PlantaCieDTO getPlantaCie() {
        return plantaCie;
    }

    public void setPlantaCie(PlantaCieDTO plantaCie) {
        this.plantaCie = plantaCie;
    }
}
