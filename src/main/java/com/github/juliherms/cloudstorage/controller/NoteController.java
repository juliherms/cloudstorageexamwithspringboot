package com.github.juliherms.cloudstorage.controller;

import java.util.List;
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

import com.github.juliherms.cloudstorage.model.Note;
import com.github.juliherms.cloudstorage.model.User;
import com.github.juliherms.cloudstorage.service.NoteService;
import com.github.juliherms.cloudstorage.service.UserService;

/**
 * Class responsible to represent code behind about Note page
 * 
 * @author jlv
 *
 */
@Controller
@RequestMapping("/notes")
public class NoteController {

	private final Logger logger = LoggerFactory.getLogger(NoteController.class);
	private final NoteService noteService;
	private final UserService userService;
	private final MessageSource messageSource;

	private final String SUCCESS_MESSAGE = "noteSuccessMessage";
	private final String ERROR_MESSAGE = "noteErrorMessage";

	public NoteController(NoteService noteService, UserService userService, MessageSource messageSource) {
		this.noteService = noteService;
		this.userService = userService;
		this.messageSource = messageSource;
	}

	/**
	 * Method responsible to save note
	 * 
	 * @param note
	 * @param authentication
	 * @param ra
	 * @return
	 */
	@PostMapping("/save")
	public String save(@ModelAttribute("note") Note note, Authentication authentication, RedirectAttributes ra) {

		User user = getUserAuthenticated(authentication.getName());

		Integer userId = user.getUserId();

		String errorMsg;
		String successMsg;

		// if note form is empty
		if (note == null) {
			errorMsg = messageSource.getMessage("notes-tab.note-empty-form-msg", null, Locale.getDefault());
			ra.addFlashAttribute(ERROR_MESSAGE, errorMsg);
			logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);
			return "redirect:/home#nav-notes";

		} // note description length check
		else if (note.getNoteDescription().length() > 1000) {

			errorMsg = messageSource.getMessage("notes-tab.note-description-too-long-msg", null, Locale.getDefault());
			ra.addFlashAttribute(ERROR_MESSAGE, errorMsg);
			logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);
			return "redirect:/home#nav-notes";

		} // if the same note submitted again from the same user
		else if (noteService.exists(note, userId)) {

			errorMsg = messageSource.getMessage("notes-tab.note-already-exists-msg", null, Locale.getDefault());
			ra.addFlashAttribute(ERROR_MESSAGE, errorMsg);
			logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);
			return "redirect:/home#nav-notes";
		}

		// if insert
		if (note.getNoteId() == null) {

			note.setUserId(userId);
			int noteSaved = noteService.create(note);

			// if note was not saved for some reason
			if (noteSaved != 1) {

				errorMsg = messageSource.getMessage("notes-tab.note-save-error-msg", null, Locale.getDefault());
				ra.addFlashAttribute(ERROR_MESSAGE, errorMsg);
				logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);

			} else {

				successMsg = messageSource.getMessage("notes-tab.note-save-success-msg", null, Locale.getDefault());
				ra.addFlashAttribute(SUCCESS_MESSAGE, successMsg);
				ra.addFlashAttribute("notes", getAllNotesByUser(user));

			}

		} else {

			note.setUserId(user.getUserId());

			int updated = noteService.update(note, userId);

			if (updated != 1) {

				errorMsg = messageSource.getMessage("notes-tab.note-update-error-msg", null, Locale.getDefault());
				ra.addFlashAttribute(ERROR_MESSAGE, errorMsg);
				logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);

			} else {

				successMsg = messageSource.getMessage("notes-tab.note-update-success-msg", null, Locale.getDefault());
				ra.addFlashAttribute(SUCCESS_MESSAGE, successMsg);
				ra.addFlashAttribute("notes", getAllNotesByUser(user));
			}

		}

		return "redirect:/home#nav-notes";
	}

	/**
	 * Method responsible to delete note
	 * @param id
	 * @param authentication
	 * @param ra
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String deleteNote(@PathVariable("id") Integer id, Authentication authentication, RedirectAttributes ra) {
		
		User user = getUserAuthenticated(authentication.getName());
		
		Integer userId = user.getUserId();
		int deleted = noteService.delete(id, userId);
		
		if (deleted == 0) {
			
			logger.error("Note with id = " + id + " was not deleted");
			String errorMsg = messageSource.getMessage("notes-tab.note-delete-error-msg", null, Locale.getDefault());
			ra.addFlashAttribute(ERROR_MESSAGE, errorMsg);
			
		} else {
			
			String successMsg = messageSource.getMessage("notes-tab.note-delete-success-msg", null,
					Locale.getDefault());
			ra.addFlashAttribute(SUCCESS_MESSAGE, successMsg);
		}
		
		ra.addFlashAttribute("files", getAllNotesByUser(user));
		return "redirect:/home#nav-notes";
	}

	/**
	 * Method responsible to list Notes by user
	 * 
	 * @param user
	 * @return
	 */
	private List<Note> getAllNotesByUser(User user) {
		return noteService.getNotes(user.getUserId());
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
