package aes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootConfiguration
@SpringBootApplication
public class EncryptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EncryptionServiceApplication.class, args);
    }
}