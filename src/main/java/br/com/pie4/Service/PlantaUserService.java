package br.com.pie4.Service;

import br.com.pie4.DTO.PlantaUserDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Domain.PlantaUser;
import br.com.pie4.Domain.Usuario;
import br.com.pie4.Repository.PlantaCieRepository;
import br.com.pie4.Repository.PlantaUserRepository;
import br.com.pie4.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantaUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PlantaCieRepository plantaCieRepository;
    @Autowired
    private PlantaUserRepository plantaUserRepository;

    public PlantaUser cadastrar(PlantaUserDTO plantaUserDTO){

        Usuario usuario = usuarioRepository.findById(plantaUserDTO.getId_usuario()).get();
        if (usuario == null){
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
        PlantaCie plantaCie = plantaCieRepository.findById(plantaUserDTO.getId_plantaCie()).get();
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
    public List<PlantaUser> findByPlantNomeUser(String nome, Long id){
        return plantaUserRepository.findByUsuarioIdAndPlantaCieNome(id, nome);
    }
    public List<PlantaUser> findByPlantNome(String nome){
        return plantaUserRepository.findByPlantaCieNome(nome);
    }
    public PlantaUser alterar(PlantaUserDTO plantaUserDTO) {
        PlantaUser plantaUser = plantaUserRepository.findById(plantaUserDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Planta do usuário não encontrada com id: " + plantaUserDTO.getId()));

        plantaUser.setQuantidade(plantaUserDTO.getQuantidade());
        plantaUser.setIdade(plantaUserDTO.getIdade());

        if (plantaUserDTO.getId_usuario() != null) {
            Usuario usuario = usuarioRepository.findById(plantaUserDTO.getId_usuario()).get();
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário não encontrado!");
            }
            plantaUser.setUsuario(usuario);
        }

        if (plantaUserDTO.getId_plantaCie()!= null) {
            PlantaCie plantaCie = plantaCieRepository.findById(plantaUserDTO.getId_plantaCie()).get();
            if (plantaCie == null) {
                throw new IllegalArgumentException("Espécie de planta não encontrada!");
            }
            plantaUser.setPlantaCie(plantaCie);
        }

        return plantaUserRepository.save(plantaUser);
    }
    public void deletarPlantaUser(Long id, Long idUsuario) {
        PlantaUser plantaUser = plantaUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Planta do usuário não encontrada com id: " + id));

        if (!plantaUser.getUsuario().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("A planta não pertence ao usuário com id: " + idUsuario);
        }
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        if (usuario == null){
            throw new IllegalArgumentException("Usuário não encontrado com id: " + idUsuario);
        }
//        if(!usuario.getRoles().contains("ADMIN")){
//            throw new IllegalArgumentException("Usuário não possui permissão para deletar plantas!");
//        }
        plantaUserRepository.delete(plantaUser);
    }

}
