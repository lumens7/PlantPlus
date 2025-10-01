package br.com.pie4.Service;

import br.com.pie4.DTO.PlantaCieDTO;
import br.com.pie4.DTO.PlantaUserDTO;
import br.com.pie4.DTO.UsuarioDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Domain.PlantaUser;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Repository.PlantaCieRepository;
import br.com.pie4.Repository.PlantaUserRepository;
import br.com.pie4.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PlantaUserService.class)
class PlantaUserServiceTest {

    @Autowired
    private PlantaUserService plantaUserService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlantaCieRepository plantaCieRepository;

    @Autowired
    private PlantaUserRepository plantaUserRepository;

    private Usuario usuarioBase;
    private PlantaCie plantaBase;
    private PlantaUser plantaUserBase;

    @BeforeEach
    void setUp() {
        plantaUserRepository.deleteAll();
        usuarioRepository.deleteAll();
        plantaCieRepository.deleteAll();

        // Criando usuário
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                "Luis",
                "12345678900",
                "luis@mail.com",
                "11999999999",
                "senha123",
                "senha123"
        );
        usuarioBase = usuarioRepository.save(new Usuario(
                null,
                usuarioDTO.getNome(),
                usuarioDTO.getDocumento_pessoal(),
                usuarioDTO.getMail(),
                usuarioDTO.getTelefone(),
                usuarioDTO.getSenha()
        ));

        // Criando planta científica
        PlantaCieDTO cieDTO = new PlantaCieDTO(
                null,
                "Lirio Aranha Vermelho",
                "Lycoris radiata",
                "Amaryllidaceae",
                "Deve ser moderada, mantendo o solo húmido, mas sem o encharcar, especialmente durante o crescimento e a floração, e reduzindo no verão quando a planta está dormente.",
                "A poda é mínima, sendo recomendável remover apenas folhas amareladas ou mortas após a floração, ou os caules e folhas secos assim que a folhagem começar a secar naturalmente, para evitar danos ao bulbo.",
                "planta vermelha",
                "https://p2.trrsf.com/image/fget/cf/1548/0/images.terra.com/2022/08/24/341557728-1-como-plantar-e-cuidar-de-lirio-aranha.webp"
        );
        plantaBase = plantaCieRepository.save(new PlantaCie(
                null,
                cieDTO.getNome(),
                cieDTO.getNomeCientifico(),
                cieDTO.getEspecie(),
                cieDTO.getRega(),
                cieDTO.getPoda(),
                cieDTO.getResumoDadosPlanta(),
                cieDTO.getUrlFoto()
        ));

        // Criando plantaUser
        PlantaUserDTO plantaUserDTO = new PlantaUserDTO(
                null,
                2,
                1,
                usuarioBase.getId(),
                plantaBase.getId()
        );
        plantaUserBase = plantaUserService.cadastrar(plantaUserDTO);
    }

    @Test
    void deveCadastrarPlantaUserComSucesso() {
        assertNotNull(plantaUserBase.getId());
        assertEquals(2, plantaUserBase.getQuantidade());
        assertEquals(1, plantaUserBase.getIdade());
        assertEquals(usuarioBase.getId(), plantaUserBase.getUsuario().getId());
        assertEquals(plantaBase.getId(), plantaUserBase.getPlantaCie().getId());
    }

    @Test
    void deveAlterarPlantaUserComSucesso() {
        PlantaUserDTO dto = new PlantaUserDTO(
                plantaUserBase.getId(),
                5,
                3,
                usuarioBase.getId(),
                plantaBase.getId()
        );

        PlantaUser alterada = plantaUserService.alterar(dto);

        assertEquals(5, alterada.getQuantidade());
        assertEquals(3, alterada.getIdade());
    }

    @Test
    void deveListarPlantasPorUsuario() {
        List<PlantaUser> lista = plantaUserService.findByUsuarioId(usuarioBase.getId());
        assertEquals(1, lista.size());
        assertEquals(usuarioBase.getId(), lista.get(0).getUsuario().getId());
    }

    @Test
    void deveListarPlantasPorNomeUsuario() {
        List<PlantaUser> lista = plantaUserService.findByPlantNomeUser("Lirio Aranha Vermelho", usuarioBase.getId());
        assertEquals(1, lista.size());
        assertEquals("Lirio Aranha Vermelho", lista.get(0).getPlantaCie().getNome());
    }

    @Test
    void deveListarPlantasPorNome() {
        List<PlantaUser> lista = plantaUserService.findByPlantNome("Lirio Aranha Vermelho");
        assertEquals(1, lista.size());
        assertEquals("Lirio Aranha Vermelho", lista.get(0).getPlantaCie().getNome());
    }

    @Test
    void deveDeletarPlantaUserComSucesso() {
        plantaUserService.deletarPlantaUser(plantaUserBase.getId(), usuarioBase.getId());
        assertEquals(0, plantaUserRepository.count());
    }

    @Test
    void deveLancarExcecaoAoDeletarDeOutroUsuario() {
        Usuario outroUsuario = usuarioRepository.save(new Usuario(null, "Maria", "98765432100", "maria@mail.com", "11888888888", "senha"));
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> plantaUserService.deletarPlantaUser(plantaUserBase.getId(), outroUsuario.getId()));
        assertTrue(ex.getMessage().contains("A planta não pertence ao usuário"));
    }
}
