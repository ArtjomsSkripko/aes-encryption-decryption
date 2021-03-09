package aes.jwt.authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserToken {

    private String token;
    private String username;
    private String tokenId;
    private String userId;
    private String userRole;
    private Exception validationException;

    public UserToken(String username,
                     String userRole,
                     String userId) {
        this.username = username;
        this.userRole = userRole;
        this.userId = userId;
    }

    public UserToken() {
    }

    /* Getters & Setters */

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Exception getValidationException() {
        return validationException;
    }

    public void setValidationException(Exception validationException) {
        this.validationException = validationException;
    }
}
