//package br.com.pie4.Initializer;
//
//import br.com.pie4.Domain.Roles;
//import br.com.pie4.Repository.RolesRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//
//@Component
//public class RoleInitializer implements CommandLineRunner {
//    private final RolesRepository rolesRepository;
//    public RoleInitializer(RolesRepository rolesRepository) {
//        this.rolesRepository = rolesRepository;
//    }
//    @Override
//    public void run(String... args) throws Exception {
//        Set<String> rolesPadrao = Set.of(
//                "ROLE_PLANTAS_CIE",
//                "ROLE_PLANTAS_USER",
//                "ROLE_CONTROLE_USUARIO",
//                "ROLE_USUARIO",
//                "ROLE_TAREFAS"
//        );
//        for (String roleNome : rolesPadrao) {
//            rolesRepository.findByNome(roleNome)
//                    .ifPresentOrElse(
//                            role -> System.out.println("Role já existe: " + roleNome),
//                            () -> {
//                                Roles novaRole = new Roles();
//                                novaRole.setNome(roleNome);
//                                rolesRepository.save(novaRole);
//                                System.out.println("Role criada: " + roleNome);
//                            }
//                    );
//        }
//    }
//}
