package aes.rest;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Encryption result")
public class EncryptionResponse {

    @ApiModelProperty(value = "Encrypted text", position = 1)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String encryptedText;

    @ApiModelProperty(value = "Encryption key", position = 2)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String encryptionKey;

    @ApiModelProperty(value = "Error message", position = 3)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public EncryptionResponse(String encryptedText, String encryptionKey, String error) {
        this.encryptedText = encryptedText;
        this.encryptionKey = encryptionKey;
        this.error = error;
    }

    public String getEncryptedText() {
        return encryptedText;
    }

    public void setEncryptedText(String encryptedText) {
        this.encryptedText = encryptedText;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptionResponse that = (EncryptionResponse) o;
        return Objects.equals(encryptedText, that.encryptedText) &&
            Objects.equals(encryptionKey, that.encryptionKey) &&
            Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encryptedText, encryptionKey, error);
    }
}
