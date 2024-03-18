package nl.abnamro.culinarycompass.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile({"dev", "prod"})
@AllArgsConstructor
public class SecurityConfiguration {

    private TestAuthenticationProvider testAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .formLogin(form -> {
                    form.defaultSuccessUrl("/swagger-ui.html");
                })
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(testAuthenticationProvider)
                .build();

    }


}
