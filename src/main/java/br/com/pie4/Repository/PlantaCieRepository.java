package br.com.pie4.Repository;

import br.com.pie4.Domain.PlantaCie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantaCieRepository extends JpaRepository<Long, PlantaCie> {
}
