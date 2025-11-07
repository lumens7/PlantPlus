package br.com.pie4.DTO;


public class PlantaCieTarefaDTO {
    private Long id;
    private String nome;
    private String url_foto;
    private Integer quantidade;

    public PlantaCieTarefaDTO() {
    }
    public PlantaCieTarefaDTO(Long id, String nome, String url_foto, Integer quantidade) {
        this.id = id;
        this.nome = nome;
        this.url_foto = url_foto;
        this.quantidade = quantidade;
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

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
