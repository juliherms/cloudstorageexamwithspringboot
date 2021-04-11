package com.github.juliherms.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.juliherms.cloudstorage.model.Note;
import com.github.juliherms.cloudstorage.service.NoteService;

/**
 * This class responsible to code behind for home page
 * 
 * @author jlv
 *
 */
@Controller
@RequestMapping("/home")
public class HomeController {

	private final NoteService noteService;

	public HomeController(NoteService noteService) {
		this.noteService = noteService;
	}

	/**
	 * Display page
	 * 
	 * @return
	 */
	@GetMapping()
	public String view(Model model) {
		model.addAttribute("notes", this.noteService.getNotes());
		return "home";
	}

	@PostMapping("note")
	public String createNote(@ModelAttribute Note note, Model model) {

		note.setUserId(1);
		noteService.create(note);

		return "home";
	}

}
