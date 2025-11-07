package br.com.pie4.Repository;

import br.com.pie4.Domain.Tarefas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefasRepository extends JpaRepository<Tarefas, Long> {

    @Query("SELECT t FROM Tarefas t WHERE t.nome_tarefa ILIKE %:nome% AND t.usuario.id = :idUsuario")
    List<Tarefas> findByNome_tarefaAndUsuarioId(String nome, Long idUsuario);
    @Query("SELECT t FROM Tarefas t JOIN t.repetir r WHERE r = :diaSemana AND t.usuario.id = :idUsuario")
    List<Tarefas> findByRepetirDiaSemanaAndUsuarioId(Tarefas.Repetir diaSemana, Long idUsuario);
    List<Tarefas> findByUsuarioId(Long usuarioId);

    @Query("SELECT t FROM Tarefas t WHERE t.id = :id_tarefa AND t.usuario.id = :id_usuario")
    Tarefas findByIdAndUsuarioId(Long id_tarefa, Long id_usuario);
}
