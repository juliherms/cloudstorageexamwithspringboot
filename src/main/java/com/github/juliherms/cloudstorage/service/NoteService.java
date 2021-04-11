package com.github.juliherms.cloudstorage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.juliherms.cloudstorage.mapper.NoteMapper;
import com.github.juliherms.cloudstorage.model.Note;

/**
 * This class responsible to provide business logic for note
 * 
 * @author jlv
 *
 */
@Service
public class NoteService {

	private final NoteMapper noteMapper;

	// dependency inject by constructor
	public NoteService(NoteMapper noteMapper) {
		this.noteMapper = noteMapper;
	}

	/**
	 * Method responsible to create note
	 * 
	 * @param note
	 * @return
	 */
	public int create(Note note) {

		// get the user id logged
		return noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
	}

	public List<Note> getNotes() {
		return noteMapper.getNotesByUserId(1);
	}

}
