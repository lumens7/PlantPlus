package br.com.pie4.Repository;

import br.com.pie4.Domain.PlantaCie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantaCieRepository extends JpaRepository<PlantaCie, Long> {

    List<PlantaCie> findByEspecie(String especie);

    @Query("SELECT p FROM PlantaCie p WHERE p.nome LIKE %:nome%")
    List<PlantaCie> findByNome(@Param("nome") String nome);

    @Query("SELECT p FROM PlantaCie p WHERE p.nomeCientifico LIKE %:nomeCientifico%")
    List<PlantaCie> findByNomeCientifico(@Param("nomeCientifico") String nomeCientifico);

    List<PlantaCie> findByUrlFoto(String urlFoto);

    @Query("SELECT p FROM PlantaCie p")
    List<PlantaCie> PesquisarTodasPlantasCie();


}
