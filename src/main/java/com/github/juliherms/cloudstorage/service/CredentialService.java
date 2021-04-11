package com.github.juliherms.cloudstorage.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.juliherms.cloudstorage.mapper.CredentialMapper;
import com.github.juliherms.cloudstorage.model.Credential;

/**
 * This class responsible to business logic for credentials
 * 
 * @author jlv
 *
 */
@Service
public class CredentialService {

	private final EncryptionService encryptionService;
	private final CredentialMapper credentialMapper;

	public CredentialService(EncryptionService encryptionService, CredentialMapper credentialMapper) {
		this.encryptionService = encryptionService;
		this.credentialMapper = credentialMapper;
	}

	/**
	 * Method responsible to save credential
	 * 
	 * @param credential
	 * @return
	 */
	public int save(Credential credential) {
		return credentialMapper.insert(encryptCred(credential));
	}

	/**
	 * Method responsible to list credentials by user Id
	 * 
	 * @param userId
	 * @return
	 */
	public List<Credential> getAllByUserId(Integer userId) {

		List<Credential> credentials = credentialMapper.getCredentialsByUserId(userId);
		List<Credential> result = new ArrayList<>();

		for (Credential credential : credentials) {
			result.add(decryptCred(credential));
		}
		return result;
	}

	/**
	 * This method responsible to generate key and password for url file
	 * 
	 * @param credential
	 * @return
	 */
	private Credential encryptCred(Credential credential) {
		String key = credential.getKey();
		if (key == null) {
			SecureRandom sr = new SecureRandom();
			byte[] keyBytes = new byte[16];
			sr.nextBytes(keyBytes);
			key = Base64.getEncoder().encodeToString(keyBytes);
		}
		String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
		credential.setKey(key);
		credential.setPassword(encryptedPassword);
		return credential;
	}

	/**
	 * This method responsible to update credential
	 * 
	 * @param credential
	 * @param userId
	 * @return
	 */
	public int update(Credential credential, Integer userId) {

		Credential existing = credentialMapper.getCredentialById(credential.getCredentialId());
		// check owner for credential
		if (existing.getUserId().equals(userId)) {
			return credentialMapper.update(encryptCred(credential));
		} else {
			return 0;
		}
	}

	/**
	 * Method responsible to delete credential
	 * @param id
	 * @param userId
	 * @return
	 */
	public int delete(Integer id, Integer userId) {
		return credentialMapper.deleteById(id, userId);
	}

	/**
	 * This method responsible to decript key and password for url file
	 * 
	 * @param credential
	 * @return
	 */
	private Credential decryptCred(Credential credential) {
		String key = credential.getKey();
		String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), key);
		credential.setDecryptedPassword(decryptedPassword);
		return credential;
	}
	
	/**
	 * This method responsible to check credentials exists in the system 
	 * @param credential
	 * @param userId
	 * @return
	 */
	public boolean exists(Credential credential, Integer userId) {
		
        Credential exists = credentialMapper.getCredential(
                credential.getUrl(),
                credential.getUsername(),
                userId
        );
        
        if (exists == null) return false;
        else {
            String key = exists.getKey();
            String decryptedPassword = encryptionService.decryptValue(exists.getPassword(), key);
            return decryptedPassword.equals(credential.getPassword());
        }
    }

}
