package br.com.pie4.Initializer;

import br.com.pie4.Domain.Roles;
import br.com.pie4.Repository.RolesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleInitializer implements CommandLineRunner {
    private final RolesRepository rolesRepository;
    public RoleInitializer(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        List<String> rolesPadrao = List.of(
                "ROLE_CADASTRO_PLANTAS_CIE",
                "ROLE_PESQUISA_PLANTAS_CIE",
                "ROLE_ALTERAR_PLANTAS_CIE",
                "ROLE_DELETAR_PLANTAS_CIE",

                "ROLE_CADASTRO_PLANTAS_USER",
                "ROLE_PESQUISA_PLANTAS_USER",
                "ROLE_ALTERAR_PLANTAS_USER",
                "ROLE_DELETAR_PLANTAS_USER",

                "ROLE_CADASTRO_USUARIO",
                "ROLE_PESQUISA_USUARIO",
                "ROLE_ALTERAR_USUARIO",
                "ROLE_DELETAR_USUARIO",

                "ROLE_CADASTRO_TAREFAS",
                "ROLE_PESQUISA_TAREFAS",
                "ROLE_ALTERAR_TAREFAS",
                "ROLE_DELETAR_TAREFAS"
        );
        for (String roleNome : rolesPadrao) {
            rolesRepository.findByNome(roleNome)
                    .ifPresentOrElse(
                            role -> System.out.println("Role já existe: " + roleNome),
                            () -> {
                                Roles novaRole = new Roles();
                                novaRole.setNome(roleNome);
                                rolesRepository.save(novaRole);
                                System.out.println("Role criada: " + roleNome);
                            }
                    );
        }
    }
}
