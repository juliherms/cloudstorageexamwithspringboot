package com.github.juliherms.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.juliherms.cloudstorage.model.User;
import com.github.juliherms.cloudstorage.service.UserService;

/**
 * This class responsible to code behind for signup page
 * 
 * @author jlv
 *
 */
@Controller()
@RequestMapping("/signup")
public class SignupController {

	private final UserService userService;

	public SignupController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Method responsible to display page
	 * 
	 * @return
	 */
	@GetMapping()
	public String signupView() {
		return "signup";
	}

	/**
	 * Method responsible to create a new user 
	 * @param user
	 * @param model
	 * @return
	 */
	@PostMapping()
	public String signupUser(@ModelAttribute User user, Model model) {
		String signupError = null;

		//check username available
		if (!userService.isUsernameAvailable(user.getUsername())) {
			signupError = "The username already exists.";
		}

		if (signupError == null) {
			
			int rowsAdded = userService.createUser(user);
			if (rowsAdded < 0) {
				signupError = "There was an error signing you up. Please try again.";
			}
		}

		if (signupError == null) {
			model.addAttribute("signupSuccess", true);
		} else {
			model.addAttribute("signupError", signupError);
		}

		return "signup";
	}
}
