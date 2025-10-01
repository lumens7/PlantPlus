package br.com.pie4.Service;

import br.com.pie4.DTO.PlantaCieDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Repository.PlantaCieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PlantaCieService.class)
class PlantaCieServiceTest {

    @Autowired
    private PlantaCieService plantaCieService;

    @Autowired
    private PlantaCieRepository plantaCieRepository;

    private PlantaCie plantaBase;

    @BeforeEach
    void setUp() {
        plantaCieRepository.deleteAll();

        PlantaCieDTO dto = new PlantaCieDTO();
        dto.setNome("Rosa");
        dto.setNomeCientifico("Rosa rubiginosa");
        dto.setEspecie("Rosaceae");
        dto.setRega("2x por semana");
        dto.setPoda("Anual");
        dto.setResumoDadosPlanta("Planta ornamental");
        dto.setUrlFoto("http://exemplo.com/rosa.png");

        plantaBase = plantaCieService.cadastrar(dto);
    }

    @Test
    void deveCadastrarPlantaCieComSucesso() {
        assertNotNull(plantaBase.getId());
        assertEquals("Rosa", plantaBase.getNome());
        assertEquals(1, plantaCieRepository.findAll().size());
    }

    @Test
    void deveEncontrarPorNome() {
        List<PlantaCie> resultado = plantaCieService.findByNome("Rosa");
        assertEquals(1, resultado.size());
        assertEquals("Rosa", resultado.get(0).getNome());
    }

    @Test
    void deveEncontrarPorNomeCientifico() {
        List<PlantaCie> resultado = plantaCieService.findByNomeCientifico("Rosa rubiginosa");
        assertEquals(1, resultado.size());
        assertEquals("Rosa rubiginosa", resultado.get(0).getNomeCientifico());
    }

    @Test
    void deveEncontrarPorEspecie() {
        List<PlantaCie> resultado = plantaCieService.findByEspecie("Rosaceae");
        assertEquals(1, resultado.size());
        assertEquals("Rosaceae", resultado.get(0).getEspecie());
    }

    @Test
    void deveListarTodasPlantas() {
        List<PlantaCie> resultado = plantaCieService.findAll();
        assertEquals(1, resultado.size());
    }

    @Test
    void deveEncontrarPorId() {
        PlantaCie resultado = plantaCieService.findById(plantaBase.getId());
        assertNotNull(resultado);
        assertEquals("Rosa", resultado.getNome());
    }
    @Test
    void deveAlterarPlantaCieComSucesso() {

        PlantaCieDTO dto = new PlantaCieDTO();
        dto.setId(plantaBase.getId());
        dto.setNome("Rosa Vermelha");
        dto.setNomeCientifico("Rosa gallica");
        dto.setEspecie("Rosaceae");
        dto.setRega("3x por semana");
        dto.setPoda("Semestral");
        dto.setResumoDadosPlanta("Planta medicinal e ornamental");
        dto.setUrlFoto("http://exemplo.com/rosa-vermelha.png");

        // Act: chama o service de alteração
        PlantaCie plantaAlterada = plantaCieService.alterar(dto);

        // Assert: verifica se os dados foram realmente atualizados
        assertEquals("Rosa Vermelha", plantaAlterada.getNome());
        assertEquals("Rosa gallica", plantaAlterada.getNomeCientifico());
        assertEquals("Rosaceae", plantaAlterada.getEspecie());
        assertEquals("3x por semana", plantaAlterada.getRega());
        assertEquals("Semestral", plantaAlterada.getPoda());
        assertEquals("Planta medicinal e ornamental", plantaAlterada.getResumoDadosPlanta());
        assertEquals("http://exemplo.com/rosa-vermelha.png", plantaAlterada.getUrlFoto());
    }

    @Test
    void deveLancarExcecaoAoAlterarPlantaInexistente() {
        // Arrange: cria um DTO com id inexistente
        PlantaCieDTO dto = new PlantaCieDTO();
        dto.setId(999L); // id inexistente
        dto.setNome("Inexistente");

        // Act & Assert: espera IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            plantaCieService.alterar(dto);
        });

        assertTrue(exception.getMessage().contains("Planta não encontrada com id"));
    }

}
