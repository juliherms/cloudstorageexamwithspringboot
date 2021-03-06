package com.github.juliherms.cloudstorage.service;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.github.juliherms.cloudstorage.mapper.UserMapper;
import com.github.juliherms.cloudstorage.model.User;

/**
 * This class responsible to provide business logic for user
 * @author jlv
 *
 */
@Service
public class UserService {
	
	private final UserMapper userMapper;
	private final HashService hashService;

	//dependency inject by constructor
	public UserService(UserMapper userMapper, HashService hashService) {
		this.userMapper = userMapper;
		this.hashService = hashService;
	}

	public boolean isUsernameAvailable(String username) {
		return userMapper.getUser(username) == null;
	}

	/**
	 * Method responsible to create user and return your id
	 * @param user
	 * @return
	 */
	public int createUser(User user) {
		
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		String encodedSalt = Base64.getEncoder().encodeToString(salt);
		String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
		return userMapper.insert(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(),
				user.getLastName()));
	}

	/**
	 * Get user by user name
	 * @param username
	 * @return
	 */
	public User getUser(String username) {
		return userMapper.getUser(username);
	}
}
