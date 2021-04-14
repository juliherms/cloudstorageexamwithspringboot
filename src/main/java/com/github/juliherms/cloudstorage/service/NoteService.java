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

	/**
	 * Method responsible to list notes by user
	 * @param userId
	 * @return
	 */
	public List<Note> getNotes(Integer userId) {
		return noteMapper.getNotesByUserId(userId);
	}

	/**
	 * Method responsible to update note
	 * 
	 * @param note
	 * @param userId
	 * @return
	 */
	public int update(Note note, Integer userId) {

		// check exist note
		Note existing = noteMapper.getNoteById(note.getNoteId());

		if (existing.getNoteId().equals(userId))
			return noteMapper.update(note);
		else
			return 0;
	}

	/**
	 * Method responsible to delete note
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	public int delete(Integer id, Integer userId) {
		return noteMapper.delete(id, userId);
	}

	/**
	 * This method responsible to check exist note
	 * @param note
	 * @param userId
	 * @return
	 */
	public boolean exists(Note note, Integer userId) {
		Note returnNote = noteMapper.getNoteByTitleAndDescription(note.getNoteTitle(), note.getNoteDescription(),
				userId);
		return returnNote != null;
	}
}