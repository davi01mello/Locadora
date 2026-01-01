package br.com.locadora.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. DESATIVA O CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 2. CONFIGURA AS PERMISSÕES
                .authorizeHttpRequests((requests) -> requests
                        // Libera arquivos estáticos (CSS, JS, Imagens) para a tela não ficar "quebrada"
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        // Libera a página de login
                        .requestMatchers("/login").permitAll()
                        // Todo o resto precisa de login (Inclusive a Home e a API da IA)
                        .anyRequest().authenticated()
                )

                // 3. CONFIGURA O LOGIN
                .formLogin((form) -> form
                        .loginPage("/login") // Usa sua tela bonita
                        .defaultSuccessUrl("/", true) // Vai pra Home ao logar
                        .permitAll()
                )

                // 4. CONFIGURA O LOGOUT
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}