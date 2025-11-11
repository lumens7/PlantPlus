package br.com.pie4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://localhost:5500"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("*"));
                    config.setExposedHeaders(Arrays.asList("Authorization"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        //plantas cie permissões
                        .requestMatchers("/api/plant/cie/cadastrar").permitAll()//hasAuthority("ROLE_PLANTAS_CIE")
                        .requestMatchers("/api/plant/cie/pesquisar/**").permitAll() //deve conceder permissão pois os usuários devem conseguir pesquisar as plantas cadastradas
                        .requestMatchers("/api/plant/cie/alterar").hasAuthority("ROLE_PLANTAS_CIE")
                        .requestMatchers("/api/plant/cie/deletar").hasAuthority("ROLE_PLANTAS_CIE")

                        //plantas user permissões
                        .requestMatchers("/api/plant/user/cadastrar").hasAuthority("ROLE_PLANTAS_USER")
                        .requestMatchers("/api/plant/user/pesquisar/**").hasAuthority("ROLE_PLANTAS_USER")
                        .requestMatchers("/api/plant/user/alterar").hasAuthority("ROLE_PLANTAS_USER")
                        .requestMatchers("/api/plant/user/deletar").hasAuthority("ROLE_PLANTAS_USER")


                        //tarefas permissões
                        .requestMatchers("/api/tarefa/cadastrar").hasAuthority("ROLE_TAREFAS")
                        .requestMatchers("/api/tarefa/pesquisar/**").hasAuthority("ROLE_TAREFAS")
                        .requestMatchers("/api/tarefa/alterar").hasAuthority("ROLE_TAREFAS")
                        .requestMatchers("/api/tarefa/alterar/tarefa_feita").hasAuthority("ROLE_TAREFAS")
                        .requestMatchers("/api/tarefa/deletar/tarefa_feita").hasAuthority("ROLE_TAREFAS")
                        .requestMatchers("/api/tarefa/deletar").hasAuthority("ROLE_TAREFAS")
                        .requestMatchers("/api/tarefa/alterar/tarefa_feita").hasAuthority("ROLE_TAREFAS")

                        //usuário permissões
                        .requestMatchers("/api/user/cadastrar").permitAll()
                        //endpoints publicos por conta de usuários que esquecem a senha
                        .requestMatchers("/api/user/pesquisar/documento").permitAll()
                        .requestMatchers("/api/user/pesquisar/mail").permitAll()
                        .requestMatchers("/api/user/alterar/senha").permitAll()

                        .requestMatchers("/api/user/pesquisar/**").hasAuthority("ROLE_USUARIO")
                        .requestMatchers("/api/user/alterar").hasAuthority("ROLE_USUARIO")
                        .requestMatchers("/api/user/deletar").hasAuthority("ROLE_CONTROLE_USUARIO")
                        .requestMatchers("/api/user/role/**").hasAuthority("ROLE_CONTROLE_USUARIO")

                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
