package br.com.pie4.Repository;

import br.com.pie4.Domain.TarefasFeitas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TarefasFeitasRepository extends JpaRepository<TarefasFeitas, Long> {

    @Query("SELECT t FROM TarefasFeitas t " +
            "WHERE t.id_tarefa = :id_tarefa " +
            "AND t.horario_efetuado_atv BETWEEN :inicio AND :fim")
    Optional<TarefasFeitas> findFeitaHoje(@Param("id_tarefa") Long id_tarefa,
                                          @Param("inicio") LocalDateTime inicio,
                                          @Param("fim") LocalDateTime fim);

}
