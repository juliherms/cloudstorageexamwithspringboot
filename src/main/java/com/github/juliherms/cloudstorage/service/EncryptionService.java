package com.github.juliherms.cloudstorage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * This class responsible to implements business logic for encription
 * @author jlv
 *
 */
@Service
public class EncryptionService {
	
	private final Logger logger = LoggerFactory.getLogger(EncryptionService.class);

	/**
	 * Method responsible to encript data
	 * @param data
	 * @param key
	 * @return
	 */
    public String encryptValue(String data, String key) {
    	
        byte[] encryptedValue = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedValue = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e.getMessage());
        }

        return Base64.getEncoder().encodeToString(encryptedValue);
    }
    
    /**
     * Method responsible to decript value
     * @param data
     * @param key
     * @return
     */
    public String decryptValue(String data, String key) {
        byte[] decryptedValue = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedValue = cipher.doFinal(Base64.getDecoder().decode(data));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e.getMessage());
        }

        assert decryptedValue != null;
        return new String(decryptedValue);
    }
}
