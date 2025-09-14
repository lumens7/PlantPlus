package br.com.pie4.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlantaCieDTO {
    private Long id;
    private String nome;
    private String nome_cientifico;
    private String especie;
    private String rega;
    private String poda;
    private String resumo_dados_planta;
    private String url_foto;

    public PlantaCieDTO() {
    }

    public PlantaCieDTO(Long id, String nome, String nome_cientifico, String especie, String rega, String poda, String resumo_dados_planta, String url_foto) {
        this.id = id;
        this.nome = nome;
        this.nome_cientifico = nome_cientifico;
        this.especie = especie;
        this.rega = rega;
        this.poda = poda;
        this.resumo_dados_planta = resumo_dados_planta;
        this.url_foto = url_foto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome_cientifico() {
        return nome_cientifico;
    }

    public void setNome_cientifico(String nome_cientifico) {
        this.nome_cientifico = nome_cientifico;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRega() {
        return rega;
    }

    public void setRega(String rega) {
        this.rega = rega;
    }

    public String getPoda() {
        return poda;
    }

    public void setPoda(String poda) {
        this.poda = poda;
    }

    public String getResumo_dados_planta() {
        return resumo_dados_planta;
    }

    public void setResumo_dados_planta(String resumo_dados_planta) {
        this.resumo_dados_planta = resumo_dados_planta;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }
}
