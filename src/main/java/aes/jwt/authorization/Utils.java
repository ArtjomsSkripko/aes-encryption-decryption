package aes.jwt.authorization;

import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public static UserToken getServiceUser() {
        return isAuthenticated() && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserToken ?
                (UserToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
    }
}
