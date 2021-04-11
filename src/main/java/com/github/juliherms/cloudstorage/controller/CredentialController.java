package com.github.juliherms.cloudstorage.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.juliherms.cloudstorage.model.Credential;
import com.github.juliherms.cloudstorage.model.User;
import com.github.juliherms.cloudstorage.service.CredentialService;
import com.github.juliherms.cloudstorage.service.UserService;

/**
 * This class responsible to code behind for credentials page
 * 
 * @author jlv
 *
 */
@Controller
@RequestMapping("/credentials")
public class CredentialController {

	private final Logger logger = LoggerFactory.getLogger(CredentialController.class);
	private final MessageSource messageSource;
	private final CredentialService service;
	private final UserService userService;
	private final String SUCCESS_MESSAGE = "credentialSuccessMessage";
	private final String ERROR_MESSAGE = "credentialErrorMessage";

	public CredentialController(CredentialService credentialService, UserService userService,
			MessageSource messageSource) {
		this.service = credentialService;
		this.userService = userService;
		this.messageSource = messageSource;
	}
	
	@PostMapping("/save")
	public String saveCredential(@ModelAttribute("credential") Credential credential, Authentication authentication,
			RedirectAttributes ra) {
		
		// extract auth user
		User user = getUserAuthenticated(authentication.getName());
		
		Integer userId = user.getUserId();

		String errorMessage;
		String successMessage;

		if (credential == null) {
			
			errorMessage = messageSource.getMessage("credentials-tab.credential-empty-form-msg", null,
					Locale.getDefault());
			ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
			logger.error("Credential error: " + errorMessage + " userid = " + userId);
			return "redirect:/home#nav-credentials";
			
		} else if (service.exists(credential, userId)) {
			
			errorMessage = messageSource.getMessage("credentials-tab.credential-already-exists-msg", null,
					Locale.getDefault());
			ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
			logger.error("Credential error: " + errorMessage + " userid = " + userId);
			return "redirect:/home#nav-credentials";
			
		}
		if (credential.getCredentialId() == null) {
			
			int noteId;
			credential.setUserId(userId);
			noteId = service.save(credential);
			if (noteId != 1) {
				errorMessage = messageSource.getMessage("credentials-tab.credential-save-error-msg", null,
						Locale.getDefault());
				ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
				logger.debug("Credential error: " + errorMessage + " userid = " + userId);
			} else {
				successMessage = messageSource.getMessage("credentials-tab.credential-save-success-msg", null,
						Locale.getDefault());
				ra.addFlashAttribute(SUCCESS_MESSAGE, successMessage);
				ra.addFlashAttribute("credentials", service.getAllByUserId(user.getUserId()));
			}
		} else {
			
			credential.setUserId(user.getUserId());
			int updated = service.update(credential, userId);
			if (updated != 1) {
				errorMessage = messageSource.getMessage("credentials-tab.credential-update-error-msg", null,
						Locale.getDefault());
				ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
				logger.debug("Credential error: " + errorMessage + " userid = " + userId);
			} else {
				successMessage = messageSource.getMessage("credentials-tab.credential-update-success-msg", null,
						Locale.getDefault());
				ra.addFlashAttribute(SUCCESS_MESSAGE, successMessage);
				ra.addFlashAttribute("credentials", service.getAllByUserId(user.getUserId()));
			}
			
		}
		return "redirect:/home#nav-credentials";
	}

	/**
	 * This method responsible to delete credential
	 * 
	 * @param id
	 * @param authentication
	 * @param ra
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String deleteNote(@PathVariable("id") Integer id, Authentication authentication, RedirectAttributes ra) {

		// extract auth user
		User user = getUserAuthenticated(authentication.getName());
		// delete credential by user
		int deleted = service.delete(id, user.getUserId());

		if (deleted == 0) {

			logger.error("Credential with id = " + id + " was not deleted");
			// create error message
			ra.addFlashAttribute(ERROR_MESSAGE,
					messageSource.getMessage("credentials-tab.credential-delete-error-msg", null, Locale.getDefault()));

		} else {
			// create success message
			ra.addFlashAttribute(SUCCESS_MESSAGE, messageSource
					.getMessage("credentials-tab.credential-delete-success-msg", null, Locale.getDefault()));
		}
		// list all files by user id
		ra.addFlashAttribute("files", service.getAllByUserId(user.getUserId()));

		// redirect to page
		return "redirect:/home#nav-credentials";
	}
	
	/**
	 * Method responsible to find User authenticated
	 * 
	 * @param username
	 * @return
	 */
	private User getUserAuthenticated(String username) {
		return userService.getUser(username);
	}

}
