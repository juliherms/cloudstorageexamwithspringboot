package com.github.juliherms.cloudstorage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.juliherms.cloudstorage.model.Note;

/**
 * This interface responsible to access database repository
 * 
 * @author jlv
 *
 */
@Mapper
public interface NoteMapper {

	/**
	 * Method responsible to insert note in the database
	 * @param note
	 * @return
	 */
	@Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
	@Options(useGeneratedKeys = true, keyProperty = "noteId")
	int insert(Note note);

	/**
	 * Method responsible to list notes from user id
	 * @param userId
	 * @return
	 */
	@Select("SELECT * FROM NOTES WHERE userid = #{userId}")
	List<Note> getNotesByUserId(Integer userId);
	
	/**
	 * Method responsible to get note by id
	 * @param noteId
	 * @return
	 */
	@Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNoteById(Integer noteId);
	
	/**
	 * Method responsible to update note by note id
	 * @param note
	 * @return
	 */
	@Update("UPDATE NOTES set notetitle=#{noteTitle}, notedescription=#{noteDescription} WHERE noteid = #{noteId}")
    int update(Note note);
	
	/**
	 * Method responsible to delete note by user id and note id
	 * @param noteId
	 * @param userId
	 * @return
	 */
	@Delete("DELETE FROM NOTES WHERE noteid = #{noteId} AND userid = #{userId}")
	int delete(Integer noteId, Integer userId);
	
	/**
	 * Method responsible to get note by title,description and user
	 * @param noteTitle
	 * @param noteDescription
	 * @param userId
	 * @return
	 */
	@Select("SELECT * FROM NOTES WHERE notetitle=#{noteTitle} AND notedescription=#{noteDescription} AND userid = #{userId}")
    Note getNoteByTitleAndDescription(String noteTitle, String noteDescription, Integer userId);
}
