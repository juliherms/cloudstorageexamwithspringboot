package com.github.juliherms.cloudstorage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.juliherms.cloudstorage.model.User;
import com.github.juliherms.cloudstorage.service.CredentialService;
import com.github.juliherms.cloudstorage.service.FileService;
import com.github.juliherms.cloudstorage.service.NoteService;
import com.github.juliherms.cloudstorage.service.UserService;

/**
 * This class responsible to code behind for home page
 * 
 * @author jlv
 *
 */
@Controller
@RequestMapping("/")
public class HomeController {

	private final NoteService noteService;
	private final UserService userService;
	private final FileService fileService;
	private final CredentialService credentialService;

	public HomeController(NoteService noteService, UserService userService, FileService fileService,
			CredentialService credentialService) {

		this.noteService = noteService;
		this.userService = userService;
		this.fileService = fileService;
		this.credentialService = credentialService;
	}

	/**
	 * Method responsible to display page home in the systems
	 * 
	 * @param authentication
	 * @param model
	 * @return
	 */
	@GetMapping("/home")
	public String view(Authentication authentication, Model model) {

		User user = userService.getUser(authentication.getName());

		// load data for user
		if (user != null) {
			Integer userId = user.getUserId();
			model.addAttribute("files", fileService.getFilesByUserId(userId));
			model.addAttribute("notes", noteService.getNotes(userId));
			model.addAttribute("credentials", credentialService.getAllByUserId(userId));
		}
		return "home";
	}
}
