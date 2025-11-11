package br.com.pie4.Repository;

import br.com.pie4.Domain.PlantaUser;
import br.com.pie4.Domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome_usuario, '%'))")
    List<Usuario> findByNome(@Param("nome_usuario") String nome_usuario);
    @Query("SELECT u FROM Usuario u WHERE u.documento_pessoal = :documento_pessoal")
    Usuario findByDocumento(@Param("documento_pessoal") String documento_pessoal);
    @Query("SELECT u FROM Usuario u WHERE u.mail = :mail")
    Usuario findByMail(@Param("mail") String mail);

    @Query("SELECT u FROM Usuario u WHERE u.telefone = :telefone")
    Usuario findByTelefone(@Param("telefone") String telefone);
}
