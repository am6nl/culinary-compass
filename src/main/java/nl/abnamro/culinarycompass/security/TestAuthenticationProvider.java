package nl.abnamro.culinarycompass.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class TestAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        return userRepository.getUsers().stream()
                .filter(u -> u.getUsername().equals(authentication.getName()))
                .findFirst()
                .map(u -> {
                    var sample = User.withUsername(u.getUsername())
                            .password(u.getPassword())
                            .roles(u.getRole())
                            .build();
                    return new UsernamePasswordAuthenticationToken(sample, null, sample.getAuthorities());
                })
                .orElse(null);

    }

    @Override
    public boolean supports(Class<?> authentication) {

        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);

    }

}
