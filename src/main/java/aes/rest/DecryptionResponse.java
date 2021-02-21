package aes.rest;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Decryption result")
public class DecryptionResponse {

    @ApiModelProperty(value = "Decrypted text", position = 1)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String decryptedText;

    @ApiModelProperty(value = "Error message", position = 2)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public DecryptionResponse(String decryptedText, String error) {
        this.decryptedText = decryptedText;
        this.error = error;
    }

    public String getDecryptedText() {
        return decryptedText;
    }

    public void setDecryptedText(String decryptedText) {
        this.decryptedText = decryptedText;
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
        DecryptionResponse that = (DecryptionResponse) o;
        return Objects.equals(decryptedText, that.decryptedText) &&
            Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decryptedText, error);
    }
}
