package br.com.pie4.Service;

import br.com.pie4.DTO.PlantaCieDTO;
import br.com.pie4.DTO.PlantaUserDTO;
import br.com.pie4.DTO.TarefaDTO;
import br.com.pie4.DTO.UsuarioDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Domain.PlantaUser;
import br.com.pie4.Domain.Tarefas;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Repository.PlantaCieRepository;
import br.com.pie4.Repository.PlantaUserRepository;
import br.com.pie4.Repository.TarefasRepository;
import br.com.pie4.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TarefaService.class, UsuarioService.class})
class TarefaServiceTest {

    @Autowired
    private TarefaService tarefaService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PlantaCieRepository plantaCieRepository;
    @Autowired
    private PlantaUserRepository plantaUserRepository;
    @Autowired
    private TarefasRepository tarefasRepository;

    private Usuario usuarioBase;
    private PlantaUser plantaUserBase;
    private PlantaCie plantaBase;
    private Tarefas tarefaBase;

    @BeforeEach
    void setUp() {
        tarefasRepository.deleteAll();
        plantaUserRepository.deleteAll();
        usuarioRepository.deleteAll();
        plantaCieRepository.deleteAll();

        // Usuário
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                "Luis", "12345678900", "luis@mail.com",
                "11999999999", "senha123", "senha123"
        );
        usuarioBase = usuarioRepository.save(new Usuario(
                null,
                usuarioDTO.getNome(),
                usuarioDTO.getDocumento_pessoal(),
                usuarioDTO.getMail(),
                usuarioDTO.getTelefone(),
                usuarioDTO.getSenha()
        ));

        // Planta científica
        PlantaCieDTO cieDTO = new PlantaCieDTO(
                null, "Lirio Aranha Vermelho", "Lycoris radiata", "Amaryllidaceae",
                "Rega moderada", "Poda mínima", "planta vermelha", "https://foto.com"
        );
        plantaBase = plantaCieRepository.save(new PlantaCie(
                null, cieDTO.getNome(), cieDTO.getNome_cientifico(), cieDTO.getEspecie(),
                cieDTO.getRega(), cieDTO.getPoda(), cieDTO.getResumo_dados_planta(), cieDTO.getUrl_foto()
        ));

        // Planta do usuário
        PlantaUserDTO plantaUserDTO = new PlantaUserDTO(
                null,
                2,
                1,
                usuarioBase.getId(),
                plantaBase.getId());
        PlantaUser plantaUserBaseTeste = new PlantaUser(
                null,
                plantaUserDTO.getQuantidade(),
                plantaUserDTO.getIdade(),
                plantaBase,
                usuarioBase
        );
        plantaUserBase = plantaUserRepository.save(plantaUserBaseTeste);

        // Criando tarefa base
        TarefaDTO tarefaDTO = new TarefaDTO(
                null,
                "Regar as plantas",
                "Regar as plantas do jardim",
                LocalDateTime.now().plusDays(1),
                List.of(plantaUserBase.getId()),
                new ArrayList<>(List.of(Tarefas.Repetir.SEGUNDA, Tarefas.Repetir.QUARTA)),
                null,
                usuarioBase.getId()
        );
        tarefaBase = tarefaService.cadastrarTarefa(tarefaDTO);
    }

    @Test
    void deveCadastrarTarefaComSucesso() {
        assertNotNull(tarefaBase.getId());
        assertEquals("Regar as plantas", tarefaBase.getNome_tarefa());
        assertEquals(usuarioBase.getId(), tarefaBase.getUsuario().getId());
        assertEquals(1, tarefaBase.getPlantaUser().size());
    }

    @Test
    void deveAlterarTarefaComSucesso() {
        TarefaDTO dto = new TarefaDTO(
                tarefaBase.getId(),
                "Podar plantas",
                "Podar as plantas mortas",
                LocalDateTime.now().plusDays(2),
                List.of(plantaUserBase.getId()),
                new ArrayList<>(List.of(Tarefas.Repetir.SEXTA)),
                null,
                usuarioBase.getId()
        );
        Tarefas alterada = tarefaService.alterar(dto);
        assertEquals("Podar plantas", alterada.getNome_tarefa());
        assertEquals("Podar as plantas mortas", alterada.getDescricao_tarefa());
    }

    @Test
    void deveListarTarefasPorUsuario() {
        List<Tarefas> lista = tarefaService.findByUsuarioId(usuarioBase.getId());
        assertEquals(1, lista.size());
        assertEquals(usuarioBase.getId(), lista.get(0).getUsuario().getId());
    }

    @Test
    void deveDeletarTarefaComSucesso() {
        tarefaService.deleteTarefa(tarefaBase.getId(), usuarioBase.getId());
        assertEquals(0, tarefasRepository.count());
    }

    @Test
    void deveLancarExcecaoAoDeletarDeOutroUsuario() {
        Usuario outro = usuarioRepository.save(new Usuario(null, "Maria", "98765432100", "maria@mail.com", "11888888888", "senha"));
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> tarefaService.deleteTarefa(tarefaBase.getId(), outro.getId()));
        assertTrue(ex.getMessage().contains("Usuário não tem permissão"));
    }
}
