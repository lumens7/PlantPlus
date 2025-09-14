package br.com.pie4.Service;

import br.com.pie4.DTO.PlantaCieDTO;
import br.com.pie4.Domain.PlantaCie;
import br.com.pie4.Repository.PlantaCieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlantaCieService {
    @Autowired
    private PlantaCieRepository plantaCieRepository;
    public PlantaCie cadastrar(PlantaCieDTO plantaCieDTO){
        if (!plantaCieRepository.findByNomeCientifico(plantaCieDTO.getNome_cientifico()).isEmpty()){
            throw new IllegalArgumentException("Nome cientifico já cadastrada!");
        }
        if (!plantaCieRepository.findByUrlFoto(plantaCieDTO.getUrl_foto()).isEmpty()){
            throw new IllegalArgumentException("Foto já está cadastrada!");
        }

        PlantaCie plantaCie = new PlantaCie();
        plantaCie.setNome(plantaCieDTO.getNome());
        plantaCie.setNomeCientifico(plantaCieDTO.getNome_cientifico());
        plantaCie.setEspecie(plantaCieDTO.getEspecie());
        plantaCie.setRega(plantaCieDTO.getRega());
        plantaCie.setPoda(plantaCieDTO.getPoda());
        plantaCie.setResumoDadosPlanta(plantaCieDTO.getResumo_dados_planta());
        plantaCie.setUrlFoto(plantaCieDTO.getUrl_foto());
        return plantaCieRepository.save(plantaCie);
    }
    public List<PlantaCie> findByNome(String nome){
        return plantaCieRepository.findByNome(nome);
    }
    public List<PlantaCie> findByNomeCientifico(String nome_cientifico){
        return plantaCieRepository.findByNomeCientifico(nome_cientifico);
    }
    public List<PlantaCie> findByEspecie(String especie){
        return plantaCieRepository.findByEspecie(especie);
    }
    public List<PlantaCie> findAll(){
        return plantaCieRepository.PesquisarTodasPlantasCie();
    }
    public PlantaCie findById(Long id_planta_cie){
        return plantaCieRepository.findById(id_planta_cie).get();
    }
}
