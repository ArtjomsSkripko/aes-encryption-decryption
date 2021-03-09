package aes.jwt.authorization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;

public class TokenAuthenticationProvider implements AuthenticationProvider {

    private AuthTokenService tokenService;

    public TokenAuthenticationProvider(AuthTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        @SuppressWarnings("unchecked")
        Optional<String> token = (Optional<String>) authentication.getPrincipal();

        UserToken tokenContainer = null;
        try {
            tokenContainer = tokenService.validateToken(token.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tokenContainer != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (!StringUtils.isEmpty(tokenContainer.getUserRole())) {
                authorities.add(new SimpleGrantedAuthority(tokenContainer.getUserRole()));
            }
            PreAuthenticatedAuthenticationToken retAuthentication =
                    new PreAuthenticatedAuthenticationToken(tokenContainer, null, authorities);
            retAuthentication.setDetails(token.get());
            return retAuthentication;
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
