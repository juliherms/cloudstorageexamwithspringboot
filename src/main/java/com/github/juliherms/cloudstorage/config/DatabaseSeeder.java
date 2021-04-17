package com.github.juliherms.cloudstorage.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.github.juliherms.cloudstorage.model.User;
import com.github.juliherms.cloudstorage.service.UserService;

@Component
public class DatabaseSeeder implements ApplicationRunner {

	private final UserService userService;

	public DatabaseSeeder(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void run(ApplicationArguments args) {
		User user = new User(null, "admin", "admin", "admin", "admin", "admin");
		userService.createUser(user);
	}

}
