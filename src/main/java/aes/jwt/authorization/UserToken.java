package aes.jwt.authorization;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserToken {

    private String token;
    private String username;
    private String name;
    private String surname;
    private String tokenId;
    private String customerId;
    private String userRole;
    private ZonedDateTime validTo;
    private Exception validationException;

    public UserToken(String username,
                     String userRole,
                     String customerId,
                     String name,
                     String surname,
                     ZonedDateTime validTo
                     ) {
        this.username = username;
        this.userRole = userRole;
        this.customerId = customerId;
        this.surname = name;
        this.surname = surname;
        this.validTo = validTo;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public ZonedDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(ZonedDateTime validTo) {
        this.validTo = validTo;
    }
}
