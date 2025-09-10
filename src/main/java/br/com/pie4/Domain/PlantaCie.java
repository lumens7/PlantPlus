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
@Table(name = "tb_planta_cie")
public class PlantaCie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String nome_cientifico;
    private String especie;
    private String rega;
    private String poda;
    private String resumo_dados_planta;
    private String url_foto;

}
