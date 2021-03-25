package aes.service;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import aes.jwt.authorization.JWTokenHelper;
import aes.model.Customer;
import aes.model.NewCustomerRequest;
import aes.repository.AESRepository;
import com.nimbusds.jose.JOSEException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class AESService {

    @Autowired
    private JWTokenHelper jwTokenHelper;
    @Autowired
    private AESRepository repository;
    @Autowired
    private Environment env;

    /**
     * Encrypts text with AES algorithm. Before encryption is performed, service compresses text with Elias Delta algorithm.
     *
     * @param textToEncrypt text to encrypt
     * @return Triple of encrypted text, encryption key, error message (if exists)
     */
    public Triple<String, String, String> encryptText(String textToEncrypt) {
        String encryptedText = null;
        String key = null;
        String error = null;
        try {
            SecretKey secretKey = generateKey();
            encryptedText = performAESEncryption(textToEncrypt, secretKey, generateIv());
            key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            error = String.format("Error happened during encryption: %s", e.getMessage());
        }
        return Triple.of(encryptedText, key, error);
    }

    /**
     * Encrypts text with AES algorithm. Before encryption is performed, service compresses text with Elias Delta algorithm.
     *
     * @param textToEncrypt text to encrypt
     * @param key key
     * @return encrypted text
     */
    public String encryptTextWithKey(String textToEncrypt, String key) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return performAESEncryption(textToEncrypt, originalKey, generateIv());
    }

    /**
     * Decrypts text that was encrypted by AES algorithm. After text is decrypted, text is decompressed by Elias Delta algorithm
     *
     * @param textToDecrypt text to decrypt
     * @param key           key that was used for encryption
     * @return Pair of decrypted text and error message (if exists)
     */
    public Pair<String, String> decryptText(String textToDecrypt, String key) {
        String decryptedText = null;
        String error = null;
        try {
            byte[] decodedKey = Base64.getDecoder().decode(key);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            decryptedText = performAESDecryption(textToDecrypt, originalKey);
        } catch (Exception e) {
            error = String.format("Error happened during decryption - provided key or text is invalid. Failure message: %s", e.getMessage());
        }
        return Pair.of(decryptedText, error);
    }

    public String generateToken(String userName, String password) throws JOSEException {
        Customer foundCustomer = repository.findCustomer(userName);

        String actualPassword = foundCustomer == null ? null : decryptText(foundCustomer.getPassword(), String.valueOf(env.getProperty("aes.key"))).getKey();
        if (password == null || !password.equals(actualPassword)) {
            throw new SecurityException("Access denied");
        }
        return "Bearer " + jwTokenHelper.createJWT(foundCustomer.getCustomerId().toString(), foundCustomer.getUsername(),
            foundCustomer.getName(), foundCustomer.getSurname());
    }

    public Pair<String, Customer> createUser(NewCustomerRequest customerRequest) throws JOSEException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        Customer newCustomer = new Customer(customerRequest.getName(), customerRequest.getSurname(), customerRequest.getUsername(),
            encryptTextWithKey(customerRequest.getPassword(), String.valueOf(env.getProperty("aes.key"))), "REGULAR_USER");
        repository.addNewCustomer(newCustomer);
        Customer foundCustomer = repository.findCustomer(newCustomer.getUsername());

        String token = "Bearer " + jwTokenHelper.createJWT(String.valueOf(foundCustomer.getCustomerId()), foundCustomer.getUsername(),
            foundCustomer.getName(), foundCustomer.getSurname());

        foundCustomer.setPassword(customerRequest.getPassword());
        return Pair.of(token, foundCustomer);
    }

    /* Encryption and decryption specific methods */

    private SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    private String performAESEncryption(String textToEncrypt, SecretKey key, IvParameterSpec algorithmParameterSpec) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] ivBytes = algorithmParameterSpec.getIV();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, algorithmParameterSpec);
        byte[] cipherText = cipher.doFinal(textToEncrypt.getBytes());
        byte[] combinedBytes = new byte[ivBytes.length + cipherText.length];
        System.arraycopy(ivBytes, 0, combinedBytes, 0, ivBytes.length);
        System.arraycopy(cipherText, 0, combinedBytes, ivBytes.length, cipherText.length);
        return Base64.getEncoder().encodeToString(combinedBytes);
    }

    private String performAESDecryption(String cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] textToDecrypt = Base64.getDecoder().decode(cipherText);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] ivBytes = Arrays.copyOfRange(textToDecrypt, 0, 16);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
        byte[] decryptedTextBytes = cipher.doFinal(Arrays.copyOfRange(textToDecrypt, 16, textToDecrypt.length));

        return new String(decryptedTextBytes);
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
