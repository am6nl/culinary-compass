package nl.abnamro.culinarycompass.security;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserPrincipal {

    private String role;
    private String username;
    private String password;

}
