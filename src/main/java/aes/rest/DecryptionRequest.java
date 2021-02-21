package aes.rest;

import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Decryption request")
public class DecryptionRequest {

    @ApiModelProperty(value = "Text to decrypt", position = 1)
    private String textToDecrypt;

    @ApiModelProperty(value = "Key for decryption", position = 2)
    private String key;

    public String getTextToDecrypt() {
        return textToDecrypt;
    }

    public void setTextToDecrypt(String textToDecrypt) {
        this.textToDecrypt = textToDecrypt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DecryptionRequest that = (DecryptionRequest) o;
        return Objects.equals(textToDecrypt, that.textToDecrypt) &&
            Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textToDecrypt, key);
    }

    @Override
    public String toString() {
        return "DecryptionRequest{" +
            "textToDecrypt='" + textToDecrypt + '\'' +
            ", key='" + key + '\'' +
            '}';
    }
}
