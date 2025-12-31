package br.com.locadora.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // Permitir que qualquer um veja a página de login e arquivos estáticos (se tiver)
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // Qualquer outra página exige autenticação (estar logado)
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login") // Diz para o Spring: "Use a MINHA tela, não a padrão"
                        .defaultSuccessUrl("/filmes", true) // Se logar com sucesso, vá para /filmes
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Se sair, volta pro login com aviso
                        .permitAll()
                );

        return http.build();
    }
}