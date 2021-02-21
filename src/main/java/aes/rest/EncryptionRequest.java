package aes.rest;

import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Encryption request")
public class EncryptionRequest {

    @ApiModelProperty(value = "Text to encrypt", position = 1)
    private String textToEncrypt;

    public String getTextToEncrypt() {
        return textToEncrypt;
    }

    public void setTextToEncrypt(String textToEncrypt) {
        this.textToEncrypt = textToEncrypt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptionRequest that = (EncryptionRequest) o;
        return Objects.equals(textToEncrypt, that.textToEncrypt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textToEncrypt);
    }

    @Override
    public String toString() {
        return "EncryptionRequest{" +
            "textToEncrypt='" + textToEncrypt + '\'' +
            '}';
    }
}
