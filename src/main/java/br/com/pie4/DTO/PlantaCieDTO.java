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
    private String nomeCientifico;
    private String especie;
    private String rega;
    private String poda;
    private String resumoDadosPlanta;
    private String urlFoto;

    public PlantaCieDTO() {
    }

    public PlantaCieDTO(Long id, String nome, String nome_cientifico, String especie, String rega, String poda, String resumo_dados_planta, String url_foto) {
        this.id = id;
        this.nome = nome;
        this.nomeCientifico = nome_cientifico;
        this.especie = especie;
        this.rega = rega;
        this.poda = poda;
        this.resumoDadosPlanta = resumo_dados_planta;
        this.urlFoto = url_foto;
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

    public String getNomeCientifico() {
        return nomeCientifico;
    }

    public void setNomeCientifico(String nomeCientifico) {
        this.nomeCientifico = nomeCientifico;
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

    public String getResumoDadosPlanta() {
        return resumoDadosPlanta;
    }

    public void setResumoDadosPlanta(String resumoDadosPlanta) {
        this.resumoDadosPlanta = resumoDadosPlanta;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}
