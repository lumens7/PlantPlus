package br.com.pie4.Repository;

import br.com.pie4.Domain.PlantaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantaUserRepository extends JpaRepository<PlantaUser, Long> {

    @Query("SELECT p FROM PlantaUser p WHERE p.Usuario.id = :idUsuario")
    List<PlantaUser> findByUsuarioId(@Param("idUsuario") Long idUsuario);
    @Query("SELECT p FROM PlantaUser p WHERE p.Usuario.id = :idUsuario AND p.PlantaCie.nome LIKE %:nome_planta%")
    List<PlantaUser> findByUsuarioIdAndPlantaCieNome(@Param("id_usuario") Long id_usuario, @Param("nome_planta") String nome_planta);


}
