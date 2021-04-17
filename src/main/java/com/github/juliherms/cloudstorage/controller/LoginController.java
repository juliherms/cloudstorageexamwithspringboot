package com.github.juliherms.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This class responsible to code behind for login page
 * 
 * @author jlv
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	/**
	 * Display page
	 * 
	 * @return
	 */
	@GetMapping()
	public String view() {
		return "login";
	}

	@PostMapping
	public String submitCredentials() {
		return "login";
	}
	
	
}
