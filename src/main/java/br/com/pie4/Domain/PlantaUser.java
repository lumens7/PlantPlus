package br.com.pie4.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_planta_user")
public class PlantaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantidade;
    private Integer idade;

    @ManyToOne
    @JoinColumn(name = "id_planta_cie", nullable = false)
    private PlantaCie plantaCie;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public PlantaUser() {
    }

    public PlantaUser(Long id, Integer quantidade, Integer idade, PlantaCie plantaCie, Usuario usuario) {
        this.id = id;
        this.quantidade = quantidade;
        this.idade = idade;
        this.plantaCie = plantaCie;
        this.usuario = usuario;
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

    public PlantaCie getPlantaCie() {
        return plantaCie;
    }

    public void setPlantaCie(PlantaCie plantaCie) {
        this.plantaCie = plantaCie;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
