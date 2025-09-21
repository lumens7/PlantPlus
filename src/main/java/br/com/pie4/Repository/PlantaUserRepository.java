package br.com.pie4.Repository;

import br.com.pie4.Domain.PlantaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantaUserRepository extends JpaRepository<PlantaUser, Long> {

    @Query("SELECT p FROM PlantaUser p WHERE p.usuario.id = :idUsuario")
    List<PlantaUser> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT p FROM PlantaUser p WHERE p.usuario.id = :idUsuario AND p.plantaCie.nome LIKE %:nomePlanta%")
    List<PlantaUser> findByUsuarioIdAndPlantaCieNome(@Param("idUsuario") Long idUsuario,
                                                     @Param("nomePlanta") String nomePlanta);

    @Query("SELECT p FROM PlantaUser p WHERE p.plantaCie.nome LIKE %:nomePlanta%")
    List<PlantaUser> findByPlantaCieNome(@Param("nomePlanta")String nome);
}
