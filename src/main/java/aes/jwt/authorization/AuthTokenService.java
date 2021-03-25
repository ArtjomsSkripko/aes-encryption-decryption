package aes.jwt.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

    private final JWTokenHelper jwtTokenHelper;

    @Autowired
    public AuthTokenService(JWTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
    }

    public UserToken validateToken(String authTokenStr) throws Exception {
        UserToken authContext = jwtTokenHelper.parseAccessToken(authTokenStr);
        if (authContext.getValidationException() == null) {
            UserToken userTokenContainer = new UserToken();
            userTokenContainer.setToken(authTokenStr);
            userTokenContainer.setUsername(authContext.getUsername());
            userTokenContainer.setTokenId(authContext.getTokenId());
            userTokenContainer.setUserRole(authContext.getUserRole());
            userTokenContainer.setCustomerId(authContext.getCustomerId());
            return userTokenContainer;
        } else {
            throw authContext.getValidationException();
        }
    }
}
