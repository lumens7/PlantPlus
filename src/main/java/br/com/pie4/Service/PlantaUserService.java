package br.com.pie4.Service;

import br.com.pie4.DTO.PlantaUserDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Domain.PlantaUser;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Repository.PlantaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantaUserService {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PlantaCieService plantaCieService;
    @Autowired
    private PlantaUserRepository plantaUserRepository;

    public PlantaUser cadastrar(PlantaUserDTO plantaUserDTO){

        Usuario usuario = usuarioService.findById(plantaUserDTO.getUsuario().getId());
        if (usuario == null){
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
        PlantaCie plantaCie = plantaCieService.findById(plantaUserDTO.getPlantaCie().getId());
        if (plantaCie == null){
            throw new IllegalArgumentException("Especie de planta não encontrada!");
        }
        PlantaUser plantaUser = new PlantaUser();
        plantaUser.setQuantidade(plantaUserDTO.getQuantidade());
        plantaUser.setIdade(plantaUserDTO.getIdade());
        plantaUser.setUsuario(usuario);
        plantaUser.setPlantaCie(plantaCie);
        return plantaUserRepository.save(plantaUser);
    }

    public List<PlantaUser> findByUsuarioId(Long idUsuario) {
        return plantaUserRepository.findByUsuarioId(idUsuario);
    }
    public List<PlantaUser> findByPlantNome(String nome, Long id){
        return plantaUserRepository.findByUsuarioIdAndPlantaCieNome(id, nome);
    }

}
