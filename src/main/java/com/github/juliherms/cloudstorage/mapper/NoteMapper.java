package com.github.juliherms.cloudstorage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.github.juliherms.cloudstorage.model.Note;

/**
 * This interface responsible to access database repository
 * 
 * @author jlv
 *
 */
@Mapper
public interface NoteMapper {

	@Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
	@Options(useGeneratedKeys = true, keyProperty = "noteId")
	int insert(Note note);

	@Select("SELECT * FROM NOTES WHERE userid = #{userId}")
	List<Note> getNotesByUserId(int userId);;
	
}
